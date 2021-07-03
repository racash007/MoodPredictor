package com.rp.mood.diary.service;

import android.location.Location;

import java.util.Date;

public interface AstronomyService {
    Date getSunRiseTime(Location location);
}
