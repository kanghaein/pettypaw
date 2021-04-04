package com.example.pettypaw_1;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class welcome extends AppCompatActivity {

    Button btn_leader, btn_member;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        btn_leader = findViewById(R.id.btn_leader);
        btn_member = findViewById(R.id.btn_member);


        // 파이어베이스 연동
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // User.java 를 통해 데이터베이스 접근
        final DatabaseReference userDB = mDatabase.getReference("User");


        // 혼자 사용할 예정이거나 누군가를 초대할 경우(그룹리더)
        btn_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        // 캘린더를 사용하고 있는 사람에게 초대 받은 경우
        btn_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

}