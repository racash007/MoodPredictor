package com.rp.mood.diary.utils;

import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DataUtils {

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

    public static List<Mood> getMood(String jsonObject) {
        String jsonObject1 = jsonObject;
        return null;
    }
}
