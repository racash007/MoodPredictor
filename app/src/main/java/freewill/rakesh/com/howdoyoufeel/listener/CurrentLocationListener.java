package freewill.rakesh.com.howdoyoufeel.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class CurrentLocationListener implements LocationListener {

    private Location lastKnowLocation = null;
    @Override
    public void onLocationChanged(Location location) {
        lastKnowLocation = location;
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
