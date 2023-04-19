package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pro_choose extends AppCompatActivity {

    Button check_place;
    Button user_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_choose);

        check_place=  findViewById(R.id.check_place);
        user_view = findViewById(R.id.user_view);


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
}