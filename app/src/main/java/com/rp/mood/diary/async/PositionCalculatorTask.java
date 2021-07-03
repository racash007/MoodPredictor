package com.rp.mood.diary.async;

import android.location.Location;

import androidx.annotation.NonNull;

import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.model.enums.Planet;
import com.rp.mood.diary.service.PlanetoryPositionCalculatorService;

import java.util.Map;

import javax.inject.Inject;


public class PositionCalculatorTask  {

    private Location location;
    private PlanetoryPositionCalculatorService positionCalculatorService;

    @Inject
    public PositionCalculatorTask(PlanetoryPositionCalculatorService positionCalculatorService) {
        this.positionCalculatorService = positionCalculatorService;
    }

    @NonNull
    public MoodDriver getMoodDriver(Mood mood) {

        MoodDriver moodDriver = new MoodDriver(mood);
        moodDriver.setAscendantSign(positionCalculatorService.calculateAscendant(mood.getMoodTime(),location));
        Map<Planet, Integer> planetaryPosition = positionCalculatorService.getPlanetaryPosition(mood.getMoodTime());
        moodDriver.setSunPosition(planetaryPosition.get(Planet.SUN));
        moodDriver.setMoonPosition(planetaryPosition.get(Planet.MOON));
        moodDriver.setMarPosition(planetaryPosition.get(Planet.MARS));
        moodDriver.setJupiterPosition(planetaryPosition.get(Planet.JUPITER));
        moodDriver.setMercuryPosition(planetaryPosition.get(Planet.MERCURY));
        moodDriver.setSaturnPosition(planetaryPosition.get(Planet.SATURN));
        moodDriver.setVenusPosition(planetaryPosition.get(Planet.VENUS));
        return moodDriver;
    }


    public void setLastLocation(Location lastKnowLocation) {
        this.location = lastKnowLocation;
    }
}
