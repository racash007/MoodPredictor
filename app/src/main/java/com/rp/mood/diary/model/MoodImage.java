package com.rp.mood.diary.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rp.mood.diary.R;

public enum MoodImage {
    HAPPY("Happy",R.drawable.happy),
    TENSED("Tensed",R.drawable.tensed),
    ANGRY("Angry", R.drawable.angry),
    SCARED("Scared",R.drawable.scared),
    NAUGHTY("Naughty",R.drawable.naughty);

    public static final Map<String, Integer> moodMap = Collections.unmodifiableMap(initializeMapping());
    private final String moodName;
    private final int moodImage;


    public String getMoodName() {
        return moodName;
    }

    public int getMoodImage() {
        return moodImage;
    }

    MoodImage(String moodName, int moodImage)
{
    this.moodName = moodName;
    this.moodImage = moodImage;
}

    private static Map<String, Integer> initializeMapping() {
        Map<String, Integer> mMap = new HashMap<String, Integer>();
        for (MoodImage s : MoodImage.values()) {
            mMap.put(s.getMoodName(), s.getMoodImage());
        }
        return mMap;
    }
}
