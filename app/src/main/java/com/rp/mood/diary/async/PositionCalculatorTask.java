package com.rp.mood.diary.async;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rp.mood.diary.listener.CurrentLocationListener;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.utility.PlanetoryPositionCalculator;


public class PositionCalculatorTask  {
    private final CurrentLocationListener currentLocationListener;
    private final Context context;

    public PositionCalculatorTask(CurrentLocationListener currentLocationListener, Context context) {
        this.currentLocationListener = currentLocationListener;
        this.context = context;

    }

    @NonNull
    public MoodDriver getMoodDriver(Mood mood) {
        MoodDriver moodDriver = new MoodDriver(mood);
        PlanetoryPositionCalculator.setContext(context);
        /*moodDriver = ScrapUtils.populateDriver(moodDriver);*/
        moodDriver.setAscendantSign(PlanetoryPositionCalculator.calculateAscendant(mood.getMoodTime(),currentLocationListener));
        moodDriver.setSunPosition(PlanetoryPositionCalculator.getSunPos(mood.getMoodTime()));
        moodDriver.setMoonPosition(PlanetoryPositionCalculator.getMoonPos(mood.getMoodTime()));
        moodDriver.setMarPosition(PlanetoryPositionCalculator.getMarPos(mood.getMoodTime()));
        moodDriver.setJupiterPosition(PlanetoryPositionCalculator.getJupPos(mood.getMoodTime()));
        moodDriver.setMercuryPosition(PlanetoryPositionCalculator.getMerPos(mood.getMoodTime()));
        moodDriver.setSaturnPosition(PlanetoryPositionCalculator.getSatPos(mood.getMoodTime()));
        moodDriver.setVenusPosition(PlanetoryPositionCalculator.getVenPos(mood.getMoodTime()));
        return moodDriver;
    }



}
