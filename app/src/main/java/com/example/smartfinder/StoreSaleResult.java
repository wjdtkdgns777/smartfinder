package com.example.smartfinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreSaleResult {
    //안심식당 api 파싱하기 위한 클래스
    //https://data.mafra.go.kr/opendata/data/indexOpenDataDetail.do?data_id=20200713000000001391&filter_ty=O&getBack=&sort_id=regist_dt&s_data_nm=&instt_id=201410120001&cl_code=&shareYn= 에서 추가 정보 확인가능


    @SerializedName("Grid_20200713000000000605_1")
    Grid_20200713000000000605_1 Grid_20200713000000000605_1;

    public class Grid_20200713000000000605_1 {
        @SerializedName("totalCnt")
        public int totalCnt;
        @SerializedName("startRow")
        public int startRow;
        @SerializedName("endRow")
        public int endRow;

        public int Anisim_total() {return totalCnt;}

        @SerializedName("result") result result;
        public class result{
            @SerializedName("message") public String message;
            @SerializedName("code") public String code;

        }


        public List<row> row = new ArrayList<>();

        public class row {
            @SerializedName("ROW_NUM") public int rname5;
            @SerializedName("RELAX_SEQ") public int rname1;
            @SerializedName("RELAX_ZIPCODE") public int rname2;
            @SerializedName("RELAX_SI_NM") public String rname3;
            @SerializedName("RELAX_SIDO_NM") public String rname4;
            @SerializedName("RELAX_RSTRNT_NM") public String rname;
            @SerializedName("RELAX_RSTRNT_REPRESENT") public String rname6;
            @SerializedName("RELAX_ADD1") public String rname7;
            @SerializedName("RELAX_ADD2") public String rname8;
            @SerializedName("RELAX_GUBUN") public String rname9;
            @SerializedName("RELAX_GUBUN_DETAIL") public String rname10;
            @SerializedName("RELAX_RSTRNT_TEL") public String rname11;
            @SerializedName("RELAX_USE_YN") public String rname12;
            @SerializedName("RELAX_RSTRNT_ETC") public String rname13;

            public String getRname() {return rname;}

        }
        public List<row> getRow() {return row;}


    }
}



