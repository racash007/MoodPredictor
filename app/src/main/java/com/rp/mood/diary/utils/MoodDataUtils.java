package com.rp.mood.diary.utils;

import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.model.enums.MoodImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoodDataUtils {

    public static JSONObject getDriverFromMoodDriver(MoodDriver moodDriver) throws JSONException {
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

    public static List<Mood> getMood(JSONObject jsonObject)  {
        List<Mood> moodList= new ArrayList<>();
        try{
        JSONArray moodJsonList = (JSONArray) jsonObject.get("mood");
        for (int i=0;i<moodJsonList.length();i++) {
            JSONArray moodData = moodJsonList.getJSONArray(i);
            moodList.add(populateMoodDto(moodData));
        }}
        catch (Exception e){
            e.printStackTrace();
        }
        return moodList;
    }

    private static Mood populateMoodDto(JSONArray moodData) throws Exception {
        Mood mood = new Mood();
        //mood.setMoodId(moodData.getLong(0));
        String moodType = moodData.getString(1);
        mood.setMood(MoodImage.valueOf(moodType.toUpperCase()).getMoodName());
        mood.setMoodDesc(moodData.getString(3));
        mood.setMoodTime(DateUtils.convertToDate(moodData.getString(2)));
        return mood;
    }
}
