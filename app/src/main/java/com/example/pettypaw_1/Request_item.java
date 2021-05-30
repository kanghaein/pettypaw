package com.example.pettypaw_1;

import android.view.View;

// 그룹장이 초대 전송 시 사용할 Request_item 객체 정의
public class Request_item {

    private String name;


    public String getText() {

        return name;
    }

    public void setText(String name) {

        this.name = name;
    }

}
