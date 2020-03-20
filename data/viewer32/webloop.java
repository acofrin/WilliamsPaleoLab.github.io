//
// WebLoop.java - Displays an animation loop of one set of images.
// Version - 3.2
// 7/3/2002
// Phil Leduc
// Copyright (c)  Geology Dept., Brown University
//
//

import java.awt.*;
import java.awt.image.*;
import java.applet.Applet;

// *******************************************************

class webloop extends Canvas implements Runnable{

	private static Applet ViewerApplet;

	private int RequestedDelay, Delay;
	private long SlideTimeout;

	private Thread Animator;

  private boolean StopLoop = true;	// Automatic slide presentation.
	private boolean ReverseLoop = false;	// Reversing the sequence of slides
	private boolean useSiteOverlays = false;	// Site overlays on top of slides
  private boolean FlickerImages = false;  // Use flicker mode
  private boolean FlickerReflect = false;  // Reflects direction of loop

	// Prefixes for overlay file names
	private String SiteOverlayPrefix = "sov";  // Site overlay maps

  // Debug variables
  private int DebugCount;
  private String DebugMsg1, DebugMsg2, DebugMsg3, DebugMsg4;
  private boolean Debug      = false;  // ****** Debug flag.

	private Dimension OffScreenDimension;
	private Graphics OffScreenGraphics;
	private MediaTracker ImageDownloadTracker, UpdateTracker;

	private int NumOfImages = 23; // The number of images in each image set of
                        // pollen maps
  private int ProcessedImages;  // Keeps track of how many images have been uploaded
                        // into memory
	private int Frame;  // Frame index

	private Image OffScreenImage;
  private Image OriginalImage;

	// The Frames[] contains Pollen image sets and
  // SiteFrames[] contains site overlays image sets
	private Image Frames[], SiteFrames[];

//
// ************* Start of Methods ***************
//

	//
	// The constructor - Initialize the WebLoop
	//
	public webloop(String PollenPrefix, Applet app) {

     // Declare instance variables.

		ViewerApplet = app;

     DebugMsg1 = DebugMsg2 = DebugMsg3 = DebugMsg4 = "WebLoop:No message yet.";
		DebugCount = 0;

		int fps = 1;  // Default frame rate in frames per second
		RequestedDelay = 1000 / fps;  // Convert frame rate to milliseconds
		Delay = RequestedDelay;  // delay is current delay value;
                      // RequestedDelay is user requested value via Delay slide

		ImageDownloadTracker = new MediaTracker(this);
		UpdateTracker = new MediaTracker(this);

     ProcessedImages = 0;
		Frames     = new Image[NumOfImages];  // current frames/slides being shown
     SiteFrames = new Image[NumOfImages];  // site frames to be overlaid on
                                     // current frames

     // Read the images into the image arrays.
     DebugMsg1 = PollenPrefix;

		readNewFrames(SiteOverlayPrefix,SiteFrames);  // Upload site overlay images
		readNewFrames(PollenPrefix,Frames); // Upload pollen images

		Frame = 0;
		start();  // Start the Animator's thread.

	}

//
// *******************************************************
//

