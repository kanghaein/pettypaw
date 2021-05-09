package com.example.serbviceexample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //이 세가지만 사용
    //서비스 시작할때, 초기화하는 부분
    @Override
    public void onCreate() {
        super.onCreate();


        mediaPlayer = MediaPlayer.create(this,R.raw.kingporterstomp); //res안에 raw폴더안에 음악문서
        mediaPlayer.setLooping(false); //반복재생할 것인지
    }

    //시작
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer.start();

        return super.onStartCommand(intent, flags, startId);
    }

    //서비스 끝났을 때
    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();

    }
}
