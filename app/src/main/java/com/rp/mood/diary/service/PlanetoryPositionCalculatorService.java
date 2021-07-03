package com.rp.mood.diary.service;

import android.location.Location;

import com.rp.mood.diary.model.enums.Planet;

import java.util.Date;
import java.util.Map;

public interface PlanetoryPositionCalculatorService {
    int calculateAscendant(Date moodTime, Location location);

    Date getSunRiseTime(Location location);

    Map<Planet,Integer> getPlanetaryPosition(Date moodTime);
}
