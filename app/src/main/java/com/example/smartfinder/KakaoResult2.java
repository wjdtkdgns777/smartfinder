package com.example.smartfinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class KakaoResult2 {
    //카카오 키워드 검색 파싱하는 부분, 아래 규격에 맞게 결과가 오면 클래스 이용해 받고 필요한 정보만 사용하는것
//https://developers.kakao.com/tool/rest-api/open/get/v2-local-search-keyword.%7Bformat%7D 여기서 결과값을 확인할수 있다
    @SerializedName("meta") Meta meta;
    public class Meta {
        @SerializedName("same_name") same_name same_name;

        public class same_name {
            @SerializedName("keyword") public String keyword;
        }

    }

    public List<documents> documents = new ArrayList<>();


    public class documents {


        @SerializedName("category_name") public String category_name;
        @SerializedName("phone") public String phone;
        @SerializedName("place_name") public String place_name;
        @SerializedName("x") public String x;
        @SerializedName("y") public String y;
        @SerializedName("place_url") public String place_url;
        @SerializedName("distance") public String distance;

        public String geturl() {return place_url;}
        public String getname() {return place_name;}

    }


    public List<documents> getDocuments2() {return documents;}

}
