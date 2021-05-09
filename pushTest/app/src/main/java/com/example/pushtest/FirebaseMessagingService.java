package com.example.pushtest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{



    private static final String TAG = "FirebaseMsgService";

    private String msg,title;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG,"onMessageReceived");

        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //인텐트를 가지고 있는 클래스지만 다른 애플리케이션에 권한을 허가해서 마치 본인 앱의 프로세스에서 사용하는 것처럼 함,(=notification눌렀을때 행위 구현한것)
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        //사진이 노티피케이션 푸시에서 바로 뜨는 역할
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1,1000}); //1000ms단위,즉 1초동안 진동이 울려라

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,mBuilder.build());

        mBuilder.setContentIntent(contentIntent);


    }
}
