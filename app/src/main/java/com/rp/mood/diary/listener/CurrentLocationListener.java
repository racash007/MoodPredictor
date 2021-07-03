package com.rp.mood.diary.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.rp.mood.diary.async.PositionCalculatorTask;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ServiceScoped;

public class CurrentLocationListener implements LocationListener {

    private final PositionCalculatorTask positionCalculatorTask;
    private Location lastKnowLocation = null;

    @Inject
    public CurrentLocationListener(PositionCalculatorTask positionCalculatorTask) {

        this.positionCalculatorTask = positionCalculatorTask;
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnowLocation = location;
        this.positionCalculatorTask.setLastLocation(lastKnowLocation);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLastKnowLocation() {
        return lastKnowLocation;
    }


}
