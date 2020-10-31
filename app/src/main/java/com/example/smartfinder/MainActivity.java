package com.example.smartfinder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings1:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wjdtkdgns777/smartfinder"));
                startActivity(intent);

                return super.onOptionsItemSelected(item);

            case R.id.action_settings2:

                Intent intent2 = new Intent(getApplicationContext(), guideInfo.class);
                startActivity(intent2);


                return super.onOptionsItemSelected(item);


            case R.id.action_settings3:

                Intent intent3 = new Intent(getApplicationContext(), MyList.class);
                startActivity(intent3);


                return super.onOptionsItemSelected(item);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {


        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        Button Searchbutton = (Button) findViewById(R.id.Sbutton);

        Searchbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng mapCenter = naverMap.getCameraPosition().target;
            Reversegeo(mapCenter.latitude, mapCenter.longitude);



            }
        });


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }



    private void Reversegeo(double lat, double lng) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dapi.kakao.com").addConverterFactory(GsonConverterFactory.create()).build();
        KakaoApi kakaoApi = retrofit.create(KakaoApi.class);


        kakaoApi.getcitybygeo(lat, lng).enqueue(new Callback<KakaoResult>() {
            @Override
            public void onResponse(Call<KakaoResult> call, Response<KakaoResult> response) {
                if (response.code() == 200) {
                    KakaoResult result = response.body();


                    if(result.getDocuments().size()!=0){
                        Log.d((result.getDocuments().get(0).getAddress().getName()), "longitude");
                       String City = result.getDocuments().get(0).getAddress().getName();
                    fetchStoreSale(City);
                    }

                }
            }

            @Override
            public void onFailure(Call<KakaoResult> call, Throwable t) {

            }
        });
    }


    private void fetchStoreSale(String RELAX_SIDO_NM){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://211.237.50.150:7080/openapi/5010c6384bc1bf89ca9024762721a81118a3d59e00cce037a1b09b5732169e05/json/Grid_20200713000000000605_1/1/1000/").addConverterFactory(GsonConverterFactory.create()).build();
        AnsimApi ansimApi = retrofit.create(AnsimApi.class);
        Log.d(String.valueOf((RELAX_SIDO_NM)), "city");
        ansimApi.getStoresByGeo(RELAX_SIDO_NM).enqueue(new Callback<StoreSaleResult>() {
            @Override
            public void onResponse(Call<StoreSaleResult> call, Response<StoreSaleResult> response) {
                Log.d(String.valueOf(response.body()), "error2");
                if (response.code() == 200) {
                    StoreSaleResult result2 = response.body();
                    if(result2.Grid_20200713000000000605_1.getRow().size()!=0) {
                        Log.d(String.valueOf((result2.Grid_20200713000000000605_1.Anisim_total())), "total");
                        Log.d(String.valueOf((result2.Grid_20200713000000000605_1.getRow().get(0).getRname())), "total");
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreSaleResult> call, Throwable t) {

            }
        });
    }

}
