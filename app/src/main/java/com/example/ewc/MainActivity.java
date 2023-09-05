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
//        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        Intent intent = new Intent(getApplicationContext(),test_view.class);
        startActivity(intent);
    }
    protected void proSignUp() {
        Intent intent = new Intent(getApplicationContext(), webview_video.class);
        startActivity(intent);
    }


    class RequestThread extends Thread {

        String IP = server_link.link + "login/"+id_value+"/"+pw_value;

        @Override
        public void run() {
            Log.e("IP1",""+IP);
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
                        if (responseData.equals("user")) {
                            // 서버에서 user 반환한 경우
                            // user 로그인 성공
                            Log.d("MainActivity", "서버에서 user 반환했습니다.");

                            login_info.id = id_value;


                            // 사용자 화면으로 이동.
                            Intent intent = new Intent(getApplicationContext(), User_controller.class);
                            startActivity(intent);

                        }
                        else if(responseData.equals("pro")) {
                            Log.d("MainActivity","서버에서 pro를 반환했습니다");
                            login_info.id = id_value;
                            // pro 화면으로
                            Intent intent = new Intent(getApplicationContext(), pro_choose.class);
                            startActivity(intent);
                        }
                        else {
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