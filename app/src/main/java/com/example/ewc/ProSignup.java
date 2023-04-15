package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class ProSignup extends AppCompatActivity {

    private EditText userid;
    private EditText proid;
    private EditText PW;
    private EditText phone;


    private int check = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_signup);
        userid = findViewById(R.id.userID);
        proid = findViewById(R.id.proID);
        PW = findViewById(R.id.PW);
        phone = findViewById(R.id.Phone_text);


        userid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //텍스트가 변경 될때마다 Call back
                // 그러면 변경 될때마다 서버로 아이디 전송하고 있는 아이디인지, 보호자 등록이 안된 사용자인지 확인 후 결과 반환

                String check_id = userid.getText().toString();
                Log.e("test",check_id);

                // 서버에 입력된 아이디를 보내고 조건에 맞는 아이디가 있으면 check는 1로 변환
                String user = "check";

                if(check_id == user) {
                    check = 1;
                }
                else {
                    check = 0;
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                //텍스트 입력이 모두 끝았을때 Call back
                // 만약 끝냈는데 없는 아이디이거나, 보호자 등록이 되어있는 사용자인 경우 다시 입력하게 시키기
                if(check == 1) {
                    Log.e("check","test");
                }
                else {
                    Log.e("check","try again");
                }
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
        String userid_value = userid.getText().toString();
        String proid_value = proid.getText().toString();
        String pw_value = PW.getText().toString();
        String phone_value = phone.getText().toString();
        //아이디 비밀번호 칸이 비어있지 않으면
        if(userid_value.length() != 0 && proid_value.length() != 0 && pw_value.length() != 0 && phone_value.length() != 0) {
            Log.e("ID", userid_value);
            Log.e("ID", proid_value);
            Log.e("PW", pw_value);
            Log.e("Phone",phone_value);
        }
        //아이디 및 비밀번호 칸이 비어있으면
        else {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}