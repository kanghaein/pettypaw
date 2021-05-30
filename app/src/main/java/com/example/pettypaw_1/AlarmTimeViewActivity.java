package com.example.pettypaw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

// 알람기능을 수행하기 위한 액티비티
public class AlarmTimeViewActivity extends AppCompatActivity {

    TextView alarmSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_view);

        alarmSetTime = findViewById(R.id.tv_alarm_set_day_time);

        Intent intent=getIntent();
        int setDay=intent.getIntExtra("setDay",0);
        String day="";
        switch (setDay){
            case 0:
                day="err";
                break;
            case 1:
                day="일요일";
                break;
            case 2:
                day="월요일";
                break;
            case 3:
                day="화요일";
                break;
            case 4:
                day="수요일";
                break;
            case 5:
                day="목요일";
                break;
            case 6:
                day="금요일";
                break;
            case 7:
                day="토요일";
                break;
            default:
                day="err";
                break;
        }

        String setTime = day+"\n"+"알람 세팅 시간"+intent.getStringExtra("setTime");
        alarmSetTime.setText(setTime);
    }
}
