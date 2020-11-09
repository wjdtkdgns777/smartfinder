package com.example.smartfinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class KakaoResult2 {

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
