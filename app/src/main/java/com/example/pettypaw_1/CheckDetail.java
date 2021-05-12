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
<<<<<<< HEAD
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.AttributeSet;
=======
import android.util.AttributeSet;
import android.util.Log;
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
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
<<<<<<< HEAD
    String detail;

    List<Object> Array = new ArrayList<Object>();
=======
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");
<<<<<<< HEAD
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

=======

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a

    User_event event = new User_event(); //날짜, 상세일정 저장하는 User_event 객체
    User_pet pet = new User_pet(); //스피너에서 펫 이름 받아올 User_pet 객체

<<<<<<< HEAD

=======
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);
        tv_date = findViewById(R.id.tv_date);

<<<<<<< HEAD
        // ViewCalendar 액티비티로부터 전송받음
        Intent intent = getIntent();
        String[] Day = intent.getStringArrayExtra("click_day");
        String date = intent.getStringExtra("date");

        // 전송받은 배열의 각 인덱스의 값들에 띄어쓰기로 이어붙여 하나의 문자열로 만든다
        String clickDay = TextUtils.join(" ", Day);

=======
        Intent intent = getIntent();
        String clickDay = intent.getStringExtra("click_day");
        String date = intent.getStringExtra("date");

>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
        tv_date.setText(date);

        //뒤로가기 버튼
        btn_pre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

<<<<<<< HEAD

=======
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a
        //일정추가 버튼
        btn_as.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
                intent.putExtra("click_day", clickDay);
                startActivity(intent);

            }
<<<<<<< HEAD


        });


        eventDB.child(getUserID).child("dog1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(clickDay).exists()) {
                    ArrayList<String> list = new ArrayList<>();
                    String detail = snapshot.child(clickDay).child("Detail").getValue().toString();

                    list.add(String.format(detail));

                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    RecycleAdapter adapter = new RecycleAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(CheckDetail.this, "일정이 없습니다", Toast.LENGTH_SHORT).show();
                }



           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });





=======
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<String> list = new ArrayList<>();
        for (int i=0;i<5;i++){
            list.add(String.format("TEST %s",i));
            //list.add(String.format("%s", date));
        }
        recyclerview_Adapter adapter = new recyclerview_Adapter(list);
        recyclerView.setAdapter(adapter);
>>>>>>> a3b78231dee788dba5e94af3caafb54c4473526a


    }

}