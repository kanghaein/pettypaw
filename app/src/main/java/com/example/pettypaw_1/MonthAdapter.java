package com.example.pettypaw_1;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.Utils;

import java.util.Calendar;

public class MonthAdapter extends BaseAdapter {

    Calendar cal;
    Context mContext;

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference userDB = mDatabase.getReference("User");
    // Uer_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");

    final DatabaseReference petDB = mDatabase.getReference("User_pet");

    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();
    String LeaderID;

    MonthItem[] items;
    int curYear;
    int curMonth;

    MonthAdapter(Context context) {
        super();
        mContext = context;
        init();
    }

    MonthAdapter(Context context, AttributeSet attrs) {
        super();
        mContext = context;
        init();
    }

    public void init() {
        cal = Calendar.getInstance(); //Calendar 객체 가져오기
        items = new MonthItem[7 * 6]; //아이템 크기 결정

        calculate();//날짜 계산해서 items[] 배열 값 설정
    }

    public void calculate() {

        for (int i = 0; i < items.length; i++) { //items[] 모든 값 0으로 초기화
            items[i] = new MonthItem(0);
        }

        cal.set(Calendar.DAY_OF_MONTH, 1); //1일로 설정

        int startDay = cal.get(Calendar.DAY_OF_WEEK); //현재 달 1일의 요일 (1: 일요일, . . . 7: 토요일)
        int lastDay = cal.getActualMaximum(Calendar.DATE); //달의 마지막 날짜

        int cnt = 1;
        for (int i = startDay - 1; i < startDay - 1 + lastDay; i++) { /* 1일의 요일에 따라 시작위치 다르고 마지막 날짜까지 값 지정*/
            items[i] = new MonthItem(cnt);
            cnt++;
        }

        curYear = cal.get(Calendar.YEAR);
        curMonth = cal.get(Calendar.MONTH);
    }

    public void setPreviousMonth() { //한 달 앞으로 가고 다시 계산
        cal.add(Calendar.MONTH, -1);
        calculate();
    }

    public void setNextMonth() {
        cal.add(Calendar.MONTH, 1); //한 달 뒤로가고 다시 계산
        calculate();
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthItemView view = new MonthItemView(mContext);
        MonthItem item = items[position];

        //각각의 뷰 아이템의 년,월,일 지정
        String[] arrDay = new String[3];
        //각 index에 날짜 값을 담는다.
        arrDay[0] = String.valueOf(curYear);
        arrDay[1] = String.valueOf(curMonth + 1);
        arrDay[2] = String.valueOf(item.getDay());

        //Join으로 배열 묶어준다.
        String ymd = TextUtils.join("", arrDay);

        //View의 Id값을 지정하기 위해서 String값을 integer로 변환한 후 Id지정
        int ymd_integer = Integer.parseInt(ymd);
        view.setId(ymd_integer);
        String ymd_id = String.valueOf(view.getId());

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                petDB.child(LeaderID).child("Pet List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String pet_name = ds.getValue().toString();

                            eventDB.child(LeaderID).child(pet_name).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(ymd_id).exists()) {
                                        view.setBackgroundColor(Color.GRAY);
                                        /**********일정 표시 디자인 부분**********/
                                        //view.setBackgroundResource(R.drawable.ic_baseline_lens_24);
                                        view.setItem(item);

                                        if (position % 7 == 0) { //일요일은 날짜 색 빨간색으로
                                            view.setTextColor(Color.RED);
                                        }

                                    } else {
                                        view.setItem(item);

                                        if (position % 7 == 0) { //일요일은 날짜 색 빨간색으로
                                            view.setTextColor(Color.RED);
                                        }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 150);
        view.setLayoutParams(params);

        return view; //뷰 뿌려주기
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override

    public long getItemId(int position) {
        return 0;
    }

    public int getCurYear() {
        return curYear;
    }

    public int getCurMonth() {
        return curMonth;
    }

}