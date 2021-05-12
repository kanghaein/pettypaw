package com.example.pettypaw_1;

public class User_event {
    //public String Pet_name;
    public String Detail;
    public String Date;
<<<<<<< HEAD
    public Boolean walk;
    public Boolean feed;
=======
    public Boolean feed_checked;
    public Boolean walk_checked;
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a

    public User_event(){

    }

<<<<<<< HEAD
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
=======
    public User_event(String Detail, String Date,Boolean feed_checked,Boolean walk_checked){
        //this.Pet_name = Pet_name;
        this.Detail = Detail;
        this.Date = Date;
        this.feed_checked = feed_checked;
        this.walk_checked = walk_checked;
    }

    public String getDate(){
        return Date;
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
    }

}
