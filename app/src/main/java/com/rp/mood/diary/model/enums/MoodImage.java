package com.rp.mood.diary.model.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rp.mood.diary.R;

public enum MoodImage {
    HAPPY("Happy",R.drawable.happy,R.string.happy_msg),
    EXCITED("Excited",R.drawable.excited,R.string.exicted_msg),
    NAUGHTY("Naughty",R.drawable.naughty,R.string.naughty_msg),
    ANGRY("Angry", R.drawable.angry,R.string.angry_msg),
    WORRIED("Worried",R.drawable.worried,R.string.tensed_msg),
    DEPRESSED("Depressed",R.drawable.depressed,R.string.scared_msg);


    public static final Map<String, Integer> moodMap = Collections.unmodifiableMap(initializeMapping());
    public static final Map<String, Integer> moodStatsMap = Collections.unmodifiableMap(initializeStatsMapping());
    public static final Map<String, Integer> moodMsgMap = Collections.unmodifiableMap(initializeMsgMapping());



    private final String moodName;
    private final int moodImage;
    private final int moodMsg;


    public String getMoodName() {
        return moodName;
    }

    public int getMoodImage() {
        return moodImage;
    }

    public int getMoodMsg() {
        return moodMsg;
    }

    MoodImage(String moodName, int moodImage, int moodMsg)
{
    this.moodName = moodName;
    this.moodImage = moodImage;
    this.moodMsg = moodMsg;
}

    private static Map<String, Integer> initializeMapping() {
        Map<String, Integer> mMap = new HashMap<String, Integer>();
        for (MoodImage s : MoodImage.values()) {
            mMap.put(s.getMoodName(), s.getMoodImage());
        }
        return mMap;
    }

    private static  Map<String, Integer>  initializeMsgMapping() {
        Map<String, Integer> mMap = new HashMap<String, Integer>();
        for (MoodImage s : MoodImage.values()) {
            mMap.put(s.getMoodName(), s.getMoodMsg());
        }
        return mMap;
    }

    private static Map<String, Integer> initializeStatsMapping() {
        Map<String, Integer> mMap = new HashMap<String, Integer>();
        int counter =0;
        for (MoodImage s : MoodImage.values()) {
            mMap.put(s.getMoodName(), counter++);
        }
        return mMap;
    }
}
