package com.example.pettypaw_1;

import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 로그인 구현, 입력창이 비었을 때 이벤트... 필요

public class MainActivity extends AppCompatActivity {

    // 화면 간 전환을 위한 REQUEST_CODE_SIGN 설정
    public static final int REQUEST_CODE_SIGN = 101;

    // 레이아웃의 id 값들 선언
    EditText lg_ID, lg_PW;
    Button btn_sign, btn_login;

    // DatabaseReference 가져오기
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 레이아웃 id 값들을 불러와 변수에 저장
        lg_ID = findViewById(R.id.lg_ID);
        lg_PW = findViewById(R.id.lg_PW);
        btn_sign = findViewById(R.id.btn_sign);
        btn_login = findViewById(R.id.btn_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 버튼 누르면 로그인 창으로 이동(전제조건 파이어베이스에 있는 정보와 맞아야함)
                Intent intent1 = new Intent(getApplicationContext(),ViewCalendar.class);
                startActivity(intent1);
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