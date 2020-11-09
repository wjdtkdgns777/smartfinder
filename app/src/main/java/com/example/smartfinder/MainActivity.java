package com.example.smartfinder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.naver.maps.map.overlay.Overlay.*;


public class MainActivity extends AppCompatActivity implements NaverMap.OnMapClickListener, OnClickListener, OnMapReadyCallback{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private InfoWindow infoWindow;
    private List<Marker> markerList = new ArrayList<Marker>();
    public String URL;

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

        Button btn;

        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 웹페이지 열기
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(mIntent);
            }
        });



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




        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                KakaoResult2 result = (KakaoResult2) marker.getTag();
                View view = View.inflate(MainActivity.this, R.layout.view_info_window, null);
                ((TextView) view.findViewById(R.id.name)).setText(result.getDocuments2().get(0).getname());


                ((TextView) view.findViewById(R.id.stock)).setText(result.getDocuments2().get(0).category_name);

                ((TextView) view.findViewById(R.id.realname)).setText(result.getDocuments2().get(0).place_url);
                URL = result.getDocuments2().get(0).place_url;
                ((TextView) view.findViewById(R.id.time)).setText(result.getDocuments2().get(0).phone);
                return view;
            }



        });





        Button Searchbutton = (Button) findViewById(R.id.Sbutton);


        Searchbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetMarkerList();
                LatLng mapCenter = naverMap.getCameraPosition().target;
                Reversegeo(mapCenter.latitude, mapCenter.longitude, naverMap);


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


    private void Reversegeo(final double lat, final double lng, final NaverMap naverMap) {



        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dapi.kakao.com").addConverterFactory(GsonConverterFactory.create()).build();
        KakaoApi kakaoApi = retrofit.create(KakaoApi.class);


        kakaoApi.getcitybygeo(lat, lng).enqueue(new Callback<KakaoResult>() {
            @Override
            public void onResponse(Call<KakaoResult> call, Response<KakaoResult> response) {
                if (response.code() == 200) {
                    KakaoResult result = response.body();


                    if (result.getDocuments().size() != 0) {
                        Log.d((result.getDocuments().get(0).getAddress().getName()), "longitude");
                        String City = result.getDocuments().get(0).getAddress().getName();
                        fetchStoreSale(City, lat, lng,naverMap);
                    }

                }
            }

            @Override
            public void onFailure(Call<KakaoResult> call, Throwable t) {

            }
        });
    }


    private void fetchStoreSale(final String RELAX_SIDO_NM, final double lat, final double lng, final NaverMap naverMap) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://211.237.50.150:7080/openapi/5010c6384bc1bf89ca9024762721a81118a3d59e00cce037a1b09b5732169e05/json/Grid_20200713000000000605_1/1/1000/").addConverterFactory(GsonConverterFactory.create()).build();
        AnsimApi ansimApi = retrofit.create(AnsimApi.class);
        Log.d(String.valueOf((RELAX_SIDO_NM)), "city");
        ansimApi.getStoresByGeo(RELAX_SIDO_NM).enqueue(new Callback<StoreSaleResult>() {
            @Override
            public void onResponse(Call<StoreSaleResult> call, Response<StoreSaleResult> response) {
                Log.d(String.valueOf(response.body()), "error2");
                if (response.code() == 200) {
                    StoreSaleResult result2 = response.body();
                    if (result2.Grid_20200713000000000605_1.getRow().size() != 0) {
                        Log.d(String.valueOf((result2.Grid_20200713000000000605_1.Anisim_total())), "total");
                        Log.d(String.valueOf((result2.Grid_20200713000000000605_1.getRow().get(0).getRname())), "firstname");

                        updateMapMarkers(RELAX_SIDO_NM, result2, lat, lng,naverMap);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreSaleResult> call, Throwable t) {

            }
        });
    }


    private void updateMapMarkers(String RELAX_SIDO_NM, StoreSaleResult result, double lat, double lng,NaverMap naverMap) {

        if (result.Grid_20200713000000000605_1.getRow() != null && result.Grid_20200713000000000605_1.totalCnt > 0) {
            for (StoreSaleResult.Grid_20200713000000000605_1.row name : result.Grid_20200713000000000605_1.row) {

                fetchStoreSale2(RELAX_SIDO_NM, name.rname, lat, lng,naverMap);

            }
        }
    }


    private void fetchStoreSale2(String RELAX_SIDO_NM, final String name, final double y, final double x, final NaverMap naverMap) {


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dapi.kakao.com").addConverterFactory(GsonConverterFactory.create()).build();
        KakaoApi2 kakaoApi2 = retrofit.create(KakaoApi2.class);


        kakaoApi2.getname(RELAX_SIDO_NM +" "+ name, y, x, 10000).enqueue(new Callback<KakaoResult2>() {
            @Override
            public void onResponse(Call<KakaoResult2> call, Response<KakaoResult2> response) {
                if (response.code() == 200) {
                    KakaoResult2 result4 = response.body();


                    if (result4.getDocuments2().size() != 0) {

                        SetMapMarker(result4,naverMap);

                    }


                }
            }

            @Override
            public void onFailure(Call<KakaoResult2> call, Throwable t) {

            }
        });

    }

    private void SetMapMarker(KakaoResult2 result,NaverMap naverMap) {

        int foo = Integer.parseInt(result.getDocuments2().get(0).distance);



        if (foo < 2000) {
            Log.d(String.valueOf(result.meta.same_name.keyword), "keyword");
            Log.d(String.valueOf(result.getDocuments2().get(0).place_name), "name");
            Log.d(String.valueOf(result.getDocuments2().get(0).place_url), "url");
            Log.d(String.valueOf(foo), "reach");

            double lng = Double.parseDouble(result.getDocuments2().get(0).x);
            double lat = Double.parseDouble(result.getDocuments2().get(0).y);


            Marker marker = new Marker();
            marker.setTag(result);
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_baseline_local_dining_24));
            marker.setPosition(new LatLng(lat, lng));


            marker.setAnchor(new PointF(0.5f, 1.0f));
            marker.setMap(naverMap);

            marker.setOnClickListener(this);
            markerList.add(marker);

        }

    }
    @Override
        public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if (infoWindow.getMarker() != null) {
        infoWindow.close();
        }
        }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;

            if (marker.getInfoWindow() != null) {
                infoWindow.close();
            } else {
                if(infoWindow!=null) {
                    infoWindow.open(marker);

                }
            }
            return true;
        }
        return false;
    }



    private void resetMarkerList() {
        if (markerList != null && markerList.size() > 0) {
            for (Marker marker : markerList) {
                marker.setMap(null);
            }
            markerList.clear();
        }

    }


}







