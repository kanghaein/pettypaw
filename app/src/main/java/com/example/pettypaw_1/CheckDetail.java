package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckDetail extends AppCompatActivity {

    Button btn_pre, btn_as;
    TextView tv_date;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    User_event event = new User_event(); //날짜, 상세일정 저장하는 User_event 객체
    User_pet pet = new User_pet(); //스피너에서 펫 이름 받아올 User_pet 객체

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);
        tv_date = findViewById(R.id.tv_date);


        Intent intent = getIntent();
        String clickDay = intent.getStringExtra("click_day");
        String date = intent.getStringExtra("date");

        tv_date.setText(date);

        //뒤로가기 버튼
        btn_pre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });


        //일정추가 버튼
        btn_as.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
                intent.putExtra("click_day", clickDay);
                startActivity(intent);

            }


        });

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<5; i++){
            list.add(String.format("TEST %d", i));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        RecycleAdapter adapter = new RecycleAdapter(list);
        recyclerView.setAdapter(adapter);


    }

}