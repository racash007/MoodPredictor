package com.rp.mood.diary.service;

import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface MoodAppService {
    JSONObject save(Mood mood, MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException;

    JSONObject getPrediction(MoodDriver moodDriver) throws JSONException, ExecutionException, InterruptedException;

    JSONObject getMoodData(Date moodTime) throws ExecutionException, InterruptedException, JSONException, TimeoutException;
}
