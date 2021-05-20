package com.example.pettypaw_1;

public class User_event {
    //public String Pet_name;
    public String Detail;
    public String Date;
    public Boolean walk;
    public Boolean feed;

    public User_event(){

    }

    public User_event(String Detail, String Date, Boolean walk, Boolean feed){
        //this.Pet_name = Pet_name;
        this.Detail = Detail;
        this.Date = Date;
        this.walk = walk;
        this.feed = feed;
    }

    public boolean isWalk(){
        return walk;
    }

    public boolean isFeed(){
        return feed;
    }

}