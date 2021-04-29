package com.example.pettypaw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MemberSetting extends AppCompatActivity {

    Button btn_alram, btn_pet_register_edit, btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_setting);

        // 알림설정
        btn_alram = findViewById(R.id.btn_alram_mem);
        btn_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Preference.class);
                startActivity(intent);
            }
        });

        // 반려동물 등록/편집
        btn_pet_register_edit = findViewById(R.id.btn_pet_register_edit_mem);
        btn_pet_register_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), pet_list.class);
                startActivity(intent);

            }
        });

        // 이용중인 캘린더
        btn_calender = findViewById(R.id.btn_calender_mem);
        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentCalendar.class);
                startActivity(intent);
            }
        });
    }
}