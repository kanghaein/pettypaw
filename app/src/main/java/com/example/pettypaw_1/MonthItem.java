package com.example.pettypaw_1;

import android.content.Context;

import java.time.Month;

public class MonthItem {

    private int dayValue;


    MonthItem(int dayValue){
        this.dayValue = dayValue;
    }

    public int getDay(){
        return dayValue;
    }

}