package com.example.threadexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


//백그라운드 상태로 들어갈때 처리해 줄 수 있는 구문
public class MainActivity extends AppCompatActivity {

    Button btn_start,btn_stop;
    Thread thread;
    boolean isThread = false; //true냐 false냐에서 초깃값을 false로 지정해줌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //스레드 시작
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isThread = true;
                thread = new Thread() {
                  public void run() { //true이니까 백그라운드에서 게속 돌고 있음
                      while(isThread) {
                          try {
                              sleep(5000); //5초동안 잠시 쉬어라
                          } catch (InterruptedException e) {
                              e.printStackTrace(); //catch문으로 예외처리 가능,오류 잡아줌
                          }
                          handler.sendEmptyMessage(0);
                      }
                  }
                };
                thread.start();

            }
        });

        //스레드 종료
        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isThread = false;
            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Toast.makeText(getApplicationContext(),"메가박스 커피",Toast.LENGTH_SHORT).show();//handler값을 5초마다 실행하게 된다.
        }
    };

}