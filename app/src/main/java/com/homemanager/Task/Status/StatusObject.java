package com.homemanager.Task.Status;

import com.homemanager.Task.Action.EventDescription;
import com.homemanager.Task.Action.EventsObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatusObject {
    private TemperatureObject tempObject;
    private EnergyObject energyObject;
    private EventsObject eventsObject;

    public StatusObject()
    {
        tempObject = new TemperatureObject();
        energyObject = new EnergyObject();
        eventsObject = new EventsObject();
    }

    public TemperatureObject getTempObject() {
        return tempObject;
    }

    public void setTempObject(TemperatureObject tempObject) {
        this.tempObject = tempObject;
    }

    public EnergyObject getEnergyObject() {
        return energyObject;
    }

    public void setEnergyObject(EnergyObject energyObject) {
        this.energyObject = energyObject;
    }

    public EventsObject getEventsObject() {
        return eventsObject;
    }

    public void setEventsObject(EventsObject eventsObject) {
        this.eventsObject = eventsObject;
    }

    public void parseStatus(JSONObject content) {
        // parse temperature
        tempObject.parseTemperature(content);
        // parse energy
        energyObject.parseEnergy(content);
        // parse events
        eventsObject.parseEvents(content);
    }

}
