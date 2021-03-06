package Automatic;

import java.util.ArrayList;

public class AutoCursor implements Automatic {
    private int updateAmount = 0;
    private int amountOfAutoCursors;
    private int first10 = 0;

    public AutoCursor() {
    }

    @Override
    public int getCost() {
        if (amountOfAutoCursors < 10){
            return 25;
        } else {
                double i = 25 * (((amountOfAutoCursors ) / 10.0));
                return (int) i;
        }

    }

    @Override
    public double getMultiplication() {
        return 0.1;
    }

    public int update (){
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String getName() {
        return "Cursor";
    }

    public void addCursor(){
        amountOfAutoCursors++;
    }

    public int getAmountOfAutoCursors(){
        return this.amountOfAutoCursors;
    }

    public void setAmountOfAutoCursors(int amountOfAutoCursors) {
        this.amountOfAutoCursors = amountOfAutoCursors;
    }
}
