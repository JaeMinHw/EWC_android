package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class User_controller extends AppCompatActivity implements SensorEventListener {

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


    private ImageView view1, view2, view3, view4, view5;


    private AnimatorSet animatorSet;
    private Button controlBTN;
    private boolean controlFlag = true;
    private Activity activity = this;

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

    final int go = 500; // 한번 바뀌는데 걸리는 시간 즉 흰색에서 파란색으로 될 때까지의 시간이 500ms
    final int back = 600;
    final int left = 400;
    final int right = 300;
    final int stop = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);

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

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);
        view4 = (ImageView) findViewById(R.id.view4);
        view5 = (ImageView) findViewById(R.id.view5);

        Integer colorFrom = Color.BLACK;
        Integer colorTo = Color.WHITE;


        colorAnimationOuter1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
//                view5.setBackgroundColor((Integer) animator.getAnimatedValue());
//                view4.setBackgroundColor((Integer) animator.getAnimatedValue());
//                view3.setBackgroundColor((Integer) animator.getAnimatedValue());
                view1.setBackgroundColor((Integer) animator.getAnimatedValue());
//                Log.e("tttttt",""+Integer.parseInt(animator.getAnimatedValue().toString())) ;
                if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                    view1.setColorFilter(R.color.black);
//                    Log.e("color","White   " + animator.getAnimatedValue());
                } else {
                    view1.setColorFilter(R.color.white);
//                    Log.e("color","Black    " + animator.getAnimatedValue());
                }

            }

        });

        colorAnimationOuter2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view2.setBackgroundColor((Integer) animator.getAnimatedValue());
                if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                    view2.setColorFilter(R.color.black);
                } else {
                    view2.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter3 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view3.setBackgroundColor((Integer) animator.getAnimatedValue());
                if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                    view3.setColorFilter(R.color.black);
                } else {
                    view3.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter4 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view4.setBackgroundColor((Integer) animator.getAnimatedValue());
                if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                    view4.setColorFilter(R.color.black);
                } else {
                    view4.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter5 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view5.setBackgroundColor((Integer) animator.getAnimatedValue());
                if (Integer.parseInt(animator.getAnimatedValue().toString()) == -1) {
                    view5.setColorFilter(R.color.black);
                } else {
                    view5.setColorFilter(R.color.white);
                }

            }

        });
        colorAnimationOuter1.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter1.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter1.setDuration(go);

        colorAnimationOuter2.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter2.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter2.setDuration(stop);

        colorAnimationOuter3.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter3.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter3.setDuration(back);

        colorAnimationOuter4.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter4.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter4.setDuration(right);

        colorAnimationOuter5.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter5.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter5.setDuration(left);

        // @TODO animation set
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(colorAnimationOuter1, colorAnimationOuter2, colorAnimationOuter3, colorAnimationOuter4, colorAnimationOuter5
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

        timerCall.cancel();
    }



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
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
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


                    }
//                    else {
//                        // 다른 응답을 반환한 경우
//                        // 로그인 실패
//                        Toast.makeText(User_controller.this, "위치 전송 실패", Toast.LENGTH_SHORT).show();
//                    }
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
