package com.example.pettypaw_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class ViewCalendar extends AppCompatActivity {

    public static Context context_view;


    GridView monthView;
    TextView monthText;
    MonthAdapter adt;


    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();
    int c;


    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference userDB = mDatabase.getReference("User");
    // Uer_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference GroupDB = mDatabase.getReference("Group");

    final DatabaseReference petDB = mDatabase.getReference("User_pet");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar);

        context_view = this;


        monthView = findViewById(R.id.monthView); //그리드뷰 객체 참조
        adt = new MonthAdapter(this); //어댑터 객체 생성
        monthView.setAdapter(adt); //그리드뷰에 어댑터 설정

        monthText = findViewById(R.id.monthText);
        setMonthText();

        Button monthPrevious = findViewById(R.id.monthPrevious);
        Button monthNext = findViewById(R.id.monthNext);

        //할일 listview로 설정한다.
        ListView todo_list = (ListView) findViewById(R.id.todo_list);


        //그리드 셀 클릭 시 스케줄 추가 버튼으로 넘어가게 설정
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonthItem item = (MonthItem)adt.getItem(position);   // 해당 아이템 가져옴

                Integer[] array = new Integer[3];
                array[0] = adt.curYear;
                array[1] = adt.curMonth+1;
                array[2] = item.getDay();

                String current_date = Arrays.toString(array);
                Intent intent = new Intent(getApplicationContext(), CheckDetail.class);
                intent.putExtra("click_day", current_date);

                startActivity(intent);

                //년, 월, 일 받게 설정 - 명시적으로 보여주기위해 토스트 메시지로 띄운 것 나중에 삭제해야됨
                //adt.curYear (년) / adt.curMonth (월) / item.getDay() (일)
//                Toast.makeText(getApplicationContext(), adt.curYear + "년" + (adt.curMonth+1) + "월" + item.getDay() + "일" , Toast.LENGTH_LONG).show();

            }

        });

        // 뒤로가기 버튼 이벤트 리스너 설정
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adt.setPreviousMonth();
                adt.notifyDataSetChanged(); //어댑터 데이터 갱신하고 뷰 다시 뿌리기
                setMonthText();
                c = adt.getCurMonth()+1;
                Toast.makeText(getApplicationContext(),"월 : "+ c,Toast.LENGTH_SHORT).show();
            }
        });

        // 앞으로 가기 버튼에 이벤트 리스너 설정
        monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adt.setNextMonth();
                adt.notifyDataSetChanged(); //어댑터 데이터 갱신하고 뷰 다시 뿌리기
                setMonthText();
                c = adt.getCurMonth()+1;
                Toast.makeText(getApplicationContext(),"월 : "+ c,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setMonthText(){
        int curYear = adt.getCurYear();
        int curMonth = adt.getCurMonth();
        monthText.setText(curYear+"년 "+(curMonth+1)+"월");
    }

    //액션버튼 메뉴 액션바에 집어넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼 클릭했을 때 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //설정 아이콘
        if (id == R.id.setting_icon) {
            userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String Invite = snapshot.child("User List").child(getUserID).child("Invite").getValue().toString();
                    // 리더로 지정된 유저라면 LeaderSetting 으로 이동
                    if (Invite.equals("Leader")) {
                        Intent intent = new Intent(getApplicationContext(), LeaderSetting.class);
                        startActivity(intent);
                    }
                    // 리더가 아닌 유저라면 MemberSetting 으로 이동
                    else{
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