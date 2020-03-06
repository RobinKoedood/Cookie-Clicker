package Automatic;

import java.awt.image.BufferedImage;

public class Mine implements Automatic {

    private int updateAmount;
    private int amountOfMines;
    private BufferedImage image = null;


    public Mine() {

    }

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public int getCost() {
        return 1000;
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
