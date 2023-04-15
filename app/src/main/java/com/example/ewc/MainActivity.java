package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button signup;
    private Button prosignup;
    private EditText id;
    private EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        prosignup=  findViewById(R.id.prosignup);
        id = findViewById(R.id.userID);
        pw = findViewById(R.id.PW);
    }

    protected void Login() {
        String id_value = id.getText().toString();
        String pw_value = pw.getText().toString();
        //아이디 비밀번호 칸이 비어있지 않으면
        if(id_value.length() != 0 && pw_value.length() != 0) {
            Log.e("ID", id_value);
            Log.e("PW", pw_value);
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

}