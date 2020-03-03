package Automatic;

import javafx.scene.control.Button;

public class Grandma implements Automatic {
    private int updateAmount = 0;
    private int amountOfGrandmas;
    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public double getMultiplication() {
        return 0.5;
    }

    @Override
    public String getname() {
        return "Grandma";
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 5;
        } else {
            return 0;
        }
    }

    private Button buttonGrandma (){
        Button button = new Button("Grandma");
        return button;
    }

    public void addGrandma(){
        amountOfGrandmas++;
    }

    public int getAmountOfGrandmas(){
        return this.amountOfGrandmas;
    }
}
