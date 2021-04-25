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



public class welcome extends AppCompatActivity {

    Button btn_leader, btn_member;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        btn_leader = findViewById(R.id.btn_leader);
        btn_member = findViewById(R.id.btn_member);

        String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

        // 파이어베이스 연동
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // User.java 를 통해 데이터베이스 접근
        final DatabaseReference UserDB = mDatabase.getReference("User");


        // 혼자 사용할 예정이거나 누군가를 초대할 경우(그룹리더)
        btn_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 리더버튼을 누르면 User -> User List 의 자식노드에 리더로 등록된다
                        User_list user_list = new User_list("Leader", getUserID);
                        UserDB.child("User List").child(getUserID).setValue(user_list);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(getApplicationContext(), enrollment.class);
                startActivity(intent);
            }
        });


        // 캘린더를 사용하고 있는 사람에게 초대 받은 경우
        btn_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RequestInvitation.class);
                startActivity(intent);

            }
        });


    }

}