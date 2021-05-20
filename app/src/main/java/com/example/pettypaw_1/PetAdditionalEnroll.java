package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class PetAdditionalEnroll extends AppCompatActivity {

    EditText et_name, et_age;
    Button btn_enroll;
    RadioButton rb_man, rb_woman;
    RadioGroup rg_gender;
    String getPetGender; // 애완동물 성별 값을 전달받을 변수
    Spinner spinner;
    String LeaderID;
    String Color;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    final DatabaseReference UserDB = mDatabase.getReference("User");

    // MainActivity 에서 가져온 lg_ID 라는 변수 이용 => 로그인한 ID를 부모로 펫정보 입력
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment1);

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
                String getPetName = et_name.getText().toString();
                String getPetAge = et_age.getText().toString();
                String getColor = spinner.getSelectedItem().toString();

                // (User -> User List -> 로그인한 ID -> Leader_ID) 의 값은 같은 그룹에 속한 사람들이라면
                // 모두 리더의 ID로 통일되어 같으므로 이것을 그룹내 공유를 위한 키값으로 사용.
                // 그 키값이 아래의 LeaderID 라는 변수
                UserDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();

                        // 등록구현
                        petDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // 반려동물의 이름과 나이가 비어있다면
                                if(getPetName.equals("") || getPetAge.equals("")) {
                                    Toast.makeText(PetAdditionalEnroll.this, "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // 자신이 속한 그룹의 "Pet Information" 이하 자식들에 대해 모두 한번씩 반복한다. (반려동물 색상 중복체크)
                                    for(DataSnapshot ds : snapshot.child(LeaderID).child("Pet Information").getChildren()) {

                                        // "Pet Information" 이하 자식들 중에서도 "Color" 라는 자식이 가지는 값을 문자열로 받아와 Color 라는 변수에 저장
                                        Color = ds.child("Color").getValue().toString();

                                        // 만약 입력한 색과 받아온 색의 값이 같다면
                                        if(getColor.equals(Color)){
                                            Toast.makeText(PetAdditionalEnroll.this, "이미 사용중인 색상입니다", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }

                                    // break 를 걸었으므로 반복문이 끝나고 색깔이 중복되었으면 변수 Color는 입력한 색과 동일한 값이다
                                    // 색이 중복되지 않았으면 변수 Color는 입력한 색과 다른 값이므로 이것을 조건으로 추가등록 구현
                                    if(!getColor.equals(Color)) {

                                        User_pet pet = new User_pet(getPetName, getPetAge, getPetGender, getColor);

                                        // 애완동물 데이터 입력, LeaderID 이용
                                        petDB.child(LeaderID).child("Pet Information").child(getPetName).setValue(pet);

                                        // 설정 => 반려동물 등록/편집 에서의 리스트 출력을 위한 애완동물 리스트 데이터 입력
                                        petDB.child(LeaderID).child("Pet List").child(getPetName).setValue(getPetName);

                                        Toast.makeText(PetAdditionalEnroll.this, "등록 완료", Toast.LENGTH_SHORT).show();
                                        finish();

                                        Intent intent = new Intent(getApplicationContext(), pet_list.class);
                                        startActivity(intent);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}