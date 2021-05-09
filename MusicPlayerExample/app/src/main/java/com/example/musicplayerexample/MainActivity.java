package com.example.musicplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_play;
    Button btn_stop;

    MediaPlayer mediaPlayer;


    // 액티비티가 종료될때, 이곳을 실행함.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null; //액티비티가 종료되더라도 미디어 플레이어의 자원을 해제함으로써 정확하게 초기화
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_play=findViewById(R.id.btn_play);
        btn_stop=findViewById(R.id.btn_stop);

        //재생버튼 눌렀을 때
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.music);
                mediaPlayer.start();
            }
        });


        //정지버튼 눌렀을 때
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){ //is ---- => ----인지
                    mediaPlayer.stop();  //실행중이 아닌데 스탑을 할 경우 앱이 죽을 수 있음 그걸 방지해줌
                    mediaPlayer.reset(); //플레이중일때 스탑
                }

            }
        });



    }
}