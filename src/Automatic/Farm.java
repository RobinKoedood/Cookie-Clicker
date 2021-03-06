package Automatic;

public class Farm implements Automatic {

    private int updateAmount;
    private int amountOfFarms;
    private int first10 = 0;

    public Farm() {
    }

    @Override
    public String getName() {
        return "Farm";
    }

    @Override
    public int getCost() {
        if (amountOfFarms < 10){
            return 250;
        } else {
            double i = 250 * (((amountOfFarms ) / 10.0));
            return (int) i;
        }

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

    public void setAmountOfFarms(int amountOfFarms) {
        this.amountOfFarms = amountOfFarms;
    }
}
