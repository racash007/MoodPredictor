package com.rp.mood.diary.service;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.rp.mood.diary.R;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ApplicationService {

    private final RequestQueue queue;
    private final UniqueIdentifierService uniqueIdentifierService;
    private final Context context;
    private final String LOG_TAG = "Persistence Service";
    public ApplicationService(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.uniqueIdentifierService = new UniqueIdentifierService(this.context);
    }

    public JSONObject persistMoodInApp(Mood mood, MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("mood", new JSONObject(mood.toString()));
            jsonRequest.put("userID", this.uniqueIdentifierService.getUniqueID());
            jsonRequest.put("driver", new JSONObject(moodDriver.toString()));
            JsonObjectRequest request = new JsonObjectRequest(this.context.getResources().getString(R.string.app_url) + "mood", jsonRequest, future, future);
            queue.add(request);
            return future.get();

    }

    public JSONObject getPrediction(MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException {

            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = this.context.getResources().getString(R.string.app_url) + "prediction";
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("userID", this.uniqueIdentifierService.getUniqueID());
            jsonRequest.put("driver", new JSONObject(moodDriver.toString()));
            JsonObjectRequest request = new JsonObjectRequest(url, jsonRequest, future, future);
            queue.add(request);
            return future.get();


        }
    }