  //
  // Read a new set of animation frames into an Image array  -  called
  // by init()
  //
  public void readNewFrames(String pollenAbrv, Image[] Frames) {

		// vary the image index from highest to lowest, e.g., 22 to 0 when
		// number of images (NumOfImages) is 23.
		for (int i = NumOfImages - 1 ; i >= 0 ; i--) {

			int ti = i - 1;  // convert image index to time interval index
        if (ti  >= 10)   // e.g. 21 implying 21K when i = 22

			   // Build the image's file name and upload the image
			   OriginalImage = ViewerApplet.getImage(ViewerApplet.getDocumentBase(),
                       "images/" + pollenAbrv + ti + "000.gif");

        else
				if (ti >= 1)  // for 9,8,...,2,1

					// Build the image's file name and upload the image
					OriginalImage =
                    ViewerApplet.getImage(ViewerApplet.getDocumentBase(),
							"images/" + pollenAbrv + "0" + ti + "000.gif");
				else // for 0 and -1!

					if (ti == 0)

						// Build the image's file name for 500 cal yr ago
						//  and upload the image
						OriginalImage =
                       ViewerApplet.getImage(ViewerApplet.getDocumentBase(),
								"images/" + pollenAbrv + "00500.gif");
					else

						// Build the image's file name for Modern interval,
						// and upload the image
						OriginalImage =
                       ViewerApplet.getImage(ViewerApplet.getDocumentBase(),
								"images/" + pollenAbrv + "00000.gif");


        // Store the image file into the working image arrays.
        Frames[NumOfImages - (i + 1)] = OriginalImage;

			ImageDownloadTracker.addImage(Frames[NumOfImages - (i + 1)], 1);
                                            // Setup the image tracker
															// so that we can find out
															// when the upload completes.

		}  // end for loop
  }

//
// *******************************************************
//

  //
  // Read a new set of animation frames into an Image array  -  called
  // by init()
  //
  public void readNewFrames(String pollenAbrv) {

     readNewFrames(pollenAbrv,Frames);

  }

//
// *******************************************************
//

	//
	// Start the animation by forking a thread.  -  Called by constructor
	//
	public void start() {
                		
		if (Animator == null) {  // If the Animator thread does not exist,
                              // create it.
			Animator = new Thread(this);
			Animator.start();  //  When the Animator(i.e. thread) starts it will
                           //  eventually call the WebLoop.run() method.
		}
	}

//
// *******************************************************
//

	//
	// Run the image loop. This method is called by class Thread.
  // run() is an abstract Runnable interface Thread method that is over-ridden
  // by this local run method.
	//
	public void run() {

     // run() is entered only once, so the code between this line and the
     // while loop executes only once.

		// Set the Animator thread's priority
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY-1);

		// Get the current clock time in milliseconds
		SlideTimeout = System.currentTimeMillis();

     // This while loop runs indefinitely.
		while (Thread.currentThread() == Animator) {

			repaint();// repaint is a method in Component which is a
                  // superclass to Canvas. repaint calls the update(Graphics g)
                  // method in Component BUT the component update method
                  // is over-ridden by the local update method below.

			// Delay depending on how far we are behind.
			SlideTimeout += Delay;  // Set the timer by adding the
                                // current delay time in milliseconds

			// Get the current time
			long CurrentTime = System.currentTimeMillis();

			// While the target time has not been reached, sleep for only
        // 50 milliseconds at a time. This allows for a quick response to
        // user requests since -- This thread's while loop can be forced
        // to exit, by setting the SlideTimeout to the current time while in
        // another thread, say in a method called by WebViewer in response
        // to a button action.

			while ((SlideTimeout - CurrentTime > 0) &&
			       (60000 > SlideTimeout - CurrentTime)) {  // Watch out for
                    // clock-around, from positive to negative values
				try {
					Thread.sleep(Math.max(0, 50));
				}
           catch (InterruptedException e) {
					break;
				}
				CurrentTime = System.currentTimeMillis();

			}

			// Advance the frame if
			// 1) in animation mode (!StopLoop) and
        // 2) all images have been read into memory (tracker indicates
        //    COMPLETE)

        if ((! StopLoop) &&
            (ImageDownloadTracker.statusID(1, true) == MediaTracker.COMPLETE)) {

				Delay = RequestedDelay;  // Update the current delay time.
										       // Normally, there will be no change.

				// If in reverse mode, decrement the frame index, so that present
				// to past images are displayed.
				if (ReverseLoop) {

					Frame--;

              if (FlickerImages) {
                  if (FlickerReflect) Frame = Frame + 2;
                  FlickerReflect = !FlickerReflect;
              }

					// Set a 4 second delay at the end of the image loop.
					if ((Frame == 0) &&
                  ! FlickerImages) Delay = 4000;  // Intermission 4000 msec
				}
				// If not in reverse mode, increment the frame index, so that past
				// to present images are displayed.
				else {

					Frame++;

              if (FlickerImages) {
                  if (FlickerReflect) Frame = Frame - 2;
                  FlickerReflect = !FlickerReflect;
              }

					// Set a 4 second delay at the end of the image loop.
					if ((Frame == NumOfImages - 1) &&
                   ! FlickerImages) Delay = 4000;  // 4000 msec
				}

				// Check for wrap-around
				if (Frame > NumOfImages - 1) {
					Frame = 0;
				}
				if (Frame < 0) {
					Frame = NumOfImages - 1;
				}

			}

		}

