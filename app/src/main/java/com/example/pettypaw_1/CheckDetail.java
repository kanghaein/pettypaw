package com.example.pettypaw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckDetail extends AppCompatActivity {

    Button btn_pre, btn_as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);
        
        //뒤로가기 버튼
        btn_pre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        //일정추가 버튼
        btn_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
                startActivity(intent);
            }
        });
    }
}