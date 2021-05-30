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

// 초대 요청 리스트뷰
public class RequestInvitation extends AppCompatActivity {

    private ListView mListView;
    String UserName, UserInvite;

    // MainActivity에서 ID 문자열로 사용하는 lg_ID 가져온다.
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();

    // 다른 액티비티에서 접근 가능
    public static Context context_RequestInvitation;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_invitation);

        // 다른 액티비티에서 접근 가능
        context_RequestInvitation = this;

        // 위젯과 멤버변수 참조 획득
        mListView = (ListView) findViewById(R.id.invite_list);

        // 아이템 추가 및 어댑터 등록
        dataSetting();
    }

    private void dataSetting() {
        // UserDB의 User List에 접근하여 해당 유저의 ID를 받아온다.
        UserDB.child("User List").child(getUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // UserInvite 변수에 "Invite"내용을 받아온다.
                UserInvite = snapshot.child("Invite").getValue().toString();
                // UserName 변수에 "Leader_ID"내용을 받아온다.
                UserName = snapshot.child("Leader_ID").getValue().toString();

                // 초대받은 사람이라면
                if (UserInvite.equals("invited")) {

                    // 리스트 출력
                    RequestAdapter mMyAdapter = new RequestAdapter();
                    mMyAdapter.addItem(UserName); // 리스트의 내용은 초대한 사람의 이름(리더의 이름)
                    mListView.setAdapter(mMyAdapter); // 리스트뷰에 어댑터 설정

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}