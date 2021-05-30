package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


//알람//
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 로그인 액티비티
public class MainActivity extends AppCompatActivity {


    // 알람 변수
    private TimePicker timePicker;
    private AlarmManager alarmManager;
    private int hour, minute;
    CheckBox cbSun, cbMon, cbTue, cbWed, cbThu, cbFri, cbSat;

    static String TAG="MainActivity";

    // 화면 간 전환을 위한 REQUEST_CODE_SIGN 설정
    public static final int REQUEST_CODE_SIGN = 101;

    EditText lg_ID, lg_PW; // 아이디 칸, 패스워드 칸
    Button btn_sign, btn_login; // 회원가입 버튼, 로그인 버튼

    // 다른 액티비티에서 접근 가능
    public static Context context_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 다른 액티비티에서 접근 가능
        context_main = this;

        //알람
        timePicker=findViewById(R.id.tp_timepicker);
        alarmManager= (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        cbSun=findViewById(R.id.cb_sun);
        cbMon=findViewById(R.id.cb_mon);
        cbTue=findViewById(R.id.cb_thu);
        cbWed=findViewById(R.id.cb_wed);
        cbThu=findViewById(R.id.cb_thu);
        cbFri=findViewById(R.id.cb_fri);
        cbSat=findViewById(R.id.cb_sat);


        // 레이아웃 id 값들을 불러와 변수에 저장
        lg_ID = findViewById(R.id.lg_ID);
        lg_PW = findViewById(R.id.lg_PW);
        btn_sign = findViewById(R.id.btn_sign);
        btn_login = findViewById(R.id.btn_login);

        // 파이어베이스 연동
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // User.java 를 통해 데이터베이스 접근
        final DatabaseReference userDB = mDatabase.getReference("User");

        // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 입력한 ID와 PW를 String 값으로 저장
                String getUserID = lg_ID.getText().toString();
                String getUserPW = lg_PW.getText().toString();

                // ID 나 PW 가 공백이라면
                if (getUserID.equals("") || getUserPW.equals("")){
                    Toast.makeText(MainActivity.this, "ID 혹은 PW를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    // addValueEventListener 를 이용한 데이터 읽기
                    userDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // DB의 User 노드에 child 로 입력한 ID가 존재한다면
                            if (snapshot.child(getUserID).exists()) {
                                // DB에 저장된 로그인 하려는 ID의 PW 값을 얻어오고
                                String myPW = snapshot.child(getUserID).child("PW").getValue().toString();
                                // 얻어온 PW와 입력된 PW가 일치한다면
                                if (myPW.equals(getUserPW)) {
                                    // 이미 그룹장인 기존 유저라면 바로 캘린더로 이동
                                    if (snapshot.child("User List").child(getUserID).child("Invite").getValue().toString().equals("Leader")) {
                                        Intent intent = new Intent(getApplicationContext(), ViewCalendar.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        // 리더 휘하의 그룹 멤버로 가입된 유저라면 바로 캘린더로 이동
                                        if (snapshot.child("User List").child(getUserID).child("Invite").getValue().toString().equals("Member")) {
                                            Intent intent = new Intent(getApplicationContext(), ViewCalendar.class);
                                            startActivity(intent);
                                        }
                                        // 리더도 아니고 그룹에도 속하지 않은 신규유저라면 welcome 창으로 이동
                                        else {
                                            Intent intent = new Intent(getApplicationContext(), welcome.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                // 얻어온 PW와 입력된 PW가 일치하지 않는다면
                                else {
                                    Toast.makeText(MainActivity.this, "ID 혹은 PW 가 옳지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // DB의 User 노드에 child 로 입력한 ID가 존재하지 않는다면
                            else {
                                Toast.makeText(MainActivity.this, "존재하지 않는 ID 입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        // 회원가입 버튼
        btn_sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 회원가입 버튼 누르면 회원가입 창으로 이동
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGN);
            }
        });
    }

    // 알람 이벤트
    public void regist(View view) {

        boolean[] week = { false, cbSun.isChecked(), cbMon.isChecked(), cbTue.isChecked(), cbWed.isChecked(),
                cbThu.isChecked(), cbFri.isChecked(), cbSat.isChecked() }; // cbSun을 1번부터 사용하기 위해 배열 0번은 false로 고정

        if(!cbSun.isChecked() &&  !cbMon.isChecked() &&  !cbTue.isChecked() && !cbWed.isChecked() &&  !cbThu.isChecked() && !cbFri.isChecked() && !cbSat.isChecked()){
            Toast.makeText(this, "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour=timePicker.getHour();
            minute=timePicker.getMinute();
        }else{
            Toast.makeText(this, "버전을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AlarmSetting.class);
        intent.putExtra("weekday", week);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,intent, 0); //PendingIntent.FLAG_UPDATE_CURRENT

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = new Date();
        long intervalDay = 24 * 60 * 60 * 1000; // 24시간

        long selectTime=calendar.getTimeInMillis();
        long currenTime=System.currentTimeMillis();

        // 설정한 시간<현재 시간 알람이 다음날 울리게 설정
        if(currenTime>selectTime){
            selectTime += intervalDay;
        }

        Log.e(TAG,"등록 버튼을 누른 시간 : "+today+"  설정한 시간 : "+calendar.getTime());

        Log.d(TAG,"calendar.getTimeInMillis()  : "+calendar.getTimeInMillis());

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,  intervalDay, pIntent);

    }

    public void unregist(View view) {
        Intent intent = new Intent(this, AlarmSetting.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}