        // Code (if any) below the while loop is not executed.

	}


//
// *******************************************************
//

	//
	// Stop the animation. The Animator thread will exit because Animator is
  // set to null.
	//
	public void stop() {

		if (Animator != null) {
			Animator = null;
			OffScreenImage = null;
			OffScreenGraphics = null;
		}
	}


//
// *******************************************************
//

	public void paint(Graphics g) {

		update(g);
	}

//
// *******************************************************
//

	//
	// Override Component's update method to avoid flicker caused by clearRect()
	// 
	public void update(Graphics g) {

		Dimension d = getSize(); // size returns the size of the current component,
							   // WebLoop, which is part of the MultiViewer
							   // component, and is where the images are displayed.

     int w = d.width;
     int h = d.height;


     if (Debug) {
         g.drawString(DebugMsg1,120,35);  // ***** Debug
         g.drawString(DebugMsg2,120,55);  // ***** Debug
         g.drawString(DebugMsg3,120,75);  // ***** Debug
         g.drawString(DebugMsg4,120,95);  // ***** Debug
     }

		// On the first pass or when the dimensions of the slides changes,
		// create an offscreen graphics, a place where
		// the images can be prepared and "blinking" goes unseen.
     // The off-screen graphic will be the same size as the canvas, WebLoop.

		if ((OffScreenGraphics == null) || (d.width != OffScreenDimension.width)
			|| (d.height != OffScreenDimension.height)) {

        ProcessedImages = 0;        // No images are processed yet.
			OffScreenDimension = d;	    // OffScreenDimension is initialized here!

			OffScreenImage = createImage(w, h);
			OffScreenGraphics = OffScreenImage.getGraphics();

		}

		// Erase old off-screen graphics, user can not see this happening
		// This is what would cause blinking if done on screen.

		OffScreenGraphics.setColor(getBackground());
		OffScreenGraphics.fillRect(0, 0, d.width, d.height);
		OffScreenGraphics.setColor(Color.black);

		// Paint a frame from frames[] into the off-screen graphics context.
		paintFrame(OffScreenGraphics);

		// Paint the completed image in the off-screen context onto
     // the screen.
     // Draws the specified image into this (WebLoop)
     // graphics context's coordinate space. 
		g.drawImage(OffScreenImage, 0, 0, null);

     if (Debug) {
         g.drawString(DebugMsg1,120,35);  // ***** Debug
         g.drawString(DebugMsg2,120,55);  // ***** Debug
         g.drawString(DebugMsg3,120,75);  // ***** Debug
         g.drawString(DebugMsg4,120,95);  // ***** Debug
     }

	}


//
// *******************************************************
//

  //
	// Paint a frame of animation.
	// 
	public void paintFrame(Graphics g) {


		// If all the images have been uploaded
		if (ImageDownloadTracker.statusID(1, true) == MediaTracker.COMPLETE) {

        // Get the dimensions of the current graphic.
			int w = Frames[Frame].getWidth(this);
			int h = Frames[Frame].getHeight(this);

        // Draw image on selected graphics, usually the off-screen
        // graphics
			g.drawImage(Frames[Frame], 0, 0, w, h, this);

			// If site overlays are requested, draw the sites on top of the
			// map image. Note that the site overlays are transparent and so
			// do not erase the map images. (Black pixels are transparent.)
			if (useSiteOverlays)
           g.drawImage(SiteFrames[Frame], 0, 0, w, h,this);

     }
     else {
			// The pollen images have not been read yet so tell the user.
			g.drawString("Reading image files...",15,15);
		}

     if (Debug) {
         g.drawString(DebugMsg1,120,35);  // ***** Debug
         g.drawString(DebugMsg2,120,55);  // ***** Debug
         g.drawString(DebugMsg3,120,75);  // ***** Debug
         g.drawString(DebugMsg4,120,95);  // ***** Debug
     }

	}


