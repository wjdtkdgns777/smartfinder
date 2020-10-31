package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnsimApi {


    @GET(".")
    Call<StoreSaleResult> getStoresByGeo(@Query("RELAX_SIDO_NM") String RELAX_SIDO_NM);
}
