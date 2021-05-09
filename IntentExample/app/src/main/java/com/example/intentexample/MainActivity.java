package com.example.intentexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_move;
    private EditText et_test;
    private String str;
    private ImageView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_test = findViewById(R.id.et_test);
        str = et_test.getText().toString(); //입력한 문자열을 받아서 string형태 str에 저장해라

        btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , SubActivity.class); // 액티비티 설정
                intent.putExtra("str", str); //보내기 전에 str 담아서 sub activity쪽으로 쏨
                startActivity(intent); //액티비티 실제로 이동해주는 구문
            }
        });

        test = (ImageView)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"아주 잘했음", Toast.LENGTH_SHORT).show();//팝업으로 송출
            }
        });




    }
}