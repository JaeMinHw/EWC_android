package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class pro_cam extends AppCompatActivity {

    private Socket socket;
    private ImageView imageView;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_cam);

        imageView = findViewById(R.id.videoView);
        button2 = findViewById(R.id.button2);

        // 사용자가 서버에 나 카메라 보여줘 하면 서버는 라즈베리에 너 카메라 사진 줘. 그럼 라즈베라파이는 서버에 사진을 주고 서버는 안드로이드에 사진을 주는 형식으로
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socket = IO.socket(server_link.link);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                socket.connect();
                socket.emit("start_video","start");
            }
        });

        try {

            socket.on("stream", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    Log.e("test","tttt");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.e("test","tttt");
                                String imageData = args[0].toString(); // 이미지 데이터를 바로 가져옴
                                byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                } else {
                                    Log.e("Image Decoding", "Bitmap is null");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("end_video","end");
        socket.disconnect();
    }
}