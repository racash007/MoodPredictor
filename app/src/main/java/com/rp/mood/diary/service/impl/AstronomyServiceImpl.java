package com.rp.mood.diary.service.impl;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.service.AstronomyService;
import com.rp.mood.diary.utils.DateUtils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class AstronomyServiceImpl implements AstronomyService {

    private final RequestQueue queue;
    private final Context context;
    private final String LOG_TAG = "Rest Call Service";
    private final String sun_rise_url;

    @Inject
    public AstronomyServiceImpl(@ApplicationContext Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.sun_rise_url = this.context.getResources().getString(R.string.sun_rise_api_url);    }

    @Override
    public Date getSunRiseTime(Location location) {
        Date sunRiseTime = null;

                    if (location == null) {

                        return getDefaultSunRiseTime();
                    }
            try{
                    RequestFuture<JSONObject> future = RequestFuture.newFuture();
                    JsonObjectRequest request = new JsonObjectRequest(sun_rise_url + "?lat=" + location.getLatitude() + "&lng=" + location.getLongitude(), null, future, future);
                    this.queue.add(request);
                    JSONObject response = future.get();
                    String sunRiseStr = (String) ((JSONObject) response.get("results")).get("sunrise");
                    Log.i(LOG_TAG, sunRiseStr);
                    sunRiseTime = DateUtils.getUTCdate(sunRiseStr);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error", e);
                return getDefaultSunRiseTime();
                }

        return sunRiseTime;
    }

    @NonNull
    private Date getDefaultSunRiseTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 6);
        instance.set(Calendar.MINUTE, 20);
        return instance.getTime();
    }
}

