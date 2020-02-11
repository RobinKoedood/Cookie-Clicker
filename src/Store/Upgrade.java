package Store;

import java.awt.image.BufferedImage;

public interface Upgrade {

    int cost();
    BufferedImage icons();
    void usage();
    String info();
}
