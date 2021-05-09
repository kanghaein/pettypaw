package com.example.backbuttonexample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;//현재시간에서- 백버튼 누른 시간

        //gaptime이 2초 이하일때, 뒤로가기 버튼을 두번 누른거니까 뒤로가기가 super에 의해 활성화된다.
        if(0 <= gapTime && 2000 >= gapTime){
            super.onBackPressed(); //이거에 의해 뒤로가기 기능 활성화
        }
        else { //한번 눌렀을때 조건에 의해 이 루프를 타서 이 메시지를 출력하게 됨
            backBtnTime = curTime;
            Toast.makeText(this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }
}