package com.example.pettypaw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LeaderSetting extends AppCompatActivity {

    Button btn_alram, btn_grp_management, btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_setting);

        btn_alram = findViewById(R.id.btn_alram);


        btn_grp_management = findViewById(R.id.btn_grp_management);
        btn_grp_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GroupManagement.class);
                startActivity(intent);
            }
        });

        btn_calender = findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentCalendar.class);
                startActivity(intent);
            }
        });


    }


}