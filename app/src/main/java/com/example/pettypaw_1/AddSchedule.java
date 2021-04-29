package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddSchedule extends AppCompatActivity {

    private Spinner spinner;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        spinner = findViewById(R.id.Spinner);


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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddSchedule.this, "선택된 강아지 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}