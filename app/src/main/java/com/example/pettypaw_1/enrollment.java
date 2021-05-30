package com.example.pettypaw_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 반려동물 등록하기 시작
public class enrollment extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment);

        // 반려동물 등록하기 버튼
        Button btn_member = findViewById(R.id.btn_member);

        // 반려동물 등록하기 버튼 클릭 이벤트
        btn_member.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // enrollment1.java 의 액티비티 시작
                Intent intent = new Intent(getApplicationContext(),enrollment1.class);
                startActivity(intent);
            }
        });

    }
}