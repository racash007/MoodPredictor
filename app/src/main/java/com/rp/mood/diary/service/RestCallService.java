package com.rp.mood.diary.service;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RestCallService {

    private final RequestQueue queue;
    private final UniqueIdentifierService uniqueIdentifierService;
    private final Context context;
    private final String LOG_TAG = "Rest Call Service";
    private final String app_url;
    private final String sun_rise_url;
    public RestCallService(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.uniqueIdentifierService = new UniqueIdentifierService(this.context);
        this.app_url = this.context.getResources().getString(R.string.app_url);
        this.sun_rise_url = this.context.getResources().getString(R.string.sun_rise_api_url);    }

    public JSONObject persistMoodInApp(Mood mood, MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("mood", new JSONObject(mood.toString()));
            jsonRequest.put("userID", this.uniqueIdentifierService.getUniqueID());
            jsonRequest.put("driver", new JSONObject(moodDriver.toString()));
            JsonObjectRequest request = new JsonObjectRequest(app_url + "mood", jsonRequest, future, future);
            queue.add(request);
            return future.get();

    }

    public JSONObject getPrediction(MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {

            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("userId", this.uniqueIdentifierService.getUniqueID());
            jsonRequest.put("driver", getDriverFromMoodDriver(moodDriver));
            JsonObjectRequest request = new JsonObjectRequest(app_url+"prediction", jsonRequest, future, future);
            queue.add(request);
            return future.get();


        }

    private JSONObject getDriverFromMoodDriver(MoodDriver moodDriver) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ascendantSign",moodDriver.getAscendantSign());
        jsonObject.put("sunPosition",moodDriver.getSunPosition());
        jsonObject.put("moonPosition",moodDriver.getMoonPosition());
        jsonObject.put("marPosition",moodDriver.getMarPosition());
        jsonObject.put("saturnPosition",moodDriver.getSaturnPosition());
        jsonObject.put("rahuPosition",moodDriver.getRahuPosition());
        jsonObject.put("ketuPosition",moodDriver.getKetuPosition());
        jsonObject.put("jupiterPosition",moodDriver.getJupiterPosition());
        jsonObject.put("mercuryPosition",moodDriver.getMercuryPosition());
        jsonObject.put("venusPosition",moodDriver.getVenusPosition());
        return jsonObject;
    }



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
                    sunRiseTime = Utility.getUTCdate(sunRiseStr);
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

