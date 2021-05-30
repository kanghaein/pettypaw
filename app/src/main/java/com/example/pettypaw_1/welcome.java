package com.example.pettypaw_1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 회원가입 직후 '그룹장이 되어 (혼자) 사용' 혹은 '그룹원이 되어 초대받은 그룹 내에서 사용' 선택
public class welcome extends AppCompatActivity {

    Button btn_leader, btn_member;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        btn_leader = findViewById(R.id.btn_leader);
        btn_member = findViewById(R.id.btn_member);

        // 혼자 사용할 예정이거나 누군가를 초대할 경우(그룹리더)
        btn_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 강아지 초기 등록 창인 enrollment 창으로 이동
                Intent intent = new Intent(getApplicationContext(), enrollment.class);
                startActivity(intent);
            }
        });

        // 캘린더를 사용하고 있는 사람에게 초대 받은 경우
        btn_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 그룹장이 보낸 초대가 존재하는 RequestInvitation 창으로 이동
                Intent intent = new Intent(getApplicationContext(), RequestInvitation.class);
                startActivity(intent);
            }
        });


    }

}