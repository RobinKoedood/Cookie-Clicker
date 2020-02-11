import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cookie {

    private Color color;
    private Ellipse2D ellipse2D;
    private BufferedImage fullImg = null;


    public Cookie(Color color, Ellipse2D ellipse2D) {
        this.color = color;
        this.ellipse2D = ellipse2D;
        try {
            this.fullImg = ImageIO.read(new File("resources/cookie.png"));
        } catch (IOException e) {
        }
    }

    public BufferedImage getImageIdle(){
        return this.fullImg.getSubimage(0,0,128,128);

    }

    public void setEllipse2D(Ellipse2D ellipse2D){
        this.ellipse2D = ellipse2D;
    }

    public BufferedImage getImageHover(){
        return this.fullImg.getSubimage(128,0,128,128);
    }

    public BufferedImage getImageHeld() {
        return this.fullImg.getSubimage(256, 0, 128, 128);
    }


    public Color getColor() {
        return color;
    }

    public Ellipse2D getEllipse2D() {
        return ellipse2D;
    }
}
