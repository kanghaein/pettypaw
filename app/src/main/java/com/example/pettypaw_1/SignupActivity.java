package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 회원가입
public class SignupActivity extends AppCompatActivity {


    // 레이아웃들의 id값들 선언
    EditText sign_ID, sign_PW, sign_PW_check;
    Button btn_back, btn_complete, btn_dc;


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
        btn_dc = findViewById(R.id.btn_dc);


        // 파이어베이스 연동
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // User.java 를 통해 데이터베이스 접근
        final DatabaseReference userDB = mDatabase.getReference("User");


        // 비밀번호 일치 확인 검사 작업
        sign_PW_check.addTextChangedListener(new TextWatcher() {

            // 입력 전에 호출되는 API
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // EditText에 변화가 있을 때
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getUserPw = sign_PW.getText().toString();
                String getUserPwCheck = sign_PW_check.getText().toString();
            }

            // 입력이 끝났을 때
            public void afterTextChanged(Editable s) {

            }
        });


        // 중복확인 버튼
        btn_dc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserId = sign_ID.getText().toString();

                userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // 아이디 입력창이 비어있다면 알려준다.
                        if (getUserId.equals("")) {
                            Toast.makeText(SignupActivity.this, "ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }

                        // 입력한 아이디가 파이어베이스에 있는 데이터와 겹치면 알려준다.
                        else if (snapshot.child(getUserId).exists()) {

                            Toast.makeText(SignupActivity.this, "이미 사용중인 ID 입니다.", Toast.LENGTH_SHORT).show();
                        }

                        // 입력한 아이디가 파이어베이스에 있는 데이터와 겹치지 않으면 알려준다.
                        else {
                            Toast.makeText(SignupActivity.this, "사용하실 수 있는 ID 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        // 가입완료 버튼 클릭 이벤트 처리
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getUserId = sign_ID.getText().toString();
                String getUserPw = sign_PW.getText().toString();
                String getUserPwCheck = sign_PW_check.getText().toString();

                // 비밀번호와 비밀번호 확인이 일치하지 않는다면 알려준다.
                if (!getUserPw.equals(getUserPwCheck)) {
                    Toast.makeText(SignupActivity.this, "PW가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }

                // 비어있는 입력창이 하나라도 존재한다면 알려준다.
                else if (getUserId.equals("") || getUserPw.equals("") || getUserPwCheck.equals("")) {
                    Toast.makeText(SignupActivity.this, "ID 혹은 PW 를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                // 비밀번호와 비밀번호 확인이 일치한다면 DB에 데이터 저장한다.
                else {

                    // addListenerForSingleValueEvent 를 이용한 데이터 읽기
                    userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // 파이어베이스에 이미 등록된 아이디라면 알려준다.
                            if (snapshot.child(getUserId).exists()) {
                                Toast.makeText(SignupActivity.this, "ID 혹은 PW 를 확인해주세요", Toast.LENGTH_SHORT).show();
                            }

                            // 그렇지 않다면 user객체에 아이디와 비밀번호 저장 후 user_list 초기화
                            else {
                                User user = new User(getUserId, getUserPw);
                                User_list user_list = new User_list("null", "null");

                                // 입력한 정보를 DB의 (User -> 자신의 ID) 노드의 자식으로 저장
                                userDB.child(getUserId).setValue(user);

                                // 또한 (User -> User List -> 자신의 ID) 에 리더정보, 초대여부 를 초기 null 값으로 저장
                                userDB.child("User List").child(getUserId).setValue(user_list);

                                Toast.makeText(SignupActivity.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                finish();

                                //회원가입 완료시 MainActivity 로 이동
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

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
}





