package com.example.pushtest;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {


    //태그를 찍는 이유 : 로그를 사용할 때 편리함,로그를 어디서 찍더라도 tag를 불러오면 되니까 편리함
    private static final String TAG = "MyFirebaseIIDService";



    //token 핸드폰 기기에서 받아올 수 있는 난수의 값,토큰을 가지고 있으면 푸쉬알림을 받을 수 있음,id같은 개념
    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG,token);

        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(String token){
        //앱서버로 토큰을 보낼떄 활용하는 부분

    }
}
