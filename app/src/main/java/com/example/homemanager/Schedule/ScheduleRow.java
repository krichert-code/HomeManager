package com.example.homemanager.Schedule;

public class ScheduleRow {
    private String element_1;
    private String element_2;

    public ScheduleRow(String element_1, String element_2) {
        this.element_1 = element_1;
        this.element_2 = element_2;
    }

    public String getElement_1() {
        return element_1;
    }

    public void setElement_1(String element_1) {
        this.element_1 = element_1;
    }

    public String getElement_2() {
        return element_2;
    }

    public void setElement_2(String element_2) {
        this.element_2 = element_2;
    }
}
