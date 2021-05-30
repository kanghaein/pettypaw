package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 반려동물을 등록하기 위한 액티비티
public class enrollment1 extends AppCompatActivity {

    EditText et_name, et_age; // 이름 칸, 나이 칸
    Button btn_enroll; // 등록 버튼
    RadioButton rb_man, rb_woman; // 반려동물 성별 라디오 버튼
    RadioGroup rg_gender; // 반려동물 성별 라디오 그룹
    String getPetGender; // 성별을 String으로 저장하기 위한 변수
    Spinner spinner; // 반려동물 색상 스피너

    // MainActivity 에서 로그인 할 때 입력한 ID 값, 즉 자신의 ID
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");

    public static Context context_enrollment1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment1);

        context_enrollment1 = this;

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        btn_enroll = findViewById(R.id.btn_enroll);
        rg_gender = (RadioGroup)findViewById(R.id.rg_gender);
        rb_man = (RadioButton)findViewById(R.id.rb_man);
        rb_woman = (RadioButton)findViewById(R.id.rb_woman);
        spinner = (Spinner)findViewById(R.id.spinner);


        // 라디오버튼 이벤트
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rb_man){ // 남자 버튼을 누르면 getPetGender 에 "남자" 반환
                    getPetGender = rb_man.getText().toString();
                }
                else if(i == R.id.rb_woman){ // 여자 버튼을 누르면 getPetGender 에 "여자" 반환
                    getPetGender = rb_woman.getText().toString();
                }
            }
        });


        // 등록버튼 이벤트
        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이름, 나이, 스피너의 값을 String 으로 받아온다
                String getPetName = et_name.getText().toString();
                String getPetAge = et_age.getText().toString();
                String getColor = spinner.getSelectedItem().toString();

                petDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 이름칸과 나이칸이 비어있다면
                        if(getPetName.equals("") || getPetAge.equals("")) {
                            Toast.makeText(enrollment1.this, "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // User_pet.java 를 통해 이름, 나이, 성별, 색상을 한번에 입력
                            User_pet pet = new User_pet(getPetName, getPetAge, getPetGender, getColor);

                            // DB 상 (User_pet -> 자신의 ID -> Pet Information -> 반려동물 이름)에 해당 반려동물 정보 입력
                            petDB.child(getUserID).child("Pet Information").child(getPetName).setValue(pet);

                            // 설정 => 반려동물 등록/편집 에서의 리스트 출력을 위한 Pet List 데이터 입력
                            petDB.child(getUserID).child("Pet List").child(getPetName).setValue(getPetName);

                            // 첫 반려동물 등록을 완료하면 현재 로그인한 유저는 User -> User List 의 자식노드에 리더로 등록된다
                            UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User_list user_list = new User_list("Leader", getUserID);
                                    UserDB.child("User List").child(getUserID).setValue(user_list);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Toast.makeText(enrollment1.this, "등록 완료", Toast.LENGTH_SHORT).show();

                            // 현재 액티비티를 종료하고 ViewCalendar.java 의 액티비티를 시작한다
                            finish();
                            Intent intent = new Intent(getApplicationContext(), ViewCalendar.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}