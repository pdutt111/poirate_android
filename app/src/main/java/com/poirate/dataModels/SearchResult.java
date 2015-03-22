package com.poirate.dataModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pariskshitdutt on 08/02/15.
 */
public class SearchResult{
    @SerializedName("_id")
    public String _id;

    @SerializedName("state")
    public String state;

    @SerializedName("name")
    public String name;
}
