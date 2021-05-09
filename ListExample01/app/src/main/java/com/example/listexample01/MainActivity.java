package com.example.listexample01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list = (ListView)findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);//현재 액티비티에 적용,안드로이드 기본 UI 적용
        list.setAdapter(adapter); //list에 apdater를 세팅함, 연결함

        data.add("굿모닝");
        data.add("굿애프터눈");
        data.add("굿이브닝");
        adapter.notifyDataSetChanged(); // 이상태를 현재 저장해버리겠다.




    }
}