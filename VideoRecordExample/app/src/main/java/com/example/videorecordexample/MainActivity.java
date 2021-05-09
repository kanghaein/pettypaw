package com.example.videorecordexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false; //현재 녹화중이냐라는 boolean값을 false로 지정해줌


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹화를 위하여 권한을 허용해주세요.")   //권한 팝업에대한 메시지
                .setDeniedMessage("권한이 거부되었습니다. 설정 > 권한에서 허용해주세요.") //거부를 했을때 나오는 팝업메시지
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .check(); //퍼미션을 최종적으로 체크해주라

        btn_record = (Button)findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording) { //녹화버튼과 중지버튼이 같기 때문에 플레이중이면 스탑해란 뜻이 됨.
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.lock();
                    recording = false;
                } else{
                    runOnUiThread(new Runnable() { //동영상을 ui로 처리해준다 그래야 과부화,오류도 줄일 수 있음
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"녹화가 시작되었습니다.",Toast.LENGTH_SHORT).show();
                            try{

                                mediaRecorder = new MediaRecorder();
                                camera.unlock();
                                mediaRecorder.setCamera(camera);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//버튼 누르면 소리재생
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//비디오 소스를 카메라에 넣어라
                                mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));//녹화하는 처리중 화질을 좋게 해주는 역활
                                mediaRecorder.setOrientationHint(90);//촬영각도를 90도로 맞춰라
                                mediaRecorder.setOutputFile('/C:/Download');//저장경로
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());//프리뷰화면 세팅해주는 과정
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;

                            }catch (Exception e){ //예외가 생기면 곧바로 꺼라
                                e.printStackTrace();
                                mediaRecorder.release();
                            }

                        }
                    });
                }
            }
        });



    }

    //퍼미션이 허용되었을때 두가지 경우에 대해서
    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this,"권한 허가",Toast.LENGTH_SHORT).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(MainActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this,"권한 거부",Toast.LENGTH_SHORT).show();
        }
    };


    //surface
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    private void refreshCamera(Camera camera) {
        if(surfaceHolder.getSurface() == null){ //널일때 예외처리
            return;
        }

        //카메라 초기화 작업
        try{
            camera.startPreview();
        }catch(Exception e){
            e.printStackTrace();
        }

        setCamera(camera);

    }

    private void setCamera(Camera cam) {

        camera = cam;

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(camera); //서피스뷰에 뭔가 변화를 감지해서 이쪽으로 호출, 그때마다 카메라 초기화시켜주는 작업
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}