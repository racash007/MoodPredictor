package com.rp.mood.diary.utility;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


import com.rp.mood.diary.R;
import com.rp.mood.diary.listener.CurrentLocationListener;
import com.rp.mood.diary.service.RestCallService;


import rx.Observable;

import static com.rp.mood.diary.utility.PerturbationHelper.applyJupiterPerturbation;
import static com.rp.mood.diary.utility.PerturbationHelper.applyMoonsPerturbation;
import static com.rp.mood.diary.utility.PerturbationHelper.applySaturnPerturbation;
import static com.rp.mood.diary.utility.Utility.getJulianDate;
import static com.rp.mood.diary.utility.Utility.getZodiacSignFromEclipticLongitude;
import static com.rp.mood.diary.utility.Utility.normalize;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class PlanetoryPositionCalculator {
    private static Calendar calendar = Calendar.getInstance();
    private static Calendar today;
    private static Date sunRiseTime = null;
    private static RestCallService restCallService;


    public static int calculateAscendant(Date moodTime, Location location) {

        calendar.setTime(moodTime);
        int sunPos = getSunPos(moodTime);
        Date sunRiseTime = getSunRiseTime(location);
        Calendar sunRiseCalender = Calendar.getInstance();
        sunRiseCalender.setTime(sunRiseTime);
        long durationGap = Utility.getTimeDiff(sunRiseCalender, calendar);
        long ascSignNumber = (sunPos + durationGap) % 12;
        return (int) ascSignNumber;
    }


    private static Date getSunRiseTime(Location location)
    {
        if(today!=null)
        {
            today.add(Calendar.DATE,1);
        }
        else {
            today = Calendar.getInstance();
        }
        if(today.after(Calendar.getInstance()) )
        {
            return sunRiseTime;
        }
        sunRiseTime = PlanetoryPositionCalculator.restCallService.getSunRiseTime(location);
        return sunRiseTime;
    }







    public static void setRestCallService(RestCallService restCallService) {
       PlanetoryPositionCalculator.restCallService = restCallService;
    }






    private static double getEclipticLongitude(double n, double i, double w, double a, double eccentricity, double meanAnomaly) {
        double eccentricAnomaly =  getEccentricAnomaly(eccentricity,meanAnomaly); //Eccentricity Anomaly
        double xv = a*(cos(Math.toRadians(eccentricAnomaly)) - eccentricity);
        double yv = a*(Math.sqrt(1.0 - eccentricity*eccentricity) * sin(toRadians(eccentricAnomaly)));
        double trueAnomaly = normalize(Math.toDegrees(Math.atan2(yv,xv)));
        double radii = sqrt(xv*xv + yv*yv);
        double xeclip = radii * ( cos(n) * cos(toRadians(normalize(trueAnomaly+w))) - sin(n) * sin(toRadians(normalize(trueAnomaly+w))) * cos(i) );
        double yeclip = radii * ( sin(n) * cos(toRadians(normalize(trueAnomaly+w))) + cos(n) * sin(toRadians(normalize(trueAnomaly+w))) * cos(i) );
        return normalize(toDegrees(atan2(yeclip,xeclip)));


    }

    



    private static double getEccentricAnomaly(double eccentricity, double meanAnomaly) {
        double e0 = meanAnomaly + eccentricity*(180/Math.PI * sin(toRadians(meanAnomaly)) *(1.0+eccentricity* cos(toRadians(meanAnomaly))));
        double e1=0;
        do {
            e0 = e1==0?e0:e1;
            e1 = e0 - ( e0 - eccentricity*(180/Math.PI) * sin(toRadians(e0)) - meanAnomaly ) / ( 1 - eccentricity * cos(toRadians(e0)) );
        }while (Math.abs(e1-e0)>0.005);
        return e1;
    }




    public static int getSunPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double w = 282.9404 + 4.70935E-5   * julianDate; //   (longitude of perihelion)
        double a = 1.000000;                              // (mean distance, a.u.)
        double eccentricity = 0.016709 - 1.151E-9             * julianDate;  //  (eccentricity)
        double meanAnomaly = normalize(356.0470 + 0.9856002585 * julianDate);    //(mean anomaly)
        double eclipticLongitude = getEclipticLongitude(0, 0, w, a, eccentricity, meanAnomaly);
        return getZodiacSignFromEclipticLongitude(eclipticLongitude).getSignNumber();
    }
    public static int getMoonPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double N = toRadians(125.1228 - 0.0529538083 * julianDate); //ascending Node Longitude
        double i = toRadians(5.1454); //inclination
        double w = 318.0634 + 0.1643573223 * julianDate; //argumentOfPerihelion
        double a = 60.2666; //semiMajorAxis
        double eccentricity = 0.054900; //Eccentricity
        double meanAnomaly = normalize(115.3654 + 13.0649929509 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        longitude = applyMoonsPerturbation(julianDate,longitude,meanAnomaly,N,N+w+meanAnomaly);

        return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();
    }


    public static int getMarPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double N = toRadians(49.5574 + 2.11081E-5   * julianDate); //ascending Node Longitude
        double i = toRadians(1.8497 - 1.78E-8  * julianDate); //inclination
        double w = 286.5016 + 2.92961E-5   * julianDate; //argumentOfPerihelion
        double a = 1.523688; //semiMajorAxis
        double eccentricity = 0.093405     + 2.516E-9   * julianDate; //Eccentricity
        double meanAnomaly = normalize(18.6021 + 0.5240207766 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();
    }


    public static int getSatPos(Date moodTime) {

        double julianDate = getJulianDate(moodTime);
        double N = toRadians(113.6634 + 2.38980E-5  * julianDate); //ascending Node Longitude
        double i = toRadians(2.4886 - 1.081E-7 * julianDate); //inclination
        double w = 339.3939 + 2.97661E-5 * julianDate; //argumentOfPerihelion
        double a = 9.55475; //semiMajorAxis
        double eccentricity = 0.055546     - 9.499E-9    * julianDate; //Eccentricity
        double meanAnomaly = normalize(316.9670 + 0.0334442282 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        longitude = applySaturnPerturbation(longitude,meanAnomaly,julianDate);
        return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();
    }

    public static int getMerPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double N = toRadians(48.3313 + 3.24587E-5  * julianDate); //ascending Node Longitude
        double i = toRadians(7.0047 + 5.00E-8 * julianDate); //inclination
        double w = 29.1241 + 1.01444E-5 * julianDate; //argumentOfPerihelion
        double a = 0.387098; //semiMajorAxis
        double eccentricity = 0.205635 + 5.59E-10 * julianDate; //Eccentricity
        double meanAnomaly = normalize(168.6562 + 4.0923344368 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();

    }

    public static int getJupPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double N = toRadians(100.4542 + 2.76854E-5  * julianDate); //ascending Node Longitude
        double i = toRadians(1.3030 - 1.557E-7  * julianDate); //inclination
        double w = 273.8777 + 1.64505E-5 * julianDate; //argumentOfPerihelion
        double a = 5.20256; //semiMajorAxis
        double eccentricity = 0.048498     + 4.469E-9  * julianDate; //Eccentricity
        double meanAnomaly = normalize(19.8950 + 0.0830853001 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        longitude = applyJupiterPerturbation(longitude,meanAnomaly,julianDate);
                return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();
    }



    public static int getVenPos(Date moodTime) {
        double julianDate = getJulianDate(moodTime);
        double N = toRadians(76.6799 + 2.46590E-5  * julianDate); //ascending Node Longitude
        double i = toRadians(3.3946 + 2.75E-8 * julianDate); //inclination
        double w = 54.8910 + 1.38374E-5 * julianDate; //argumentOfPerihelion
        double a = 0.723330; //semiMajorAxis
        double eccentricity = 0.006773 - 1.302E-9   * julianDate; //Eccentricity
        double meanAnomaly = normalize(48.0052 + 1.6021302244 * julianDate); //Mean Anomaly
        double longitude = getEclipticLongitude(N, i, w, a, eccentricity, meanAnomaly);
        return getZodiacSignFromEclipticLongitude(longitude).getSignNumber();
    }
}
