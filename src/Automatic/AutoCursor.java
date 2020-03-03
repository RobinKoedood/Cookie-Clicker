package Automatic;

import java.util.ArrayList;

public class AutoCursor implements Automatic {
    private int updateAmount = 0;
    private ArrayList<AutoCursor> autoCursors = new ArrayList<>();
    private int amountOfAutoCursors;
    @Override
    public int getCost() {
        return 25;
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
    public String getname() {
        return "Cursor";
    }

    public void addCursor(){
        amountOfAutoCursors++;
    }

    public int getAmountOfAutoCursors(){
        return this.amountOfAutoCursors;
    }
}
