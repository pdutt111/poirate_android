package com.poirate.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.poirate.dataModels.ActivityModel;
import com.poirate.dataModels.ActivityResp;
import com.poirate.dataModels.SearchModel;
import com.poirate.interfaces.ActivitiesResponse;
import com.poirate.interfaces.SearchResponse;
import com.poirate.network.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.poirate.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pariskshitdutt on 08/02/15.
 */
public class ServerCoordinator {
    final private String AUTHORITY="192.168.0.132:3000";
    final private String API_PATH="api/v1/";
    public void search(final String search, final SearchResponse resp) {
            AppController app = AppController.getInstance();
            app.cancelPendingRequests("search");
            Uri.Builder request = new Uri.Builder();
            request.scheme("http").encodedAuthority(AUTHORITY).appendEncodedPath(API_PATH).appendPath("city").appendQueryParameter("search", search);
// Request a string response from the provided URL.
            JSONObject params = new JSONObject();
            try {
                params.put("search", search);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("search query", search);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, request.build().toString(), params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            SearchModel searchModel = gson.fromJson(response.toString(), SearchModel.class);
                            resp.response(searchModel);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
// Add the request to the RequestQueue.
            req.setPriority(Request.Priority.IMMEDIATE);
            req.setShouldCache(true);
            app.addToRequestQueue(req, "search");
    }
    public void searchActivities(final String city_id, final ActivitiesResponse resp) {
        AppController app = AppController.getInstance();
        app.cancelPendingRequests("search");
        Uri.Builder request = new Uri.Builder();
        request.scheme("http").encodedAuthority(AUTHORITY).appendEncodedPath(API_PATH).appendPath("attractions").appendQueryParameter("city_id", city_id);
// Request a string response from the provided URL.
        JSONObject params = new JSONObject();
        try {
            params.put("city_id", city_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("search query", city_id);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, request.build().toString(), params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ActivityModel activityModel = gson.fromJson(response.toString(), ActivityModel.class);
                        resp.response(activityModel);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
// Add the request to the RequestQueue.
        req.setPriority(Request.Priority.IMMEDIATE);
        req.setShouldCache(true);
        app.addToRequestQueue(req, "search");
    }
}
