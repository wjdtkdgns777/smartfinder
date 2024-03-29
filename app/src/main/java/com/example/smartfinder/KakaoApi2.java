package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KakaoApi2 {

    @Headers("Authorization: KakaoAK f9fd32cb730831b9a37dff23d7c3acb0")//카카오 api 키
    @GET("/v2/local/search/keyword.json?page=1&size=15&sort=accuracy")//카카오 api 세부 url
    Call<KakaoResult2> getname(@Query("query") String query, @Query("y") double lat,  @Query("x") double lng,@Query("radius") int radius);//쿼리로 "광진구 결돈부리(예시)",lat,lng,반경값 보내줌

}
