package Automatic;

public class Farm implements Automatic {

    private int updateAmount;
    private int amountOfFarms;

    @Override
    public String getname() {
        return "Farm";
    }

    @Override
    public int getCost() {
        return 250;
    }

    @Override
    public double getMultiplication() {
        return 1.0;
    }

    @Override
    public int update() {
        updateAmount++;
        if (updateAmount == 10){
            updateAmount = 0;
            return 10;
        }
        return 0;
    }

    public void addFarm(){
        amountOfFarms++;
    }

    public int getAmountOfFarms(){
        return this.amountOfFarms;
    }
}
