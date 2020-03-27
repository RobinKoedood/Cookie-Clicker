package Automatic;

import java.util.ArrayList;

public class AutoCursor implements Automatic {
    private int updateAmount = 0;
    private ArrayList<AutoCursor> autoCursors = new ArrayList<>();
    private int amountOfAutoCursors;
    int first10 = 0;

    public AutoCursor() {
    }

    @Override
    public int getCost() {
        if (first10 < 40){
            first10++;
            return 25;
        } else {
            double i = 25 * (amountOfAutoCursors/10.0);
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
}
