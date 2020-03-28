package Automatic;

import java.awt.image.BufferedImage;

public class Lab implements Automatic {

    private int updateAmount;
    private int amountOfLabs;
    private int first10 = 0;

    @Override
    public String getName() {
        return "Lab";
    }

    @Override
    public int getCost() {
        if (first10 < 21){
            first10++;
            return 10000;
        } else {
            double i = 10000 * (amountOfLabs/10.0);
            return (int)i;
        }
    }

    @Override
    public double getMultiplication() {
        return 25;
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 250;
        }
        return 0;
    }

    public void addLab(){
        amountOfLabs++;
    }

    public int getAmountOfLabs() {
        return amountOfLabs;
    }

}
