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

public class SignUp extends AppCompatActivity {

    private EditText ID;
    private EditText PW;
    private EditText Phone;
    private Button signbtn;
    String id_value ;
    String pw_value;
    String phone_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ID = findViewById(R.id.userID);
        PW = findViewById(R.id.PW);
        Phone = findViewById(R.id.Phone_text);

        signbtn = findViewById(R.id.signup);

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSignUp();

            }
        });
    }


    //서버로 아이디 비밀번호 전화번호를 전송
    protected void checkSignUp() {
        id_value = ID.getText().toString();
        pw_value = PW.getText().toString();
        phone_value = Phone.getText().toString();
        //아이디 비밀번호 칸이 비어있지 않으면
        if(id_value.length() != 0 && pw_value.length() != 0 && phone_value.length() != 0) {
            Log.e("ID", id_value);
            Log.e("PW", pw_value);
            Log.e("Phone",phone_value);
            RequestThread rt = new RequestThread();
            rt.start();

            Toast.makeText(this, "successs", Toast.LENGTH_SHORT).show();
        }
        //아이디 및 비밀번호 칸이 비어있으면
        else {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    class RequestThread extends Thread {


        @Override
        public void run() {
            String IP = server_link.link + "signup/"+id_value+"/"+pw_value + "/"+phone_value;
            try {
                URL url = new URL(IP);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int resCode = conn.getResponseCode();
                    System.out.println("ttt");
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
                            Log.d("MainActivity", "서버에서 success 반환했습니다.");


                            // 사용자 화면으로 이동.

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        else {
                            // 다른 응답을 반환한 경우
                            // 로그인 실패
                            Toast.makeText(SignUp.this, "아이디와 비밀번호를 확인하여주세요", Toast.LENGTH_SHORT).show();
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