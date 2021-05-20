package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSchedule extends AppCompatActivity {

    private Spinner spinner;

    EditText et_ds; //상세일정 입력
    Button et_button;
    String LeaderID;


    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");
    final DatabaseReference UserDB = mDatabase.getReference("User");

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        spinner = findViewById(R.id.Spinner);
        et_ds = findViewById(R.id.et_ds);
        et_button = findViewById(R.id.et_button);

        User_event event = new User_event(); //날짜, 상세일정 저장하는 User_event 객체
        User_pet pet = new User_pet(); //스피너에서 펫 이름 받아올 User_pet 객체


        Intent intent = getIntent();
        String Join = intent.getStringExtra("click_day");
        //ArrayList Id = intent.getParcelableArrayListExtra("id_key");


        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();

                petDB.child(LeaderID).child("Pet List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final List<String> petName = new ArrayList<String>(); //이름 담을 array

                        for (DataSnapshot nameSnapshot: dataSnapshot.getChildren()){ //강아지 담긴 수만큼 받아와서 array에 넣기
                            String data = nameSnapshot.getValue().toString();
                            petName.add(data);
                        }

                        //ArrayAdapter 객체 생성
                        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(AddSchedule.this, android.R.layout.simple_spinner_item, petName);

                        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(nameAdapter); //어댑터에 연결

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



        //선택된 강아지
        spinner.setSelection(Adapter.NO_SELECTION, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pet.Pet_Name = spinner.getItemAtPosition(position).toString(); //강아지 이름을 스피너에서 받아옴
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                eventDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String getDetail = et_ds.getText().toString();

                        //상세일정 입력 칸이 비어있다면
                        if(getDetail.equals("")) {
                            Toast.makeText(AddSchedule.this, "상세일정을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            event.Detail = getDetail;

                            eventDB.child(LeaderID).child(pet.Pet_Name).child(Join).child("Detail").setValue(event.Detail); //User_event하위에 event 등록

                            Toast.makeText(AddSchedule.this, "등록 완료", Toast.LENGTH_SHORT).show();
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