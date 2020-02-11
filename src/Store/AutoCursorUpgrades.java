package Store;

import Automatic.AutoCursor;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AutoCursorUpgrades implements Upgrade {



    @Override
    public int cost() {
        return 100;
    }

    @Override
    public BufferedImage icons() {
        try {
            BufferedImage total = ImageIO.read(getClass().getResource("/icons.png"));
            BufferedImage cursor = total.getSubimage(0,0,48,48);
            return cursor;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void usage() {
        AutoCursor autoCursor = new AutoCursor();

    }

    @Override
    public String info() {
        return "Auto Cursors are" + "<b> bold </b>" +  "as usefull";
    }
}
