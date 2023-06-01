package com.example.ewc;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class send_control extends Thread{
    public int move(String movement) {
        String mUrl = "http://43.200.191.244:5000/brain_wave/"+movement;

        try {
            URL url = new URL(mUrl);
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
                    if (responseData.equals("success")) {
                        // 서버에서 success를 반환한 경우
                        // 로그인 성공
                        Log.d("MainActivity", "서버에서 success를 반환했습니다.");
                        // 사용자인지 보호자인지 확인 후 해당하는 화면으로 이동.
                        conn.disconnect();
                        return 1;
                    }
                    else {
                        Log.e("connect","error");
                    }


                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
