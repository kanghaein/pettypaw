package com.example.pettypaw_1;

public class User_event {
    //public String Pet_name;
    public String Detail;
    public String Date;
    public Boolean feed_checked;
    public Boolean walk_checked;

    public User_event(){

    }

    public User_event(String Detail, String Date,Boolean feed_checked,Boolean walk_checked){
        //this.Pet_name = Pet_name;
        this.Detail = Detail;
        this.Date = Date;
        this.feed_checked = feed_checked;
        this.walk_checked = walk_checked;
    }

    public String getDate(){
        return Date;
    }

}
