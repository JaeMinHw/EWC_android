package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private EditText ID;
    private EditText PW;
    private EditText Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ID = findViewById(R.id.userID);
        PW = findViewById(R.id.PW);
        Phone = findViewById(R.id.Phone_text);
    }


    //서버로 아이디 비밀번호 전화번호를 전송
    protected void checkSignUp() {
        String id_value = ID.getText().toString();
        String pw_value = PW.getText().toString();
        String phone_value = Phone.getText().toString();
        //아이디 비밀번호 칸이 비어있지 않으면
        if(id_value.length() != 0 && pw_value.length() != 0 && phone_value.length() != 0) {
            Log.e("ID", id_value);
            Log.e("PW", pw_value);
            Log.e("Phone",phone_value);
        }
        //아이디 및 비밀번호 칸이 비어있으면
        else {
            Toast.makeText(this, "ID와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

}