package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProSignup extends AppCompatActivity {

    private EditText userid;
    private EditText proid;
    private EditText PW;
    private EditText phone;

    private TextView check_user;

    private Button signbtn;
    String check_id;

    private int check=0;

    String userid_value ;
    String proid_value;
    String pw_value ;
    String phone_value ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_signup);
        userid = findViewById(R.id.userID);
        proid = findViewById(R.id.proID);
        PW = findViewById(R.id.PW);
        phone = findViewById(R.id.Phone_text);
        check_user = findViewById(R.id.check_user_id);

        signbtn = findViewById(R.id.signup);

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkproSignUp();
            }
        });



        userid.addTextChangedListener(new TextWatcher() {
            int check = 0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //텍스트가 변경 될때마다 Call back
                // 그러면 변경 될때마다 서버로 아이디 전송하고 있는 아이디인지, 보호자 등록이 안된 사용자인지 확인 후 결과 반환
                // 만약 끝냈는데 없는 아이디이거나, 보호자 등록이 되어있는 사용자인 경우 다시 입력하게 시키기
                check_id = userid.getText().toString();

                id_check_Thread id = new id_check_Thread();
                id.start();


                if(check_id.length()==0){
                    check_user.setText("");
                }

                if(check == 0)  {
                    check_user.setText(getResources().getString(R.string.check_du));
                    check_user.setTextColor(getResources().getColor(R.color.error));
                }
                else if(check == 1) {
                    check_user.setText(getResources().getString(R.string.check_du1));
                    check_user.setTextColor(getResources().getColor(R.color.success));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                //텍스트 입력이 모두 끝았을때 Call back


            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //텍스트가 입력하기 전에 Call bac
                // 아무 동작하지 않는 함수
            }
        });
    }


    protected void checkproSignUp() {
        userid_value = userid.getText().toString();
        proid_value = proid.getText().toString();
        pw_value = PW.getText().toString();
        phone_value = phone.getText().toString();
        //아이디 비밀번호 칸이 비어있지 않으면
        if(userid_value.length() != 0 && proid_value.length() != 0 && pw_value.length() != 0 && phone_value.length() != 0 && check ==1) {
            Log.e("ID", userid_value);
            Log.e("ID", proid_value);
            Log.e("PW", pw_value);
            Log.e("Phone",phone_value);
            signup_Thread st = new signup_Thread();
            st.start();
        }
        //아이디 및 비밀번호 칸이 비어있으면
        else {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    class id_check_Thread extends Thread {

        String IP = server_link.link + "check/"+check_id;
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
                        Log.e("response result",responseData);

                        // 서버에 입력된 아이디를 보내고 조건에 맞는 아이디가 있으면 check는 1로 변환
                        if (responseData.equals("possible")) {
                            // 사용자 아이디 등록 안됨
                            Log.d("MainActivity", "서버에서 success 반환했습니다.");

                            check = 1;
                            check_user.setText(getResources().getString(R.string.check_du1));
                            check_user.setTextColor(getResources().getColor(R.color.success));
                        }
                        else {
                            Log.e("Main","don't do that");
                            check = 0;
                        }

                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class signup_Thread extends Thread {

        String IP = server_link.link + "prosignup/"+userid_value + "/"+ proid_value + "/" + pw_value + "/"+phone_value;
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
                        Log.e("response result",responseData);

                        // 서버에 입력된 아이디를 보내고 조건에 맞는 아이디가 있으면 check는 1로 변환
                        if (responseData.equals("success")) {
                            // 사용자 아이디 등록 안됨
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
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