package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestInvitation extends AppCompatActivity {

    private ListView mListView;
    String UserName, UserInvite;
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    // 다른 액티비티에서 접근 가능
    public static Context context_RequestInvitation;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_invitation);

        // 다른 액티비티에서 접근 가능
        context_RequestInvitation = this;

        // 위젯과 멤버변수 참조 획득
        mListView = (ListView)findViewById(R.id.invite_list);

        // 아이템 추가 및 어댑터 등록
        dataSetting();
    }

    private void dataSetting(){
        UserDB.child("User List").child(getUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserInvite = snapshot.child("Invite").getValue().toString();
                UserName = snapshot.child("Leader_ID").getValue().toString();

                // 초대받은 사람이라면
                if (UserInvite.equals("invited")) {

                    // 리스트 출력
                    RequestAdapter mMyAdapter = new RequestAdapter();
                    // 리스트의 내용은 초대한 사람의 이름(리더의 이름)
                    mMyAdapter.addItem(UserName);
                    mListView.setAdapter(mMyAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}