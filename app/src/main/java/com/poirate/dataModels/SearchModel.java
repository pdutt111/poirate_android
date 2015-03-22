package com.poirate.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pariskshitdutt on 08/02/15.
 */
public class SearchModel {


    @SerializedName("result")
    public List<SearchResult> results=new ArrayList<>();

}
