package com.poirate.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pariskshitdutt on 08/02/15.
 */
public class ActivityResp {
    @SerializedName("_id")
    public String activity_id;

    @SerializedName("activity_name")
    public String name;

    @SerializedName("activity_time")
    public String time_required;

    @SerializedName("description")
    public String description;

    @SerializedName("score")
    public double score;

    @SerializedName("activity_visit_time_start")
    public String activity_visit_time_start;

    @SerializedName("activity_visit_time_end")
    public String activity_visit_time_end;

    @SerializedName("activity_type")
    public String activity_type;
    @SerializedName("loc")
    public double[] loc;
    @SerializedName("foursquare_checkins")
    public int foursquare_checkins;
    @SerializedName("photos")
    public List<photos> photos;


    public class photos{

        @SerializedName("photo_id")
        public String photo_id;


        @SerializedName("api_name")
        public String api_name;

        @SerializedName("image_url")
        public String image_url;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("user_profile_url")
        public String user_profile_url;

    }

}
