package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 상세일정 보기
public class CheckDetail extends AppCompatActivity {

    Button btn_pre, btn_as; // 이전, 일정추가 버튼
    CheckBox Feed, Walk; // 밥, 산책 체크박스
    TextView tv_date; // 현재 날짜 표시
    String clickDay; // 클릭한 날짜
    String LeaderID; // 그룹장의 ID
    int i=0; // 반려동물 리스트 추가를 막기위한 count 변수

    // MainActivity 에서 로그인 할 때 입력한 ID 값, 즉 자신의 ID
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();

    // 상세보기의 일정리스트 출력을 위한 리사이클러뷰 리스트 생성
    // 리사이클러뷰에 출력될 값으로 리사이클러뷰 어댑터로 전달될 리스트
    ArrayList<Recycler_item> list = new ArrayList<>();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");
    // User.java를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");

    public static Context context_CheckDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        context_CheckDetail = this;

        // 레이아웃 id 값과 변수들을 연결
        btn_pre = findViewById(R.id.btn_pre);
        btn_as = findViewById(R.id.btn_as);
        Feed = findViewById(R.id.feed_checked);
        Walk = findViewById(R.id.walk_checked);
        tv_date = findViewById(R.id.tv_date);

        // ViewCalendar 액티비티로부터 전송받은 클릭한 날짜와 오늘의 날짜
        Intent intent = getIntent();
        String[] Day = intent.getStringArrayExtra("click_day"); // "YYYY MM DD"
        String date = intent.getStringExtra("date"); // "YYYY년 MM월 DD일"

        // 전송받은 배열의 각 인덱스의 값들을 이어붙여 하나의 문자열로 만든다
        clickDay = TextUtils.join("", Day);

        // 오늘의 날짜를 tv_date 에 세트해 화면에 출력된다
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
                // AddSchedule.java 의 액티비티를 시작하면서 클릭한 날짜에 대한 값을 전송한다
                Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
                intent.putExtra("click_day", clickDay);
                startActivity(intent);
            }
        });


        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // DB 상의 (User List -> 자신의 ID -> Leader_ID) 에 저장된 값은 얻어와 문자열로 저장한다
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                // 리스트 초기화
                list.clear();

                // 리사이클러뷰 item 들의 모든 이벤트
                // 상세보기에 일정리스트 띄우기, Pet List 를 이용하기 위한 petDB 접근
                petDB.child(LeaderID).child("Pet List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 자신이 등록한 반려동물의 수만큼 반복
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            // 루프문을 돌때마다 Pet List 의 반려동물들 차례로 변수에 저장
                            String pet_name = ds.getValue().toString();

                            // 펫리스트에 등록된 자식의 수를 변수에 저장
                            long child_count = snapshot.getChildrenCount();
                            // 리사이클러뷰의 아이콘 변수
                            Drawable drawable = getDrawable(R.drawable.paw).mutate();

                            // 리사이클러뷰의 각 item 에 접근하기 위한 recycler_item 객체 생성
                            Recycler_item item = new Recycler_item();
                            // 리사이클러뷰 생성
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            // 리사이클러뷰 어댑터에 list 변수에 있는 값들을 저장
                            RecycleAdapter adapter = new RecycleAdapter(list);

                            // 리사이클러뷰의 각 item 들을 set
                            item.setPetName(pet_name);
                            item.setDetail("등록된 일정 없음");
                            item.setSelected_feed(false);
                            item.setSelected_walk(false);
                            item.setIcon(drawable);

                            // list 에 item 값들을 추가, ArrayList 이므로 제한없이 저장된다
                            list.add(item);


                            // 리사이클러뷰에 보여질 반려동물 색상, Pet Information 에 저장된 Color 값을 이용한다
                            petDB.child(LeaderID).child("Pet Information").child(pet_name).child("Color").addValueEventListener(new ValueEventListener() {
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

                            // 상세일정을 불러오기 위한 eventDB 접근
                            eventDB.child(LeaderID).child(pet_name).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // DB 상에 클릭한 날짜에 해당하는 노드가 존재한다면
                                    if (snapshot.child(clickDay).exists()) {
                                        // 상세일정이 있으면 받아와서 리사이클러뷰 item의 Detail 부분을 갱신해준다
                                        String detail = snapshot.child(clickDay).child("Detail").getValue().toString();
                                        item.setDetail(detail);

                                        // 액티비티에 접근하면 체크박스의 저장된 상태를 불러오기 위한 if문
                                        // 클릭한 날짜의 자식으로 Feed 와 Walk 가 둘 다 존재한다면
                                        if (snapshot.child(clickDay).child("Feed").exists() && snapshot.child(clickDay).child("Walk").exists()) {
                                            String feed_check = snapshot.child(clickDay).child("Feed").getValue().toString();
                                            String walk_check = snapshot.child(clickDay).child("Walk").getValue().toString();
                                            // Feed 와 Walk 가 둘 다 "checked" 상태라면 펫이름, 상세일정 입력 후 모든 체크박스 체크한다
                                            if (feed_check.equals("checked") && walk_check.equals("checked")) {
                                                item.setSelected_feed(true);
                                                item.setSelected_walk(true);
                                            }
                                        }
                                        // 클릭한 날짜의 자식으로 Feed 만 존재한다면
                                        else if (snapshot.child(clickDay).child("Feed").exists()) {
                                            String feed_check = snapshot.child(clickDay).child("Feed").getValue().toString();
                                            // Feed 의 상태가 "checked"라면 펫이름, 상세일정 입력 후 Feed 만 체크한다
                                            if (feed_check.equals("checked")) {
                                                item.setSelected_feed(true);
                                                item.setSelected_walk(false);
                                            }
                                        }
                                        // 클릭한 날짜의 자식으로 Walk 만 존재한다면
                                        else if (snapshot.child(clickDay).child("Walk").exists()) {
                                            String walk_check = snapshot.child(clickDay).child("Walk").getValue().toString();
                                            // Walk 의 상태가 "checked"라면 펫이름, 상세일정 입력 후 Walk 만 체크한다
                                            if (walk_check.equals("checked")) {
                                                item.setSelected_feed(false);
                                                item.setSelected_walk(true);
                                            }
                                        }
                                        // 클릭한 날짜의 자식으로 Detail 만 존재한다면
                                        else {
                                            // 펫이름, 상세일정 입력 후 모든 체크박스를 비활성화 한다
                                            item.setSelected_feed(false);
                                            item.setSelected_walk(false);
                                        }
                                    }

                                    // ArrayList 는 크기에 제한이 없으므로 반려동물이 등록된 수보다 더 출력되는 것을 방지
                                    // 만약 i가 펫리스트에 등록된 반려동물의 수보다 작거나 같다면 어댑터를 이용한 작업 실시
                                    if(i < child_count) {
                                        //리스트 저장 및 새로고침하여 반영
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);
                                    }
                                    // 하나의 반려동물에 대한 처리를 한 후 i를 +1 한다
                                    i++;
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
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 스마트폰 자체의 뒤로가기 버튼을 누를 경우
    @Override
    public void onBackPressed() {
        // ViewCalendar.java 의 액티비티를 시작한 후 현재 액티비티 종료
        Intent intent = new Intent(getApplicationContext(), ViewCalendar.class);
        startActivity(intent);
        finish();
    }
}