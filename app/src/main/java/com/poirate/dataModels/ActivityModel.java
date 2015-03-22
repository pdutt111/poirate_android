package com.poirate.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pariskshitdutt on 08/02/15.
 */
public class ActivityModel {
    @SerializedName("_id")
    public String city_id;
    @SerializedName("activities")
    List<ActivityResp> activities=new ArrayList<>();
}
