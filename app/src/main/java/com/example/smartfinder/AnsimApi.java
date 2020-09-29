package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnsimApi {

    @Headers("Accept: application/json")
    @GET("/corona19-masks/v1/storesByGeo/json")
    Call<Result> getStoresByGeo(@Query("lat") double lat, @Query("lng") double lng, @Query("m") int m);
}
