import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Kruimel {

    private double xPos;
    private double yPos;
    private BufferedImage image;
    private double dirX, dirY;


    public Kruimel (double xStart, double yStart){
        this.xPos = xStart;
        this.yPos = yStart;

        dirX = (Math.random() * 20) - 10;
        dirY = (Math.random() * 20) - 10;

    }

    public void update (double deltaTime){
        dirY += 10 * deltaTime;
        xPos += dirX;
        yPos += dirY;
    }

    public void draw (FXGraphics2D graphics2D){
//        Paint paint = graphics2D.getPaint();
//        Color color = graphics2D.getColor();
        graphics2D.setColor(new Color(150,108,45));
        graphics2D.fill(new Ellipse2D.Double(xPos, yPos, 10, 10));
//        graphics2D.setPaint(paint);
//        graphics2D.setColor(color);
    }
}
