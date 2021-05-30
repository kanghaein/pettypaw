package com.example.pettypaw_1;

// 등록된 사용자 리스트를 담기 위한 User_list 클래스
// Invite를 통해 Leader와 Member 여부를 구분할 수 있다.
// Leader_ID를 통해 해당 그룹의 그룹장이 알 수 있다.
public class User_list {

    public String Invite;
    public String Leader_ID;

    public User_list() {

    }

    public User_list(String Invite, String Leader_ID) {
        this.Invite = Invite;
        this.Leader_ID = Leader_ID;
    }


}
