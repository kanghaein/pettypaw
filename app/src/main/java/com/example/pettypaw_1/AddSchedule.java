package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// 상세일정을 추가하기 위한 액티비티
public class AddSchedule extends AppCompatActivity {

    private Spinner spinner; // 스피너 버튼을 위한 변수 선언

    EditText et_ds; //상세일정 입력
    Button et_button; // 등록완료 버튼
    String LeaderID; // 그룹장의 ID에 접근하기 위한 변수
    // MainActivity 에서 로그인 할 때 입력한 ID 값, 즉 자신의 ID
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");
    // User.java를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // 변수의 값을 레이아웃 id로 입력해 레이아웃과 연결
        spinner = findViewById(R.id.Spinner);
        et_ds = findViewById(R.id.et_ds);
        et_button = findViewById(R.id.et_button);

        User_event event = new User_event(); //날짜, 상세일정 저장하는 User_event 객체
        User_pet pet = new User_pet(); //스피너에서 펫 이름 받아올 User_pet 객체

        // CheckDetail.java 로부터 전달받은 클릭한 날짜 값
        Intent intent = getIntent();
        String Join = intent.getStringExtra("click_day");


        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // DB 상의 (User List -> 자신의 ID -> Leader_ID) 에 저장된 값은 얻어와 문자열로 저장한다
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();

                petDB.child(LeaderID).child("Pet List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 반려동물 이름을 담을 ArrayList
                        final List<String> petName = new ArrayList<String>();

                        // DB 상에 담긴 자신의 반려동물 수만큼 반려동물을 받아와서 ArrayList 에 넣는다
                        for (DataSnapshot nameSnapshot : dataSnapshot.getChildren()) {
                            String data = nameSnapshot.getValue().toString();
                            petName.add(data);
                        }

                        // petName ArrayList 에 대한 ArrayAdapter 객체 생성
                        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(AddSchedule.this, android.R.layout.simple_spinner_item, petName);
                        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(nameAdapter); // 스피너에 ArrayList 의 값들을 세트
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


        // 스피너에서 반려동물 선택 시 이벤트
        spinner.setSelection(Adapter.NO_SELECTION, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 반려동물 이름을 터치하면 그 값을 스피너에서 받아온다
                pet.Pet_Name = spinner.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 등록완료버튼 클릭 이벤트
        et_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 상세일정 입력 텍스트칸에 입력된 값을 String 값으로 받아온다
                        String getDetail = et_ds.getText().toString();

                        //상세일정 입력 칸이 비어있다면
                        if (getDetail.equals("")) {
                            Toast.makeText(AddSchedule.this, "상세일정을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 입력한 상세일정 값
                            event.Detail = getDetail;

                            // DB 상의 (User_event -> 그룹장 ID -> 펫 이름 -> 클릭한 날짜 -> 상세일정)에 입력한 상세일정을 넣는다
                            eventDB.child(LeaderID).child(pet.Pet_Name).child(Join).child("Detail").setValue(event.Detail);

                            Toast.makeText(AddSchedule.this, "등록 완료", Toast.LENGTH_SHORT).show();

                            // 현재 액티비티를 종료한 후 ViewCalendar.java 의 액티비티를 시작한다
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