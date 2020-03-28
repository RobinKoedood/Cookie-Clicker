package Automatic;

import java.awt.image.BufferedImage;

public class Factory implements Automatic {

    private int updateAmount;
    private int amountOfFactories;
    private int first10 = 0;

    @Override
    public String getName() {
        return "Factory";
    }

    @Override
    public int getCost() {
        if (first10 < 21){
            first10++;
            return 2500;
        } else {
            double i = 2500 * (amountOfFactories/10.0);
            return (int) i;
        }
    }

    @Override
    public double getMultiplication() {
        return 6;
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 60;
        }
        return 0;
    }

    public void addFactory(){
        amountOfFactories++;
    }

    public int getAmountOfFactories() {
        return amountOfFactories;
    }
}
