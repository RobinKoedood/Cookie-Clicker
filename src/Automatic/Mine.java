package Automatic;

import java.awt.image.BufferedImage;

public class Mine implements Automatic {

    private int updateAmount;
    private int amountOfMines;
    private BufferedImage image = null;
    private boolean first = true;


    public Mine() {

    }

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public double getCost() {
        if (first){
            first = false;
            return 1000;
        } else {
            double i = 1000 * (amountOfMines/10.0);
            Math.round(i);
            return i;
        }

    }

    @Override
    public double getMultiplication() {
        return 2.5;
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 25;
        }
        return 0;
    }

    public void addMine(){
        amountOfMines++;
    }

    public int getAmountOfMines() {
        return amountOfMines;
    }
}
