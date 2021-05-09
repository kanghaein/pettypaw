package com.example.dialogexam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btn_dialog;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_dialog = (Button)findViewById(R.id.btn_dialog);
        tv_result = (TextView)findViewById(R.id.tv_result);

        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                //dialog에서는 필수적으로 해줘야되는 요소
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("제목");
                ad.setMessage("앱을 종료하시겠습니까?");

                //다이아로그안에 edit text란 객체를 add해줘라 view로 추가해주라
                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);


                //긍정버튼 누른 경우
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = et.getText().toString(); //edittext안에 값을 가져와라
                        tv_result.setText(result);
                        dialog.dismiss();//이 모든걸 실행하고 현재 다이아로그 닫아라
                    }
                });

                //부정버튼을 누른 경우
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show(); //이 모든게 정상적으로 뜨게 만듦
            }
        });

    }
}