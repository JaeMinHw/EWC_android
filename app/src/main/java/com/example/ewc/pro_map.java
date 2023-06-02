package com.example.ewc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

        startLocationService();

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









    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;

                textView.setText(message);
                marker.setPosition(new LatLng(latitude, longitude));

//                naverMap.setLocationSource(mLocationSource);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Toast.makeText(getApplicationContext(),"내 위치 확인 요청항",Toast.LENGTH_SHORT).show();
        }catch(SecurityException e) {
            e.printStackTrace();
        }
    }



    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.e("tt",message);
        }

    }

}