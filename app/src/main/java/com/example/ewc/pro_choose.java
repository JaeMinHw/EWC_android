package com.example.ewc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class pro_choose extends AppCompatActivity {

    Button check_place;
    Button user_view;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_choose);

        check_place = findViewById(R.id.check_place);
        user_view = findViewById(R.id.user_view);

        getToken();
        check_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_place();
            }
        });

        user_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_view();
            }
        });

    }

    public void check_place() {
        Intent intent = new Intent(getApplicationContext(), pro_map.class);
        startActivity(intent);
    }

    public void user_view() {
        Intent intent = new Intent(getApplicationContext(), pro_cam.class);
        startActivity(intent);
    }


    // 인텐트로 값 받아서 보호자 아이디에 맞는 테이블에 기기 토큰 값 가져와서 보호자 테이블에 넣어주기
    public void getToken(){
        //토큰값을 받아옵니다.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("error", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                         token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("user_token", token);
                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                        RequestThread rt = new RequestThread();
                        rt.start();

                    }

                });
    }

    // 토큰 값 데이터 베이스에 넣어주기
    class RequestThread extends Thread {

        String IP = server_link.link + "token/"+login_info.id+"/"+token;

        @Override
        public void run() {
            Log.e("IP",""+IP);
            try {
                URL url = new URL(IP);
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
                            // 서버에서 user 반환한 경우
                            // user 로그인 성공
                            Log.d("MainActivity", "서버에서 user 반환했습니다.");

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