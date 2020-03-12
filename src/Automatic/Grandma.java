package Automatic;

import javafx.scene.control.Button;

public class Grandma implements Automatic {
    private int updateAmount = 0;
    private int amountOfGrandmas;

    private boolean first = true;

    public Grandma() {
    }

    @Override
    public double getCost() {
        if (first){
            first = false;
            return 100;
        } else {
            double i = 100 * (amountOfGrandmas/10.0);
            Math.round(i);
            return i;
        }

    }

    @Override
    public double getMultiplication() {
        return 0.5;
    }

    @Override
    public String getName() {
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
        button.setOnAction(event -> {
            addGrandma();

        });
        return button;
    }

    public void addGrandma(){
        amountOfGrandmas++;
    }

    public int getAmountOfGrandmas(){
        return this.amountOfGrandmas;
    }
}