//
// *******************************************************
//

    //
    // Change the delay between animation frames.
    //
    public void updateFrameDelay(int newTime)  {

		  // Store the new user requested delay time.
       RequestedDelay = newTime;

		  // Set the slide timeout to the current time so that
		  // the wait while loop in run() will terminate, and the
		  // new requested delay time will be put to use
		  // as soon as the Animator thread wakes up.
		  SlideTimeout = System.currentTimeMillis();
    }

//
// *******************************************************
//

	public void setReverseLoopFlag(boolean newReverseLoop) {

		// Store the user's request for loop direction.
		// True implies present to past.
		// False implies past to present.
		ReverseLoop = newReverseLoop;
	}

//
// *******************************************************
//

    public void setSiteOverlaysFlag(boolean newState)  {

		// Store the user's site overlay request.
		// True implies overlay the site images, which are transparent
		// except for dots representing site location, on the pollen maps
		// False implies do not use site overlays.
        useSiteOverlays = newState;

    }

//
// *******************************************************
//

    public void setStopLoopFlag(boolean newState)  {

		// Store the user's stop loop request.
		// True means stop the automatic presentation of images, i.e., slide mode.
		// False means activate the automatic presentation of images, i.e.,
     // animation mode.
		StopLoop = newState;

    }

//
// *******************************************************
//

    //
    // Change the slide being shown in slide mode.
    //
    public void changeSlide(int delta)  {

		// Delta = 1 means past to present direction
        if (delta == 1) Frame++;

		// Delta = -1 means present to past direction
        else if (delta == -1) Frame--;

        else ; // do nothing

		// Handle the wrap-around of the frame index
		if (Frame > NumOfImages - 1) {
			Frame = 0;
		}
		if (Frame < 0) {
			Frame = NumOfImages - 1;
		}

		// Set the timer time to the current time so that 
		// the wait while loop in run() will terminate, and the 
		// new requested frame will be put to use
		// as soon as the Animator thread wakes up.
		SlideTimeout = System.currentTimeMillis();
    }

//
// *******************************************************
//

    //
    // Change the slide being shown in slide mode to the first slide.
    //
    public void rewind()  {


     if (StopLoop) {
        // Set the frame index to the first image.
        Frame = 0;
     }
     else {
        // Set the frame index to the last image.
        if (!ReverseLoop)
           Frame = NumOfImages - 1;
        else
           // If in reverse direction, the next image will be last image.
           Frame = 1;
     }

		// Set the timer time to the current time so that
		// the wait while loop in run() will terminate, and the
		// new requested frame will be put to use
		// as soon as the Animator thread wakes up.
		SlideTimeout = System.currentTimeMillis();

    }

//
// *******************************************************
//

    //
    // Change the slide being shown in slide mode to the first slide.
    //
    public void fastforward()  {


     if (StopLoop) {
        // Set the frame index to the last image.
        Frame = NumOfImages - 1;
     }
     else {
        // Set the frame index to the first image.
        if (!ReverseLoop)
           Frame = NumOfImages - 2;
        else
           Frame = 0;
     }

		// Set the timer time to the current time so that
		// the wait while loop in run() will terminate, and the
		// new requested frame will be put to use
		// as soon as the Animator thread wakes up.
		SlideTimeout = System.currentTimeMillis();
    }

//
// *******************************************************
//

    //
    // Change the slide being shown in slide mode to the first slide.
    //
    public void setFlickerImagesFlag(boolean newState)  {

    FlickerImages = FlickerReflect = newState;

    }

//
// ******************* End of class **********************
//

}

