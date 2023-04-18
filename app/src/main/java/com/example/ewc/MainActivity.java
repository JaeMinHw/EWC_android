package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button signup;
    private Button prosignup;
    private EditText id;
    private EditText pw;

    String id_value;
    String pw_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        prosignup=  findViewById(R.id.prosignup);
        id = findViewById(R.id.userID);
        pw = findViewById(R.id.PW);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

        prosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proSignUp();
            }
        });

    }

    protected void Login() {
        id_value = id.getText().toString();
        pw_value = pw.getText().toString();



        //아이디 비밀번호 칸이 비어있지 않으면
        if(id_value.length() != 0 && pw_value.length() != 0) {
            Log.e("ID", id_value);
            Log.e("PW", pw_value);
            RequestThread thread = new RequestThread();
            thread.start();
        }
        //아이디 및 비밀번호 칸이 비어있으면
        else {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }

    }
    protected void SignUp() {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
    protected void proSignUp() {
        Intent intent = new Intent(getApplicationContext(), ProSignup.class);
        startActivity(intent);
    }


    class RequestThread extends Thread {

//        String IP = "아이피 주소 + 포트 + / 서버에 전송하는 규칙" + "tt";
        String IPadd = "http://54.180.101.143:5001/";
        String IP = IPadd + "login/"+id_value+"/"+pw_value;
        @Override
        public void run() {
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
                            // 서버에서 success를 반환한 경우
                            // 로그인 성공
                            Log.d("MainActivity", "서버에서 success를 반환했습니다.");
                            // 사용자인지 보호자인지 확인 후 해당하는 화면으로 이동.
                            

                        } else {
                            // 다른 응답을 반환한 경우
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인하여주세요", Toast.LENGTH_SHORT).show();
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