package com.rp.mood.diary.async;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.rp.mood.diary.listener.CurrentLocationListener;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.service.RestCallService;
import com.rp.mood.diary.utility.PlanetoryPositionCalculator;


public class PositionCalculatorTask  {
    private Location location;
    private final RestCallService restCallService;

    public PositionCalculatorTask(RestCallService restCallService) {
        this.restCallService = restCallService;
    }

    @NonNull
    public MoodDriver getMoodDriver(Mood mood) {

        MoodDriver moodDriver = new MoodDriver(mood);
        PlanetoryPositionCalculator.setRestCallService(restCallService);
        moodDriver.setAscendantSign(PlanetoryPositionCalculator.calculateAscendant(mood.getMoodTime(),location));
        moodDriver.setSunPosition(PlanetoryPositionCalculator.getSunPos(mood.getMoodTime()));
        moodDriver.setMoonPosition(PlanetoryPositionCalculator.getMoonPos(mood.getMoodTime()));
        moodDriver.setMarPosition(PlanetoryPositionCalculator.getMarPos(mood.getMoodTime()));
        moodDriver.setJupiterPosition(PlanetoryPositionCalculator.getJupPos(mood.getMoodTime()));
        moodDriver.setMercuryPosition(PlanetoryPositionCalculator.getMerPos(mood.getMoodTime()));
        moodDriver.setSaturnPosition(PlanetoryPositionCalculator.getSatPos(mood.getMoodTime()));
        moodDriver.setVenusPosition(PlanetoryPositionCalculator.getVenPos(mood.getMoodTime()));
        return moodDriver;
    }


    public void setLastLocation(Location lastKnowLocation) {
        this.location = lastKnowLocation;
    }
}
