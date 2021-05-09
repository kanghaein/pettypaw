package com.example.sharedexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText et_save;
    String shared = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_save = (EditText)findViewById(R.id.et_save);

        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);//edit text에 글자 입력 뒤로 가기 버튼 누르면 종료.
        String value = sharedPreferences.getString("kang", "");
        et_save.setText(value); //value에 있는 stirng값을 settext를 해줌 ->불러오기 구문


    }

    @Override
    protected void onDestroy() { //나가버리면 이게 호출됨, 이때 sharedpere로 저장시키면서 나갈 수 있게 구문을 만들어줘야됨, 그래야 다음에 실행했을 때 불러와짐
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();//sharedpref안에 editor를 연결한 상태
        String value = et_save.getText().toString();//edittext로 입력 받은 값을 value에 입혀주는 상황
        editor.putString("kang",value);//kang이 별명 이 값의
        editor.commit();//실제 세이브를 완료하라는 뜻 -> 저장 구문



    }
}