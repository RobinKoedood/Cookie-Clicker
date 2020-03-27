package Automatic;

import java.awt.image.BufferedImage;

public class Bank implements Automatic {

    private int updateAmount;
    private int amountOfBanks;
    private BufferedImage image = null;
    private int first10 = 0;

    @Override
    public String getName() {
        return "Bank";
    }

    @Override
    public int getCost() {
        if (first10 < 21){
            first10++;
            return 5000;
        } else {
            double i = 5000 * (amountOfBanks/10.0);
            return (int) i;
        }
    }

    @Override
    public double getMultiplication() {
        return 12;
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 120;
        }
        return 0;
    }

    public void addBank(){
        amountOfBanks++;
    }

    public int getAmountOfBanks() {
        return amountOfBanks;
    }

}
