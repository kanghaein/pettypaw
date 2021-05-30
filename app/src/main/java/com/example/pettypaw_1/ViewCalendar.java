package com.example.pettypaw_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 메인 캘린더
public class ViewCalendar extends AppCompatActivity {

    // 다른 액티비티에서 접근 가능
    public static Context context_view;

    GridView monthView;
    TextView monthText;
    MonthAdapter adt;

    // MainActivity 에서 가져온 lg_ID 라는 변수 이용 => 로그인한 ID를 부모로 펫정보 입력
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();
    String[] arrDay = new String[3]; // 날짜를 담기 위한 배열 선언 (년/월/일)이므로 3개의 공간 필요

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference userDB = mDatabase.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar);

        context_view = this;

        monthView = findViewById(R.id.monthView); // 그리드뷰 객체 참조
        adt = new MonthAdapter(this); // 어댑터 객체 생성
        monthView.setAdapter(adt); // 그리드뷰에 어댑터 설정
        monthText = findViewById(R.id.monthText);

        setMonthText(); // 현재 년, 월 출력

        Button monthPrevious = findViewById(R.id.monthPrevious);
        Button monthNext = findViewById(R.id.monthNext);

        // 그리드 셀(날짜) 클릭 시 스케줄 추가 버튼으로 넘어가도록 이벤트 처리
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonthItem item = (MonthItem) adt.getItem(position); // 해당 position의 아이템 가져옴

                // 년, 월, 일 정보를 전달하기 위해 String으로 변환
                String year = Integer.toString(adt.curYear);
                String month = Integer.toString((adt.curMonth) + 1);
                String day = Integer.toString(item.getDay());

                // 각 index에 날짜 값을 담는다
                arrDay[0] = year;
                arrDay[1] = month;
                arrDay[2] = day;

                // CheckDetail 창으로 이동하며 해당 액티비티에 날짜 값들을 전달
                Intent intent = new Intent(getApplicationContext(), CheckDetail.class);
                intent.putExtra("click_day", arrDay);
                intent.putExtra("date", adt.curYear + "년" + (adt.curMonth + 1) + "월" + item.getDay() + "일");

                startActivity(intent);
                finish();

            }

        });

        // "이전 달"버튼 이벤트 리스너
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adt.setPreviousMonth();
                adt.notifyDataSetChanged(); // 어댑터 데이터 갱신 후 뷰 출력
                setMonthText(); // 현재 년, 월 출력
            }
        });

        // "다음 달"버튼 이벤트 리스너
        monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adt.setNextMonth();
                adt.notifyDataSetChanged(); // 어댑터 데이터 갱신 후 뷰 출력
                setMonthText(); // 현재 년, 월 출력
            }
        });
    }

    // 현재 년, 월 출력
    public void setMonthText() {
        int curYear = adt.getCurYear();
        int curMonth = adt.getCurMonth();
        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }

    // 액션버튼 메뉴 액션바에 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // 액션버튼 클릭했을 때 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 설정 아이콘
        if (id == R.id.setting_icon) {
            userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // User List의 "Invite"에 접근
                    String Invite = snapshot.child("User List").child(getUserID).child("Invite").getValue().toString();

                    // 리더로 지정된 유저라면 LeaderSetting 으로 이동
                    if (Invite.equals("Leader")) {
                        Intent intent = new Intent(getApplicationContext(), LeaderSetting.class);
                        startActivity(intent);
                    }

                    // 리더가 아닌 유저라면 MemberSetting 으로 이동
                    else {
                        Intent intent = new Intent(getApplicationContext(), MemberSetting.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}