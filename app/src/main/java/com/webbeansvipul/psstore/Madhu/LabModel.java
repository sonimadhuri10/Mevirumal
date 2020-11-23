package com.webbeansvipul.psstore.Madhu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LabModel {

    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public ArrayList<labtestlist> testlist = new ArrayList<>();

    public static class labtestlist {

        @SerializedName("id")
        public String id;

        @SerializedName("lab_id")
        public String lab_id;

        @SerializedName("name")
        public String name;

        @SerializedName("speciality")
        public String speciality;


        @SerializedName("price")
        public String price;

        @SerializedName("details")
        public String details;

    }
}
