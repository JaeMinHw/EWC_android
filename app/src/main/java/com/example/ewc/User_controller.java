package com.example.ewc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class User_controller extends AppCompatActivity implements SensorEventListener{

    send_control sc = new send_control();

    private Timer timerCall;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private int COLLISION_THRESHOLD = 25000;  // 충돌 임계값

    private static SensorManager mSensorManager;
    private Sensor mAccelerometer; // 가속도 센스
    private Sensor mMagnetometer; // 자력계 센스
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    String resultText = "";
    double latitude;
    double longitude;

    // 위치 관리자 객체 참조하기

    String s;

    private TextToSpeech tts;



    private Button temp;
    private ImageView[] imgView = new ImageView[7];
    private Integer[] imgView_id= {R.id.view, R.id.view1, R.id.view2, R.id.view3, R.id.view4, R.id.view5, R.id.view6};


    private AnimatorSet animatorSet;
    private Button controlBTN;
    private boolean controlFlag = true;
    private Activity activity = this;


    private ValueAnimator[] vaAni = new ValueAnimator[7];

    // turn left
    private ValueAnimator colorAnimationOuter;

    // go
    private ValueAnimator colorAnimationOuter1;

    // stop
    private ValueAnimator colorAnimationOuter2;

    // back
    private ValueAnimator colorAnimationOuter3;

    // right
    private ValueAnimator colorAnimationOuter4;

    // left
    private ValueAnimator colorAnimationOuter5;

    // turn right
    private ValueAnimator colorAnimationOuter6;

    final int go = 500; // 한번 바뀌는데 걸리는 시간 즉 흰색에서 파란색으로 될 때까지의 시간이 500ms
    final int back = 600;
    final int left = 400;
    final int right = 300;
    final int stop = 200;
    final int turn_left = 800;
    final int turn_right = 700;


    Integer ord[] = {turn_left,go,stop,back,right,left,turn_right};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);


//        tts = new TextToSpeech(this, this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mMagnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

//        // 활동 퍼미션 체크
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
//
//            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
//        }

        for(int i=0;i<7;i++){
            imgView[i] = (ImageView) findViewById(imgView_id[i]);
        }

        vaAni[0] = colorAnimationOuter;
        vaAni[1] = colorAnimationOuter1;
        vaAni[2] = colorAnimationOuter2;
        vaAni[3] = colorAnimationOuter3;
        vaAni[4] = colorAnimationOuter4;
        vaAni[5] = colorAnimationOuter5;
        vaAni[6] = colorAnimationOuter6;


        temp = findViewById(R.id.temp);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task1 crash = new Task1();
                crash.start();
            }
        });

        Integer colorFrom = Color.BLACK;
        Integer colorTo = Color.WHITE;


        for(int i=0;i<7;i++){
            final int num = i;
            vaAni[i] = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            vaAni[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animator) {
                    imgView[num].setBackgroundColor((Integer) animator.getAnimatedValue());
                    if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                        imgView[num].setColorFilter(R.color.black);
                    } else {
                        imgView[num].setColorFilter(R.color.white);
                    }
                }
            });
            vaAni[i].setRepeatCount(ValueAnimator.INFINITE);
            vaAni[i].setRepeatMode(ValueAnimator.REVERSE);
            vaAni[i].setDuration(ord[i]);

        }




        // @TODO animation set
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(vaAni[0],vaAni[1], vaAni[2], vaAni[3], vaAni[4], vaAni[5],vaAni[6]
        );

        animatorSet.start();

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            double altitude = location.getAltitude();



            Log.e("tt","위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
        }


        // 위치정보를 원하는 시간, 거리마다 갱신해준다.
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                gpsLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                gpsLocationListener);


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Task2 location_task = new Task2();
                location_task.start();
            }
        };



        timerCall = new Timer();
        timerCall.schedule(timerTask,1000,5000);




    }

    @Override

    public void onDestroy( ) {

        super.onDestroy( );

//        timerCall.cancel();
    }


