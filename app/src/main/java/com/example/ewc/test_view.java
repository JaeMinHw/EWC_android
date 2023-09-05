package com.example.ewc;



import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class test_view extends AppCompatActivity {
    private WebSocketClient webSocketClient;
    private URI serverUri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);

        imageView = findViewById(R.id.imageView);

        // WebSocket 서버 주소 설정
        serverUri = URI.create("ws://3.36.70.207:8765");

        // WebSocket 클라이언트 초기화 및 연결
        initWebSocketClient();
    }

    private void initWebSocketClient() {
        webSocketClient = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("WebSocket", "Connection opened");
            }

            @Override
            public void onMessage(String message) {
                // 서버로부터 메시지 수신 (이미지 수신 성공 여부 메시지 등)
                // 이미지 수신 메시지가 오면 이미지 업데이트를 위한 메서드를 호출
                handleReceivedImage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocket", "Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                Log.e("WebSocket", "Error: " + ex.getMessage());
            }
        };

        // WebSocket 연결 시작
        webSocketClient.connect();
    }

    private void handleReceivedImage(String base64Image) {
        // base64로 인코딩된 이미지를 디코딩하여 이미지 뷰에 표시
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        // UI 업데이트를 메인 스레드에서 실행
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 이미지 뷰에 새로운 이미지 설정
                imageView.setImageBitmap(decodedBitmap);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 종료될 때 WebSocket 연결을 닫음
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}