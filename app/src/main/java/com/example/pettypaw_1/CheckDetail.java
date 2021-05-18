package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();


    User_event event = new User_event(); //날짜, 상세일정 저장하는 User_event 객체
    User_pet pet = new User_pet(); //스피너에서 펫 이름 받아올 User_pet 객체

    // 상세보기의 일정리스트 출력을 위한 리사이클러뷰 리스트 생성
    ArrayList<Recycler_item> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);
        tv_date = findViewById(R.id.tv_date);

        // ViewCalendar 액티비티로부터 전송받음
        Intent intent = getIntent();
        String[] Day = intent.getStringArrayExtra("click_day"); // "YYYY MM DD"
        String date = intent.getStringExtra("date"); // "YYYY년 MM월 DD일"

        // 전송받은 배열의 각 인덱스의 값들에 띄어쓰기로 이어붙여 하나의 문자열로 만든다
        String clickDay = TextUtils.join(" ", Day);

        tv_date.setText(date);

        //뒤로가기 버튼
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        // 상세보기에 일정리스트 띄우기, Pet List 를 이용하기 위한 petDB 접근
        petDB.child(getUserID).child("Pet List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //기존 리스트가 존재하지 않게 초기화
                list.clear();

                // 자신이 등록한 반려동물의 수만큼 반복
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String pet_name = ds.getValue().toString();

                    Drawable drawable = getDrawable(R.drawable.paw).mutate();


                    // 상세일정을 불러오기 위한 eventDB 접근
                    eventDB.child(getUserID).child(pet_name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // DB상에 클릭한 날짜에 해당하는 노드가 존재한다면
                            if (snapshot.child(clickDay).exists()) {
                                String detail = snapshot.child(clickDay).child("Detail").getValue().toString();

                                addItem(pet_name, detail, drawable);


                                petDB.child(getUserID).child("Pet Information").child(pet_name).child("Color").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String color = snapshot.getValue().toString();

                                        switch (color) {
                                            case "빨강색":
                                                drawable.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "주황색":
                                                drawable.setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "노랑색":
                                                drawable.setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "초록색":
                                                drawable.setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "파랑색":
                                                drawable.setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "남색":
                                                drawable.setColorFilter(Color.parseColor("#00106A"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            case "보라색":
                                                drawable.setColorFilter(Color.parseColor("#E448FF"), PorterDuff.Mode.SRC_IN);
                                                break;
                                            default:
                                                break;
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                RecycleAdapter adapter = new RecycleAdapter(list);

                                //리스트 저장 및 새로고침하여 반영
                                adapter.notifyDataSetChanged();

                                recyclerView.setAdapter(adapter);

                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    // 리사이클러뷰의 각각의 item 에 따로 데이터를 저장하기 위한 함수
    public void addItem(String petName, String detail, Drawable icon) {
        Recycler_item item = new Recycler_item();

        item.setPetName(petName);
        item.setDetail(detail);
        item.setIcon(icon);


        list.add(item);
    }

}
