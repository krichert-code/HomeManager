package com.homemanager.Task.Energy;

public class EnergyObject {
    private String current;
    private String today;
    private String total;
    private boolean valid;

    public void setCurrentPower(String power){
        this.current = power;
    }

    public void setTodayPower(String power){
        this.today = power;
    }

    public void setTotalPower(String power){
        this.total = power;
    }


    public String getCurrentPower() { return this.current; }

    public String getTodayPower() { return this.today; }

    public String getTotalPower(){
        return this.total;
    }


    public boolean isValid() {return this.valid; }

    public void setValid(boolean valid) { this.valid = valid; }
}



