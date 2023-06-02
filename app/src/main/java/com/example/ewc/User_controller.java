package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class User_controller extends AppCompatActivity implements SensorEventListener {

    send_control sc = new send_control();

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private int COLLISION_THRESHOLD = 25000;  // 충돌 임계값

    private static SensorManager mSensorManager;
    private Sensor mAccelerometer; // 가속도 센스
    private Sensor mMagnetometer; // 자력계 센스
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    String resultText = "";



    private ImageView view1,view2,view3,view4,view5;

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
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view1.setColorFilter(R.color.black);
//                    Log.e("color","White   " + animator.getAnimatedValue());
                }
                else {
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
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view2.setColorFilter(R.color.black);
                }
                else {
                    view2.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter3 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view3.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view3.setColorFilter(R.color.black);
                }
                else {
                    view3.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter4 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view4.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view4.setColorFilter(R.color.black);
                }
                else {
                    view4.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter5 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view5.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view5.setColorFilter(R.color.black);
                }
                else {
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
        animatorSet.playTogether(colorAnimationOuter1, colorAnimationOuter2,colorAnimationOuter3,colorAnimationOuter4,colorAnimationOuter5
                );

        animatorSet.start();
    }

    public void go(View view) {
        new Thread(){public void run() {
            sc.move("go");
        }}.start();
    }
    public void back(View view) {
        new Thread(){public void run() {
            sc.move("back");
        }}.start();
    }
    public void right(View view) {
        new Thread(){public void run() {
            sc.move("right");
        }}.start();
    }
    public void left(View view) {
        new Thread(){public void run() {
            sc.move("left");
        }}.start();
    }
    public void stop(View view) {
        new Thread(){public void run() {
            sc.move("stop");
        }}.start();
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
        if(mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

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
                    try {
                        resultText = new Task1().execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("result",resultText);
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

}

class Task1 extends AsyncTask<String, Void, String> {

    private String str, receiveMsg;
    @Override
    protected String doInBackground(String... params) {
        URL url;
        try {
            url = new URL("http://"); // 마지막에는 / 넣지 말기

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");


            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.e("receiveMsg : ", receiveMsg);

                reader.close();
            }
            else if(conn.getResponseCode() == 404) {
                Log.e("Mytag","what");
            }
            else {
                Log.e("결과", conn.getResponseCode() + "Error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            Log.e("Mytag","내용: "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}
