package com.rp.mood.diary.service.impl;

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.service.IdentityService;
import com.rp.mood.diary.service.MoodAppService;
import com.rp.mood.diary.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static com.rp.mood.diary.utils.MoodDataUtils.getDriverFromMoodDriver;

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
    public JSONObject getMoodData(Date moodTime) throws ExecutionException, InterruptedException, TimeoutException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        Uri uri = new  Uri.Builder()
                        .encodedPath(app_url)
                        .appendPath("mood")
                        .appendQueryParameter("u",this.identityService.getUniqueID())
                        .appendQueryParameter("t",DateUtils.getFormattedDate(moodTime)).build();
        JsonObjectRequest request = new JsonObjectRequest(uri.toString(), null,future, future);
        queue.add(request);
        return future.get();
    }


}
