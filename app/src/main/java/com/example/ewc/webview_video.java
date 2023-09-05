package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class webview_video extends AppCompatActivity {

    private WebView webView;
    private Button button3;
    private Socket so;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_video);
        webView = (WebView) findViewById(R.id.webview);
        button3 = findViewById(R.id.button3);
        so = server_link.socket;
        try {
            so = IO.socket(server_link.link);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        so.connect();



        so.on("stream", new Emitter.Listener() {
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
//                                imageView.setImageBitmap(bitmap);
                                Log.e("test","tttt");
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


        //WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setLoadWithOverviewMode(true);

//wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);

//줌 설정 여부
        webView.getSettings().setSupportZoom(false);

//줌 확대/축소 버튼 여부
        webView.getSettings().setBuiltInZoomControls(true);

//자바스크립트 사용 여부
        webView.getSettings().setJavaScriptEnabled(true);

//자바스크립트가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//멀티윈도우 사용 여부
        webView.getSettings().setSupportMultipleWindows(true);
        webView.loadUrl(server_link.camera_link);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview_video.RequestThread rt = new webview_video.RequestThread();
                rt.start();

            }
        });

    }

    class RequestThread extends Thread {

        String IP = server_link.link + "call/"+login_info.id;

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
                        if (!responseData.equals("None")) {
                            // 전화번호 다이얼로 이동.
                            Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + responseData));
                            startActivity(tt);

                        }
                        else if(responseData.equals("pro")) {
                            Toast.makeText(getApplicationContext(), "보호자 번호가 없습니다.", Toast.LENGTH_SHORT).show();
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