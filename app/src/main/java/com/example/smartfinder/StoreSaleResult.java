package com.example.smartfinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreSaleResult {


    @SerializedName("Grid_20200713000000000605_1")
    Grid_20200713000000000605_1 Grid_20200713000000000605_1;

    public class Grid_20200713000000000605_1 {
        @SerializedName("totalCnt")
        public int totalCnt;

        public int Anisim_total() {return totalCnt;}

        public List<row> row = new ArrayList<>();

        public class row {

            @SerializedName("RELAX_RSTRNT_NM") public String rname;



        }
    }
}



