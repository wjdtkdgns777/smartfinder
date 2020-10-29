package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KakaoApi {

        @Headers("Authorization: KakaoAK f9fd32cb730831b9a37dff23d7c3acb0")
        @GET("/v2/local/geo/coord2address.json")
        Call<cityResult> getcitybygeo(@Query("lat") double lat, @Query("lng") double lng);


}