//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//            int language = tts.setLanguage(Locale.KOREAN);
//
//            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
//            } else {
//                try {
//                    speakOutNow();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } else {
//            Toast.makeText(this, "TTS 실패!", Toast.LENGTH_SHORT).show();
//        }
//    }

    //Speak out...
    private void speakOutNow() throws IOException {
        InputStream in = getResources().openRawResource(R.raw.read_text);
        byte[] b = new byte[in.available()];

        in.read(b);
        s = new String(b);
        Log.e("test",s);

        String text = s;
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

//    public void click(String value) {
//        new Thread() {
//            @Override
//            public void run() {
//                sc.move(value);
//            }
//        }
//    }




    public void go(View view) {
        new Thread() {
            public void run() {
                sc.move("go");
            }
        }.start();
    }

    public void back(View view) {
        new Thread() {
            public void run() {
                sc.move("back");
            }
        }.start();
    }

    public void right(View view) {
        new Thread() {
            public void run() {
                sc.move("right");
            }
        }.start();
    }

    public void left(View view) {
        new Thread() {
            public void run() {
                sc.move("left");
            }
        }.start();
    }

    public void stop(View view) {
        new Thread() {
            public void run() {
                sc.move("stop");
            }
        }.start();
    }

    public void turn_left(View view) {
        new Thread() {
            public void run() {
                sc.move("Turning_left");
            }
        }.start();
    }

    public void turn_right(View view) {
        new Thread() {
            public void run() {
                sc.move("Turning_right");
            }
        }.start();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            mGravity = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }


        // 걸음 센서 이벤트 발생시

        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis(); // 현재시간


            // 0.1초 간격으로 가속도값을 업데이트
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                // 내가 마음대로 정한 충돌량
                double collision_detect = Math.sqrt(Math.pow(z - last_z, 2) * 100 + Math.pow(x - last_x, 2) * 10 + Math.pow(y - last_y, 2) * 10) / diffTime * 10000;


                if (collision_detect > COLLISION_THRESHOLD) {
                    //지정된 수치이상 흔들림이 있으면 실행
                    Toast.makeText(this, "충돌!!", Toast.LENGTH_SHORT).show();
                    Task1 crash = new Task1();
                    crash.start();


               }
                //갱신
                last_x = x;
                last_y = y;
                last_z = z;
            }


            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values;
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic = event.values;
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            longitude = location.getLongitude(); // 위도
            latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            Log.e("tt","위치정보 : " + provider + "\n" + "위도 : " + longitude + "\n" + "경도 : " + latitude + "\n" + "고도 : " + altitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };




    // 사용자 위치 전송 thread
    class Task2 extends Thread {

        private String str, receiveMsg;

        public void run() {
            try {
                URL url = new URL(server_link.link+"place/"+login_info.id+"/"+latitude+"/"+longitude);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int resCode = conn.getResponseCode();

                    if(resCode == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();

                        // 응답 데이터 처리
                        String responseData = response.toString();
                        if (responseData.equals("success")) {
                            // 서버에서 success를 반환한 경우
                            Log.d("MainActivity", "서버에서 success를 반환했습니다.");


                        } else {
                            // 다른 응답을 반환한 경우
                            // 로그인 실패
                            Toast.makeText(User_controller.this, "위치 전송 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    class Task1 extends Thread {

        private String str, receiveMsg;

        public void run() {
            try {
                URL url = new URL(server_link.link+"crash/"+login_info.id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int resCode = conn.getResponseCode();

                    if(resCode == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();

                        // 응답 데이터 처리
                        String responseData = response.toString();
                        if (responseData.equals("success")) {
                            // 서버에서 success를 반환한 경우
                            Log.d("crash", "서버에서 success를 반환했습니다.");
                            Toast.makeText(User_controller.this, "보호자에게 알림을 전송했습니다.", Toast.LENGTH_LONG).show();


                        }

                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}



