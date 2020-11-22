package com.example.smartfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnsimApi {


    @GET(".")//안심식당 api, 레트로핏을 사용하기에 적함하지 않아 많은 고민을 했는데, 겟 부분에 .만 있으면 베이스 유알엘로 설정한 부분을 그대로 사용한다는것을 찾아내 사용
    Call<StoreSaleResult> getStoresByGeo(@Query("RELAX_SIDO_NM") String RELAX_SIDO_NM);//안심식당 api에 쿼리로 도시정보를 보내 검색하도록 함
}
