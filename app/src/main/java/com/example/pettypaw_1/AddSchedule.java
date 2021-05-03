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
import java.util.List;

public class AddSchedule extends AppCompatActivity {

    private Spinner spinner;

    EditText et_ds; //상세일정 입력
    Button et_button;

    String Pname;
    List<Object> Plist = new ArrayList<>(); //Pet List 이름 받아오기 위한 배열

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        spinner = findViewById(R.id.Spinner);
        et_ds = findViewById(R.id.et_ds);
        et_button = findViewById(R.id.et_button);

        Intent intent = getIntent();
        String clickDay = intent.getStringExtra("click_day");
        Toast.makeText(getApplicationContext(), clickDay, Toast.LENGTH_SHORT).show();


        petDB.child(getUserID).child("Pet List").addValueEventListener(new ValueEventListener() {
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

        //선택된 강아지
        spinner.setSelection(Adapter.NO_SELECTION, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Boolean first = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(first){
                    first = false;
                }
                else {
                    String Name = spinner.getItemAtPosition(position).toString(); //스피너의 강아지 이름 string으로

                    petDB.child(getUserID).child("Pet List").addListenerForSingleValueEvent(new ValueEventListener() { //Pet List로 접근해서 값을 읽는데
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot ds : snapshot.getChildren()) { //Pet List에 담긴 child 개수만큼
                                Pname = ds.getValue().toString(); //값을 string으로 변환
                                Plist.add(Pname);
                            }

                            if(parent.getItemAtPosition(position).toString().equals(Plist.get(position).toString())){ //스피너string이랑 Pet List string이랑 일치하면
                                eventDB.child(getUserID).child(Name).child("date").push().setValue(clickDay); //Pet List하위에 스피너string 하위에 clickDay 등록
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        et_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }

        });


    }


}