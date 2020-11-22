package com.example.smartfinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class KakaoResult {
    //카카오 리버스지오코딩 파싱하는 부분, 아래 규격에 맞게 결과가 오면 클래스 이용해 받고 필요한 정보만 사용하는것
//https://developers.kakao.com/tool/rest-api/open/get/v2-local-geo-coord2address.%7Bformat%7D 여기서 결과값을 확인할수 있다

    @SerializedName("meta")
    Meta meta;
    public class Meta {
        @SerializedName("total_count")
        public int total_count;

        public int getCount() {return total_count;}

    }


     public List<documents> documents = new ArrayList<>();


    public class documents {


            @SerializedName("address") address address;
            @SerializedName("road_address") road_address road_address;

            public class address{
                @SerializedName("address_name") public String address_name;
                @SerializedName("region_1depth_name") public String region_1depth_name;
                @SerializedName("region_2depth_name") public String region_2depth_name;
                @SerializedName("region_3depth_name") public String region_3depth_name;
                @SerializedName("mountain_yn") public String mountain_yn;
                @SerializedName("main_address_no") public String main_address_no;
                @SerializedName("sub_address_no") public String sub_address_no;
                public String getName() {return region_2depth_name;}
                public String getName1() {return region_1depth_name;}

            }

            public documents.address getAddress() {return address;}

            public class road_address{

                @SerializedName("address_name") public String address_name2;
                @SerializedName("region_1depth_name") public String region_1depth_name2;
                @SerializedName("region_2depth_name") public String region_2depth_name2;
                @SerializedName("region_3depth_name") public String region_3depth_name2;
                @SerializedName("road_name") public String mountain_yn2;
                @SerializedName("underground_yn") public String main_address_n2o;
                @SerializedName("main_building_no") public String sub_address_no2;
                @SerializedName("sub_building_no") public String mountain_yn3;
                @SerializedName("building_name") public String main_address_no4;
                @SerializedName("zone_no") public String sub_address_no5;

                public String getName3() {return address_name2;}
            }

             public documents.road_address getAddress2() {return road_address;}
        }


    public List<documents> getDocuments() {return documents;}


}
