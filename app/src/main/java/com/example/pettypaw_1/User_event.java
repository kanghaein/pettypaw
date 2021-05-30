package com.example.pettypaw_1;

// 사용자의 일정 정보를 담기 위한 User_event 클래스
public class User_event {
    public String Detail;
    public String Date;
    public Boolean walk;
    public Boolean feed;

    public User_event() {

    }

    public User_event(String Detail, String Date, Boolean walk, Boolean feed) {
        this.Detail = Detail;
        this.Date = Date;
        this.walk = walk;
        this.feed = feed;
    }

    public boolean isWalk() {
        return walk;
    }

    public boolean isFeed() {
        return feed;
    }

}