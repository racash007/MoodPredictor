package com.rp.mood.diary.service.impl;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.service.IdentityService;
import com.rp.mood.diary.service.MoodAppService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static com.rp.mood.diary.utils.DataUtils.getDriverFromMoodDriver;

public class MoodAppServiceImpl implements MoodAppService {

    private final RequestQueue queue;
    private final IdentityService identityService;
    private final String app_url;

    @Inject
    public MoodAppServiceImpl(@ApplicationContext Context context, IdentityService identityService) {
            this.queue = Volley.newRequestQueue(context);
            this.identityService = identityService;
            this.app_url = context.getResources().getString(R.string.app_url);
    }


    @Override
    public JSONObject save(Mood mood, MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("mood", new JSONObject(mood.toString()));
        jsonRequest.put("userID", this.identityService.getUniqueID());
        jsonRequest.put("driver", new JSONObject(moodDriver.toString()));
        JsonObjectRequest request = new JsonObjectRequest(app_url + "mood", jsonRequest, future, future);
        queue.add(request);
        return future.get();

    }
    @Override
    public JSONObject getPrediction(MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("userId", this.identityService.getUniqueID());
        jsonRequest.put("driver", getDriverFromMoodDriver(moodDriver));
        JsonObjectRequest request = new JsonObjectRequest(app_url+"prediction", jsonRequest, future, future);
        queue.add(request);
        return future.get();


    }

    @Override
    public String fetch() throws ExecutionException, InterruptedException, TimeoutException {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(0,app_url + "mood", future, future);
        queue.add(request);
        return future.get(5, TimeUnit.SECONDS);
    }


}
