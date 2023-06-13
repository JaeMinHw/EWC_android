package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class pro_cam extends AppCompatActivity {

    private Socket socket;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_cam);

        imageView = findViewById(R.id.videoView);

        // 사용자가 서버에 나 카메라 보여줘 하면 서버는 라즈베리에 너 카메라 사진 줘. 그럼 라즈베라파이는 서버에 사진을 주고 서버는 안드로이드에 사진을 주는 형식으로


        try {
            socket = IO.socket(server_link.link);
            socket.connect();
            socket.emit("start_video","start");
            socket.on("stream", onStreamReceived);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onStreamReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String base64Data = (String) args[0];
                    byte[] decodedString = Base64.decode(base64Data, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("end_video","end");
    }
}