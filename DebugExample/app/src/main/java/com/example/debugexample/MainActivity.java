package com.example.debugexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String hong = "홍드로이드";

        Log.e("MainActivity : ", hong); //log.e나 log.d로 출력하는게 디버그할때 많이 사용


        // 핸드폰 갯수
        int a =10;
        // 로그를 찍음
        Log.e("MainActivity : ", String.valueOf(a));
         

    }
}