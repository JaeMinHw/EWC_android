package com.example.ewc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class pro_map extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    TextView textView;
    Marker marker = new Marker();
    double latitude ;
    double longitude;

    int map_type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_map);

        textView = findViewById(R.id.textView7);

        Button map_change = findViewById(R.id.button);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        place_Task2 pt = new place_Task2();
        pt.start();

        // 서버에 해당 사용자의 위치 요청 후 받기.
        map_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(map_type ==0) {
                    map_type =1;
                }
                else  {
                    map_type =0;
                }
                naverMapBasicSettings();
            }
        });
        naverMapBasicSettings();


    }

    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    public void onMapReady(@NonNull final NaverMap naverMap) {

        // 현재 위치 버튼 안보이게 설정
        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(false);

        if(map_type == 0) {
            naverMap.setMapType(NaverMap.MapType.Hybrid);
        }
        else {
            naverMap.setMapType(NaverMap.MapType.Basic);
        }

        // 지도 유형 위성사진으로 설정
        Marker marker = new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);

        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        new LatLng(latitude, longitude),15)
                .animate(CameraAnimation.Fly, 3000);

        naverMap.moveCamera(cameraUpdate);
        marker.setCaptionTextSize(16);
        marker.setCaptionText("현재 사용자의 위치입니다");
        marker.setCaptionRequestedWidth(200);
        marker.setCaptionColor(Color.BLUE);
        marker.setCaptionHaloColor(Color.rgb(200, 255, 200));

    }


    // 사용자 위치 전송 thread
    class place_Task2 extends Thread {

        private String str, receiveMsg;

        public void run() {
            try {
                URL url = new URL(server_link.link+"place/pro/"+login_info.id);
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

                        Log.e("plcae",responseData);


                        try{
                            JSONObject jarray = new JSONObject(responseData);

                            for(int i = 0; i< jarray.length(); i++)
                            {
                                latitude = jarray.getDouble("latitude");
                                longitude= jarray.getDouble("longitude");

                                Log.e("tt",""+latitude);


                            }
                        }catch (JSONException e){
                            e.printStackTrace();
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