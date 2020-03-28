package Automatic;

import java.awt.image.BufferedImage;

public class Mine implements Automatic {

    private int updateAmount;
    private int amountOfMines;
    private int first10 = 0;


    public Mine() {

    }

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public int getCost() {
        if (amountOfMines < 10){
            return 1000;
        } else {
            double i = 1000 * (((amountOfMines ) / 10.0));
            return (int) i;
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

    public void setAmountOfMines(int amountOfMines) {
        this.amountOfMines = amountOfMines;
    }
}
