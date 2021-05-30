package com.example.pettypaw_1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

// 캘린더의 날짜를 담기 위한 아이템뷰
public class MonthItemView extends AppCompatTextView {

    private MonthItem item;

    public MonthItemView(Context context){
        super(context);
        init();
    }

    public MonthItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        setBackgroundColor(Color.WHITE);
    }

    public MonthItem getItem(){
        return item;
    }

    public void setItem(MonthItem item){
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(String.valueOf(day));
            setGravity(Gravity.CENTER);
            setTextColor(Color.BLACK);
            setTextSize(20);
        }else{
            setText("");
        }
    }
}