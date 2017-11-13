package com.example.koopc.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager lm;
    private LocationListener ll;
    private Context mContext;

    //좌표값 설정
    private LatLng getMju2 = new LatLng(37.222270, 127.185000); // 명지대 제 2 공학관에 대한 좌표값
    private LatLng getMju5 = new LatLng(37.222275,127.18629199999998); // 명지대 제 5 공학관에 대한 좌표값
    private LatLng gpsMe = new LatLng(0,0); // 내 위치에 대한 마커
    private LatLngBounds Bounds = new LatLngBounds(
            new LatLng(37.527089, 127.028480), new LatLng(38,128));

    //마커 설정
    private Marker mju2; // 명지대 제 2 공학관 위치 마커
    private Marker mju5; // 명지대 제 5 공학관 위치 마커
    private Marker mePosition; // 나의 위치 마커

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = getApplicationContext();
        // 안드로이드 버젼 확인
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkCallingOrSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED) ;
                else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        //로케이션 서비스 받고 GPS 혹은 AP 포인트에서 수신할 것인지 확인. 바뀌는 시간은 1초, 1M 주기로 수신.
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new MyLocationListner();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, ll);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, ll);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // 맵이 제거되었을 경우 마커의 업데이트를 중단한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkCallingOrSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED) ;
                else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(ll);
    }


    //
    private class MyLocationListner implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(mContext, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            gpsMe = new LatLng(latitude, longitude);

            mePosition.setPosition(gpsMe);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(gpsMe));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mapSetting;
        mapSetting = mMap.getUiSettings();
        mapSetting.setZoomControlsEnabled(true);
        mapSetting.setCompassEnabled(true);
        mapSetting.setIndoorLevelPickerEnabled(true);
        mapSetting.setMapToolbarEnabled(true);
        mapSetting.setScrollGesturesEnabled(true);
        mapSetting.setTiltGesturesEnabled(true);
        mapSetting.setRotateGesturesEnabled(true);
        mapSetting.setRotateGesturesEnabled(true);
        mapSetting.setMyLocationButtonEnabled(true);

        mju2 = mMap.addMarker(new MarkerOptions()
                .position(getMju2)
                .title("title: 제 2 공학관")
                .snippet("Snippet: 공학도의 건물"));
        mju5 = mMap.addMarker(new MarkerOptions()
                .position(getMju5)
                .title("title: 제 5 공학관")
                .snippet("Snippet: 공학도의 건물"));
        mePosition = mMap.addMarker(new MarkerOptions()
                .position(gpsMe)
                .title("title: 나")
                .snippet("Snippet: 내 자신의 위치"));
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getMju2,18));

    }

    @Override
    public boolean onMarkerClick(Marker marker) { // 내 위치가 마커 내로 들어왔을 경우.
        if (marker.getPosition().latitude - 0.00001 <= gpsMe.latitude && gpsMe.latitude <= marker.getPosition().latitude + 0.00001 &&
                marker.getPosition().longitude - 0.000001 <= gpsMe.longitude && gpsMe.longitude <= marker.getPosition().longitude + 0.001) {
            Intent intent = new Intent(this, PopUpActivity.class); // default 값이 아닌 활성화된 인텐트를 넘긴다. - 추후 구현 예정
            double position[] = new double[]{marker.getPosition().latitude, marker.getPosition().longitude};
            intent.putExtra("data",position); // 해당 액티비티로 좌표값 송신
            startActivity(intent);
        } else {
            // 그렇지 않을 경우 활성화되지 않은 인텐트를 넘긴다. - 미구현
            //Toast.makeText(mContext, "해당 좌표가 범위 내로 들어오지 않음", Toast.LENGTH_SHORT).show();
            String markerLatitude = Double.toString(marker.getPosition().latitude); // 해당 위도 좌표를 스트링으로 받기
            String markerLongitude = Double.toString(marker.getPosition().longitude); // 해당 경도 좌표를 스트링으로 받기

           Intent intent = new Intent(this, PopUpActivity.class); // default 값에 대한 intent 를 넘긴다.
            String position[] = new String[]{markerLatitude, markerLongitude};
            intent.putExtra("gps", position); // 해당 액티비티로 좌표값 송신
            startActivity(intent);
        }
        return true;
    }
    public void buttonClick(View view){
        Intent intent = new Intent(this, PopUpActivity.class);
        intent.putExtra("data","됐다.");
        startActivity(intent);
    }
}
