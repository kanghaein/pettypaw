package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckDetail extends AppCompatActivity {

    Button btn_pre, btn_as;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);

        Intent intent = getIntent();
        String clickDay = intent.getStringExtra("click_day");


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
                intent.putExtra("click_day", clickDay);
                startActivity(intent);
            }
        });
    }
}