package com.example.pettypaw_1;

// 캘린더의 날짜를 전달하기 위한 클래스
public class MonthItem {

    private int dayValue;


    MonthItem(int dayValue){
        this.dayValue = dayValue;
    }

    public int getDay(){
        return dayValue;
    }

}