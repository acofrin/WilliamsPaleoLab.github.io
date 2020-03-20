//
// WebViewer.java - Pollen Viewer 3.2 adds pollen animations (total 50+)
//                  a drop-down selection box that uses Latin
//                  and Common names, new Latin/Common titles on map images,
//                  rewind and fast-forward buttons that override the
//                  loop intermission, and more flexible layout so the viewer
//                  looks better on Mac platforms.
// Version - 3.2
// 7/31/2002
// Phil Leduc
// Copyright (c) Geology Dept., Brown University
//
//

import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.*;
import java.applet.Applet;
import java.awt.event.*;


//
// *****************************************************************
//

public class webviewer extends Applet
                       implements ActionListener,
                                  AdjustmentListener,
                                  ItemListener {

	private static Applet ViewerApplet;

	private webloop pImageLoop; // a Canvas extension!
  private imgcanvas TitleCanvas, LegendCanvas;

  private pollenlist SelectionList;

  private Panel RightPanel;
  private Panel NomenclaturePanel, ButtonPanel;
  private Panel DelayPanel, TogglePanel;
  private Panel FFRewindPanel, ForwardBackPanel;

  private GridBagLayout thisLayout, NomenclatureLayout,
          DelayLayout, ToggleLayout, RightLayout;
  private GridBagConstraints c, nlc, dlc, tlc, rlc;

	private Label pDelayValue;

	private String SelectionPrefix;
  private String SelectionName;

	private Image pLegendImg, pTitleImg;

  private Dimension loopDimension;

  private Label pNomenclatureLabel;
  private CheckboxGroup pLanguageGroup;
  private Checkbox pLatinBox, pCommonBox;

  private String PollenName[];
	private Choice pPollenNameCombo;

	private Checkbox pReverseLoopBox,pSiteBox,pFlickerBox;

	private Button pRewindBtn, pBackBtn, pPauseBtn, pForwardBtn, pFastForwardBtn;

	private Scrollbar pDelayBar;

  private int ListLength;
  private int LatinConstant, CommonConstant;
  private int CurrentNomenclatureInt, CurrentSelectionIndex;

	private boolean StopLoop = true;
	private boolean DisplaySites = false;
	private boolean ReverseLoop = false;
  private boolean FlickerImages = false;

  private static int DelayInt = 1000;  // default frame delay is 1000 msec.

  // Set the dimension of the Applet's size which will be a portion of
  // the web browser's window.
	// Initial size is in logical units
  //	Dimension InitialSize = new Dimension(400, 380);

  private boolean Debug = false;  //  ***** Debug flag.
  private Label pDebug1, pDebug2;
  private String DebugStr;

// Sample debub messages below for String and int.
//
//     pDebug1.setText(SelectionPrefix);
//     pDebug2.setText(String.valueOf(LatinConstant));

//
// *****************************************************************
//

	public void init() {

     // Save current object's reference in the applet variable.
		ViewerApplet = this;

		setBackground(Color.lightGray);

     initializePollenList();  // Need list to build Choice component.

     // Pollen Viewer Title
		pTitleImg = getImage(getCodeBase(),"title.gif");

     // **** Nomenclature radio button
 		pNomenclatureLabel = new Label("Selection Nomenclature");
		pLanguageGroup = new CheckboxGroup();
     if (CurrentNomenclatureInt == LatinConstant) {
  		pLatinBox = new Checkbox("Latin", pLanguageGroup, true);
  		pCommonBox = new Checkbox("Common", pLanguageGroup, false);
     }
     else {
  		pLatinBox = new Checkbox("Latin", pLanguageGroup, false);
  		pCommonBox = new Checkbox("Common", pLanguageGroup, true);
     }
     pLatinBox.addItemListener(this);
     pCommonBox.addItemListener(this);

     // Debug messages as labels (20 characters wide)
     pDebug1 = new Label("Nothing yet.        ");
     pDebug2 = new Label("Nothing yet.        ");
     if (! Debug) {
        pDebug1.setEnabled(false);
        pDebug2.setEnabled(false);
        pDebug1.setVisible(false);
        pDebug2.setVisible(false);
     }


     // Animation frame delay is in millisecs.
		pDelayValue = new Label("Delay:  "+String.valueOf(DelayInt)+" ms");
		pDelayValue.setAlignment(Label.LEFT);

     // **** Delay controls
     // Scrollbar min = 1 and max = 40, initial value = 1
		pDelayBar = new Scrollbar(Scrollbar.HORIZONTAL,10,5,1,45);
		pDelayBar.setUnitIncrement(1);
		pDelayBar.setBlockIncrement(5);
     pDelayBar.addAdjustmentListener(this);

     // **** Manual Slide controls
     // Pause/Go button toggles animation and slide modes.
		pPauseBtn = new Button("Play");
     pPauseBtn.addActionListener(this);
     // Rewind button only usable when in slide mode.
		pRewindBtn = new Button("|<");
     pRewindBtn.addActionListener(this);
     // Back button only usable when in slide mode.
		pBackBtn = new Button("<");
     pBackBtn.addActionListener(this);
     // Forward button only usable when in slide mode.
		pForwardBtn = new Button(">");
     pForwardBtn.addActionListener(this);
     // Fast-forward button only usable when in slide mode.
		pFastForwardBtn = new Button(">|");
     pFastForwardBtn.addActionListener(this);

     // Display Sites checkbox - toggles on and off
		pSiteBox = new Checkbox("Display Sites");
     pSiteBox.addItemListener(this);

     // Reverse Animation checkbox - toggles Reverse loop mode on and off
		pReverseLoopBox = new Checkbox("Reverse Animation");
     pReverseLoopBox.addItemListener(this);

     // Flicker side-by-side Images checkbox - toggles on and off
		pFlickerBox = new Checkbox("Compare Images");
     pFlickerBox.addItemListener(this);

     // Pollen names selection/choice list.
		pPollenNameCombo = new Choice();
     for (int i=0; i<ListLength; i++)
  		pPollenNameCombo.addItem(PollenName[i]);
     pPollenNameCombo.select(CurrentSelectionIndex);
     pPollenNameCombo.addItemListener(this);

     // Image animation loop
		pImageLoop = new webloop(SelectionPrefix, this);
     loopDimension = pImageLoop.getPreferredSize();

     // Legend image
		pLegendImg = getImage(getCodeBase(),
					 "images/" + SelectionPrefix + "lgndh.gif");

// ***** Setup layout panels ************************************

     // Prepare canvases for use in panels.
     // Do this early so that images can be downloaded.
     TitleCanvas = new imgcanvas();
     TitleCanvas.setSize(120,50);  // Dimensions based on title image size
     TitleCanvas.initialize(pTitleImg);

     pImageLoop.setSize(392,371);  // Dimensions based on map image size

     LegendCanvas = new imgcanvas();
     LegendCanvas.setSize(382,41);   // Dimensions based on max legend sizes
     LegendCanvas.initialize(pLegendImg);

     // Prepare a layout for this applet.
     thisLayout = new GridBagLayout ();
     c = new GridBagConstraints();

     // Assign the GridBagLayout to this applet.
		this.setLayout(thisLayout);
     c.fill = GridBagConstraints.NONE;
     c.insets = new Insets(3,10,2,0);
     c.weightx = 0.0;
     c.weighty = 0.0;
     c.anchor = GridBagConstraints.NORTHWEST;

// ****** Left side of this applet ******

     // Add the Title canvas to the left side of this applet.
     // The pTitleImg image will drawn there later. See ImgCanvas.java.
     c.gridx = 1;
     c.gridy = 0;
     c.gridwidth = 1;
     c.gridheight = 2;
     thisLayout.setConstraints(TitleCanvas,c);
     add(TitleCanvas);

     buildNomenclaturePanel();

     // Add the nomenclature panel to the left side of this applet.
     // The nomenclature panel contains a label and the latin and
     // common radio buttons.
     c.gridx = 1;
     c.gridy = 2;
     c.gridwidth = 1;
     c.gridheight = 2;
     thisLayout.setConstraints(NomenclaturePanel,c);
     add(NomenclaturePanel);

     buildDelayPanel();

     // Add the delay panel to the left side of this applet.
     // The delay panel contains a label with the current delay
     // time in milliseconds, a delay scrollbar, and the play/pause
     // rewind, fast-forward, back and forward buttons.
     c.gridx = 1;
     c.gridy = 4;
     c.gridwidth = 1;
     c.gridheight = 2;
     thisLayout.setConstraints(DelayPanel,c);
     add(DelayPanel);

     buildTogglePanel();

     // Add the toggle panel to the left side of this applet.
     c.gridx = 1;
     c.gridy = 6;
     c.gridwidth = 1;
     c.gridheight = 2;
     thisLayout.setConstraints(TogglePanel,c);
     add(TogglePanel);

     // Add the debug labels to the left side of this applet.
     // This labels are visible only when the debug variable
     // is set to true.
     c.gridx = 1;
     c.gridy = 8;
     c.gridwidth = 1;
     c.gridheight = 1;
     thisLayout.setConstraints(pDebug1,c);
     add(pDebug1);

     c.gridx = 1;
     c.gridy = 9;
     c.gridwidth = 1;
     c.gridheight = 1;
     thisLayout.setConstraints(pDebug2,c);
     add(pDebug2);

// ****** Right side of this applet ******

     buildRightPanel();


     c.anchor = GridBagConstraints.NORTH;
     c.gridx = 2;
     c.gridy = 0;
     c.gridwidth = 1;
     c.gridheight = 13;
     thisLayout.setConstraints(RightPanel,c);
     add(RightPanel);

     return;
	} // end of init()


	public void initializePollenList()  {

     // Create pollen lists
     SelectionList = new pollenlist();
     SelectionList.initializeLists();

     // Get the length of the pollen lists.
     ListLength = SelectionList.getListLength();

     // Get Latin and Common nomenclature constants
     LatinConstant = SelectionList.getLatinConstant();
     CommonConstant = SelectionList.getCommonConstant();

     // Get default list selections
     CurrentNomenclatureInt = SelectionList.getDefaultNomenclature();
     CurrentSelectionIndex = SelectionList.getDefaultSelectionIndex();

     // Get prefix for Default pollen
     SelectionPrefix = SelectionList.getFilePrefix(CurrentNomenclatureInt,
                                        CurrentSelectionIndex);

     // Get pollen names
     PollenName = new String[ListLength];
     SelectionList.getPollenNames(CurrentNomenclatureInt, PollenName);

	}

	public void paint(Graphics g)  {

//   This paint method does nothing, since the images are now
//   painted onto canvases through the use ImgCanvas.java a
//   Canvas extension.
//   Note that pImageLoop is also a Canvas extension and does not
//   require the use of ImgCanvas.  WebLoop.java does the painting
//   for the animations.

     return;
	}

//
// *****************************************************************
//

	public void buildNomenclaturePanel() {

     NomenclaturePanel = new Panel();

     // Prepare the panel's layout
     NomenclatureLayout = new GridBagLayout ();
     nlc = new GridBagConstraints();
     nlc.fill = GridBagConstraints.HORIZONTAL;
     nlc.insets = new Insets(0,0,0,0);
     nlc.weightx = 0.0;
     nlc.weighty = 0.0;
     nlc.anchor = GridBagConstraints.NORTHWEST;

     // Assign the layout to the panel
     NomenclaturePanel.setLayout(NomenclatureLayout);

     // Add the Delay: xxxx ms label to the panel
     nlc.gridx = 0;
     nlc.gridy = 0;
     nlc.gridwidth = 1;
     nlc.gridheight = 1;
     NomenclatureLayout.setConstraints(pNomenclatureLabel,nlc);
     NomenclaturePanel.add(pNomenclatureLabel);

     // Add the Latin radio button to the panel
     nlc.gridy = 1;
     NomenclatureLayout.setConstraints(pLatinBox,nlc);
     NomenclaturePanel.add(pLatinBox);

     // Add the Common radio button to the panel
     nlc.gridy = 2;
     NomenclatureLayout.setConstraints(pCommonBox,nlc);
     NomenclaturePanel.add(pCommonBox);

     return;

  }

//
// *****************************************************************
//

	public void buildRightPanel() {

     RightPanel = new Panel();

     // Prepare the panel's layout.
     RightLayout = new GridBagLayout ();
     rlc = new GridBagConstraints();
     rlc.fill = GridBagConstraints.BOTH;
     rlc.insets = new Insets(0,0,4,0);
     rlc.weightx = 0.0;
     rlc.weighty = 0.0;
     rlc.anchor = GridBagConstraints.NORTHWEST;

     // Assign the layout to the panel.
     RightPanel.setLayout(RightLayout);

     // Add the pollen selection combo to the top
     // of the right panel.
     rlc.gridx = 0;
     rlc.gridy = 0;
     rlc.gridwidth = 1;
     rlc.gridheight = 1;
     RightLayout.setConstraints(pPollenNameCombo,rlc);
     RightPanel.add(pPollenNameCombo);

     // Add the animation loop to the panel.
     rlc.gridx = 0;
     rlc.gridy = 1;
     rlc.gridwidth = 1;
     rlc.gridheight = 11;
     RightLayout.setConstraints(pImageLoop,rlc);
     RightPanel.add(pImageLoop);

     // Add the legend canvas to the panel.
     // the legend image will be drawn into this canvas.
     // See ImgCanvas.java.
     rlc.anchor = GridBagConstraints.NORTH;
     rlc.gridx = 0;
     rlc.gridy = 12;
     rlc.gridwidth = 1;
     rlc.gridheight = 2;
     rlc.weightx = 0.0;
     rlc.weighty = 0.0;
     RightLayout.setConstraints(LegendCanvas,rlc);
     RightPanel.add(LegendCanvas);

     return;

  }

//
// *****************************************************************
//

	public void buildTogglePanel() {

     TogglePanel = new Panel();

     // Prepare the panel's layout
     ToggleLayout = new GridBagLayout ();
     tlc = new GridBagConstraints();
     tlc.fill = GridBagConstraints.HORIZONTAL;
     tlc.insets = new Insets(0,0,0,0);
     tlc.weightx = 0.0;
     tlc.weighty = 0.0;
     tlc.anchor = GridBagConstraints.NORTHWEST;

     // Assign the layout to the panel.
     TogglePanel.setLayout(ToggleLayout);

     // Add the display sites toggle checkbox to the panel
     tlc.gridx = 1;
     tlc.gridy = 0;
     tlc.gridwidth = 1;
     tlc.gridheight = 1;
     ToggleLayout.setConstraints(pSiteBox,tlc);
     TogglePanel.add(pSiteBox);

     // Add the reverse animation toggle checkbox
     tlc.gridy = 1;
     ToggleLayout.setConstraints(pReverseLoopBox,tlc);
     TogglePanel.add(pReverseLoopBox);

     // Add the flicker images toggle checkbox
     tlc.gridy = 2;
     ToggleLayout.setConstraints(pFlickerBox,tlc);
     TogglePanel.add(pFlickerBox);

     return;

  }

//
// *****************************************************************
//

	public void buildDelayPanel() {

     DelayPanel = new Panel();
     FFRewindPanel = new Panel();
     ForwardBackPanel = new Panel();

     // Prepare the panel's layout
     DelayLayout = new GridBagLayout ();
     dlc = new GridBagConstraints();
     dlc.fill = GridBagConstraints.HORIZONTAL;
     dlc.insets = new Insets(0,0,4,0);
     dlc.weightx = 0.0;
     dlc.weighty = 0.0;
     dlc.anchor = GridBagConstraints.NORTHWEST;

     // Assign the layout to the panel
     DelayPanel.setLayout(DelayLayout);

     // Add the Delay value label/text.
     dlc.gridx = 0;
     dlc.gridy = 0;
     dlc.gridwidth = 2;
     dlc.gridheight = 1;
     DelayLayout.setConstraints(pDelayValue,dlc);
     DelayPanel.add(pDelayValue);

     // Add the delay time scroll bar.
     dlc.gridx = 0;
     dlc.gridy = 1;
     dlc.gridwidth = 2;
     dlc.gridheight = 1;
     DelayLayout.setConstraints(pDelayBar,dlc);
     DelayPanel.add(pDelayBar);

     // Add the Play/Pause toggle button
     dlc.gridx = 0;
     dlc.gridy = 2;
     dlc.gridwidth = 2;
     dlc.gridheight = 1;
     DelayLayout.setConstraints(pPauseBtn,dlc);
  	DelayPanel.add(pPauseBtn);

     // Add the rewind and fast-forward buttons into their own
     // panel. This panel is needed in order to get the two
     // buttons to expand to the full width of the delay panel's
     // width.
     FFRewindPanel.setLayout(new GridLayout(1,2,0,0));
     FFRewindPanel.add(pRewindBtn); // Default FlowLayout is used here
     FFRewindPanel.add(pFastForwardBtn);

     // Add the rewind and fast-forward panel into the delay panel.
     dlc.gridx = 0;
     dlc.gridy = 3;
     dlc.gridwidth = 2;
     DelayLayout.setConstraints(FFRewindPanel,dlc);
  	DelayPanel.add(FFRewindPanel);

     // Add the back and forward buttons into their own panel.
     ForwardBackPanel.setLayout(new GridLayout(1,2,0,0));
     ForwardBackPanel.add(pBackBtn);
     ForwardBackPanel.add(pForwardBtn);

     // Add the forward and back panel into the delay panel.
     dlc.gridx = 0;
     dlc.gridy = 4;
     dlc.gridwidth = 2;
     DelayLayout.setConstraints(ForwardBackPanel,dlc);
  	DelayPanel.add(ForwardBackPanel);

    return;

  }

//
// *****************************************************************
//

	public Frame getFrame(Container c) {  // Here, frame is a sub-class of a
                                        // Window object--
                                        // not an animation frame.

		if (c instanceof Frame || c == null)
			return((Frame)c);
		else
			return(getFrame(c.getParent()));
	   }


//
// *****************************************************************
//

	public void adjustmentValueChanged(AdjustmentEvent evt) {

     // Scrollbar has min = 1 and max = 40 values
     // DelayInt and image loop delay have min = 100 and max = 4000
     DelayInt = evt.getValue();

     DelayInt = DelayInt * 100;
     pDelayValue.setText("Delay:  "+String.valueOf(DelayInt)+" ms");
     // Pass DelayInt to the image loop.
     pImageLoop.updateFrameDelay(DelayInt);

		return;
	}

//
// *****************************************************************
//

	public void startNewLoop(String SelectionPrefix) {

 		pLegendImg = getImage(getCodeBase(),"images/" +
                    SelectionPrefix + "lgndh.gif");
     LegendCanvas.initialize(pLegendImg);

		repaint();  // Repaint before updating loop for quick user feedback.

     stop();
		pImageLoop.readNewFrames(SelectionPrefix);
     LegendCanvas.repaint();
     start();

		return;
	}

//
// *****************************************************************
//

	public void actionPerformed(ActionEvent evt) {

  if (evt.getActionCommand() instanceof String) {

     String cmd = (String) evt.getActionCommand();

		if (cmd == "<") {
//			if (StopLoop) {
                // Decrement frame index.
                pImageLoop.changeSlide(-1);
//        }
        return;
		}
		else if (cmd == ">") {
//			if (StopLoop) {
                // Increment frame index,
                // no adjustment necessary since modulus is used.
                pImageLoop.changeSlide(+1);
//        }
        return;
		}
		else if (cmd == "|<") {
        pImageLoop.rewind();
        return;
		}
		else if (cmd == ">|") {
        pImageLoop.fastforward();
        return;
		}
		else if (cmd == "Pause" ||
              cmd == "Play") {

			if (! StopLoop) {
           // Change to Slide/stop mode
				StopLoop = true;
           pImageLoop.setStopLoopFlag(StopLoop);
           pImageLoop.updateFrameDelay(100);

				pPauseBtn.setLabel("Play");

           setBackForwardEnabled();

           return;
			}
			else {
           // Change to Animation mode
				StopLoop = false;
           pImageLoop.setStopLoopFlag(StopLoop);
           pImageLoop.updateFrameDelay(DelayInt);

				pPauseBtn.setLabel("Pause");

           setBackForwardEnabled();

           return;
			}
		}
  }

  return;

  }
//
// *****************************************************************
//

	public void itemStateChanged(ItemEvent evt) {

     String itemLabel = (String) evt.getItem();
     String evtString = evt.toString();
     int choiceIndex = evtString.indexOf("choice");

		if (itemLabel == "Latin") {

        // If already in Latin mode, do nothing.
        if (CurrentNomenclatureInt == LatinConstant) return;

        // Get list index before rebuilding the list.
        int PreviousIndex = pPollenNameCombo.getSelectedIndex();

        // Rebuild the pollen list
        SelectionList.getPollenNames(LatinConstant, PollenName);

        // Rebuild the selection choices.
        pPollenNameCombo.removeAll();
        for (int i=0; i<ListLength; i++)
  		   pPollenNameCombo.addItem(PollenName[i]);

        // Rename the current pollen name.
        int altLink;
        altLink = SelectionList.getAlternateLink(CommonConstant, PreviousIndex);
        pPollenNameCombo.select(altLink);
 			CurrentNomenclatureInt = LatinConstant;
        return;
		}
		else if (itemLabel == "Common") {

        // If already in Common mode, do nothing.
        if (CurrentNomenclatureInt == CommonConstant) return;

        // Get list index before rebuilding the list.
        int PreviousIndex = pPollenNameCombo.getSelectedIndex();

        // Rebuild the pollen list
        SelectionList.getPollenNames(CommonConstant, PollenName);

        // Rebuild the selection choices.
        pPollenNameCombo.removeAll();
        for (int i=0; i<ListLength; i++)
  		   pPollenNameCombo.addItem(PollenName[i]);

        // Rename the current pollen name.
        int altLink;
        altLink = SelectionList.getAlternateLink(LatinConstant, PreviousIndex);
        pPollenNameCombo.select(altLink);
 			CurrentNomenclatureInt = CommonConstant;
        return;
		}

		else if (itemLabel == "Display Sites") {
			DisplaySites = !DisplaySites;
			pImageLoop.setSiteOverlaysFlag(DisplaySites);
        return;
		}

		else if (itemLabel == "Reverse Animation") {
			ReverseLoop = !ReverseLoop;  // Toggle the boolean flag
			pImageLoop.setReverseLoopFlag(ReverseLoop);
        return;
		}

		else if (itemLabel == "Compare Images") {
			FlickerImages = !FlickerImages;  // Toggle the boolean flag
			pImageLoop.setFlickerImagesFlag(FlickerImages);
        setBackForwardEnabled();
        return;
		}

		else if (choiceIndex >=0) {

        CurrentSelectionIndex = pPollenNameCombo.getSelectedIndex();
        SelectionPrefix = SelectionList.getFilePrefix(CurrentNomenclatureInt,
                                        CurrentSelectionIndex);
        startNewLoop(SelectionPrefix);

        return;

     }

		return;
	}

//
// *****************************************************************
//

	public void setBackForwardEnabled() {

     if (StopLoop || FlickerImages) {

        // Enable back and forward buttons
        pForwardBtn.setEnabled(true);
        pBackBtn.setEnabled(true);

     }
     else {

        // Disable back and forward buttons
        pForwardBtn.setEnabled(false);
        pBackBtn.setEnabled(false);

     }

		return;
	}

//
// *****************************************************************
//

}


