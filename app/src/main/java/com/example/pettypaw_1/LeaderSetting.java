package com.example.pettypaw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LeaderSetting extends AppCompatActivity {

    Button btn_alram, btn_pet_register_edit, btn_grp_management, btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_setting);

        // 알림설정
        btn_alram = findViewById(R.id.btn_alram);
        btn_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Preference.class);
                startActivity(intent);
            }
        });

        // 반려동물 등록/편집
        btn_pet_register_edit = findViewById(R.id.btn_pet_register_edit);
        btn_pet_register_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), pet_list.class);
                startActivity(intent);

            }
        });

        // 그룹관리
        btn_grp_management = findViewById(R.id.btn_grp_management);
        btn_grp_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GroupManagement.class);
                startActivity(intent);
            }
        });

        // 이용중인 캘린더
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