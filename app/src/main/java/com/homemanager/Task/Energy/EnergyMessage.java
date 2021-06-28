package com.homemanager.Task.Energy;

import com.homemanager.Task.Temperature.TemperatureObject;

public interface EnergyMessage {
    public void displayEnergy(final EnergyObject energy);
    public void displayErrorMessage();
}
