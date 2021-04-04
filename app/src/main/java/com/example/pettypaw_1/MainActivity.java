



package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    // 화면 간 전환을 위한 REQUEST_CODE_SIGN 설정
    public static final int REQUEST_CODE_SIGN = 101;

    // 레이아웃의 id 값들 선언
    EditText lg_ID, lg_PW;
    Button btn_sign, btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 레이아웃 id 값들을 불러와 변수에 저장
        lg_ID = findViewById(R.id.lg_ID);
        lg_PW = findViewById(R.id.lg_PW);
        btn_sign = findViewById(R.id.btn_sign);
        btn_login = findViewById(R.id.btn_login);

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // User.java 를 통해 데이터베이스 접근
        final DatabaseReference userDB = mDatabase.getReference("User");


        // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getUserID = lg_ID.getText().toString();
                String getUserPW = lg_PW.getText().toString();

                // ID 나 PW 가 공백이라면
                if (getUserID.equals("") || getUserPW.equals("")){
                    Toast.makeText(MainActivity.this, "ID 혹은 PW를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    // addValueEventListener 를 이용한 데이터 읽기
                    userDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // DB의 child 에 입력한 ID가 존재한다면
                            if (snapshot.child(getUserID).exists()) {
                                // User.java 를 통해 해당 child 의 ID 와 PW 값을 얻어오고
                                User user = snapshot.child(getUserID).getValue(User.class);
                                // 얻어온 PW와 입력된 PW가 일치한다면
                                if ((user.PW).equals(getUserPW)) {
                                    // welcome 창으로 이동
                                    Intent intent = new Intent(getApplicationContext(), welcome.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "ID 혹은 PW 가 옳지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "존재하지 않는 ID 입니다", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        // 회원가입 버튼
        btn_sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 회원가입 버튼 누르면 회원가입 창으로 이동
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
