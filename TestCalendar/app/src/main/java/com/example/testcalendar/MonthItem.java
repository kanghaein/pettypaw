package com.example.testcalendar;

public class MonthItem {

    private int dayValue;

    MonthItem(int dayValue){
        this.dayValue = dayValue;
    }

    public int getDay(){
        return dayValue;
    }

}