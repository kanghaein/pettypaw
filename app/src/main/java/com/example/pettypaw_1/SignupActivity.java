package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// id중복확인, 입력창 비었을 때의 이벤트,... 필요

public class SignupActivity extends AppCompatActivity {

    // 레이아웃들의 id값들 선언
    EditText sign_ID, sign_PW, sign_PW_check;
    Button btn_back, btn_complete;

    // DatabaseReference 가져오기
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 레이아웃 id 값들을 불러와 변수에 저장
        sign_ID = findViewById(R.id.sign_ID);
        sign_PW = findViewById(R.id.sign_PW);
        sign_PW_check = findViewById(R.id.sign_PW_check);
        btn_complete = findViewById(R.id.btn_complete);
        btn_back = findViewById(R.id.btn_back);

        // 파이어베이스 연동
        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        // 비밀번호 일치 확인 검사 작업
        sign_PW_check.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            public void onTextChanged(CharSequence s, int start, int before, int count){
                String getUserPw = sign_PW.getText().toString();
                String getUserPwCheck = sign_PW_check.getText().toString();
            }

            public  void afterTextChanged(Editable s){

            }
        });

        // 가입완료 버튼
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 버튼을 누르면 아래 작업 실행
                String getUserId = sign_ID.getText().toString();
                String getUserPw = sign_PW.getText().toString();
                String getUserPwCheck = sign_PW_check.getText().toString();

                // 비밀번호와 비밀번호확인이 일치하지 않는다면
                if(!getUserPw.equals(getUserPwCheck)){
                    Toast.makeText(com.example.pettypaw_1.SignupActivity.this, "PW가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                // 비밀번호와 비밀번호 확인이 일치한다면
                else if(getUserPw.equals(getUserPwCheck)) {
                    writeNewData("1", getUserId, getUserPw); // DB에 데이터 쓰기
                }
            }
        });

        // 취소 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private void writeNewData(String userId, String ID, String PW) {
        // User 파일에 데이터 전달
        User user = new User(ID, PW);

        mDatabase.child("users").child(userId).setValue(user)
                // 완료 리스너
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(com.example.pettypaw_1.SignupActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(com.example.pettypaw_1.SignupActivity.this, "ID 혹은 PW를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void readUser(){
        // User 파일에 전달받은 데이터를 기반으로 DB에 데이터 쓰기
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(com.example.pettypaw_1.SignupActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}

