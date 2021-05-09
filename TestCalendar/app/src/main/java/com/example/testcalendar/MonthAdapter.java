package com.example.testcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Calendar;

public class MonthAdapter extends BaseAdapter {
    Calendar cal;

    Context mContext;

    MonthItem[] items;
    int curYear;
    int curMonth;

    MonthAdapter(Context context){
        super();
        mContext = context;
        init();
    }

    MonthAdapter(Context context, AttributeSet attrs){
        super();
        mContext = context;
        init();
    }

    public void init(){
        cal = Calendar.getInstance(); //Calendar 객체 가져오기
        items = new MonthItem[7*6]; //아이템 크기 결정

        calculate();//날짜 계산해서 items[] 배열 값 설정
    }

    public void calculate(){
        for(int i=0; i<items.length; i++){ //items[] 모든 값 0으로 초기화
            items[i] = new MonthItem(0);
        }

        cal.set(Calendar.DAY_OF_MONTH, 1); //1일로 설정

        int startDay = cal.get(Calendar.DAY_OF_WEEK); //현재 달 1일의 요일 (1: 일요일, . . . 7: 토요일)
        int lastDay = cal.getActualMaximum(Calendar.DATE); //달의 마지막 날짜

        int cnt = 1;
        for(int i=startDay-1; i<startDay-1+lastDay; i++){ /* 1일의 요일에 따라 시작위치 다르고 마지막 날짜까지 값 지정*/
            items[i] = new MonthItem(cnt);
            cnt++;
        }

        curYear = cal.get(Calendar.YEAR);
        curMonth = cal.get(Calendar.MONTH);
    }

    public void setPreviousMonth(){ //한 달 앞으로 가고 다시 계산
        cal.add(Calendar.MONTH, -1);
        calculate();
    }

    public void setNextMonth(){
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
        view.setItem(item); //날짜 값이 0이면 ""으로, 아니면 날짜값으로 TextView의 Text 지정

        if(position%7==0){ //일요일은 날짜 색 빨간색으로
            view.setTextColor(Color.RED);
        }

        GridView.LayoutParams params = new GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT,150);
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

    public int getCurYear(){
        return curYear;
    }

    public int getCurMonth(){
        return curMonth;
    }

}