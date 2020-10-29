package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnsimApi {

    @Headers(
            "Authorization: KakaoAK {REST_API_KEY}")
    @GET("/v2/local/geo/coord2address.json")
    Call<cityResult> getStoresByGeo(@Query("lat") double lat, @Query("lng") double lng, @Query("m") int m);
}
