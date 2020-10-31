package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnsimApi {


    @GET("/openapi/5010c6384bc1bf89ca9024762721a81118a3d59e00cce037a1b09b5732169e05/xml/Grid_20200713000000000605_1/1/1000")
    Call<KakaoResult> getStoresByGeo(@Query("RELAX_SIDO_NM") String RELAX_SIDO_NM);
}
