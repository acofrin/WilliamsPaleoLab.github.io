
import java.awt.*;

public class imgcanvas extends Canvas {

  private Image Img;
  private int ImgHeight = 0, ImgWidth = 0;
  private int CanvasHeight = 0, CanvasWidth = 0;
  private int ImgX;

  public void initialize(Image InputImage) {

     Img = InputImage;
  }

  public void paint(Graphics g)  {

     ImgHeight = Img.getHeight(this);
     ImgWidth = Img.getWidth(this);
     CanvasHeight = this.getSize().height;
     CanvasWidth = this.getSize().width;
		ImgX = (CanvasWidth - ImgWidth) / 2;

     if (ImgHeight > 0 && ImgHeight > 0 &&
          CanvasHeight > 0 && CanvasWidth > 0) {
		     g.setColor(getBackground());
		     g.fillRect(0, 0, CanvasWidth, CanvasHeight);
		     g.setColor(Color.black);
          g.drawImage(Img, ImgX, 0, this);
//        g.drawString("Image Okay",20,50);
//        g.drawString(String.valueOf(CanvasWidth),20,70);
     }
     else {
        g.drawString("No Image Error",20,70);
     }
  }

  public void update (Graphics g) {

     paint(g);

  }

}
