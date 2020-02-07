package Automatic;

public class Grandma implements Automatic {
    private int updateAmount = 0;
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
}
