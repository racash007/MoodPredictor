package freewill.rakesh.com.howdoyoufeel.utility;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.model.RisingSign;

public class CalculatorUtils {
    private static Calendar calendar = Calendar.getInstance();
    private static String url;
    private static Context context;
    private static RequestQueue queue;



    public static int calculateAscendant(Date moodTime, CurrentLocationListener currentLocationListener) {
        calendar.setTime(moodTime);
        RisingSign sunSign = getSunSign(moodTime);
        Date sunRiseTime = getSunRiseTime(currentLocationListener);
        Calendar sunRiseCalender = Calendar.getInstance();
        sunRiseCalender.setTime(sunRiseTime);
        long durationGap =  Utility.getTimeDiff(sunRiseCalender,calendar);
        long ascSignNumber = (sunSign.getSignNumber()+durationGap)%12;
        return (int) ascSignNumber;
    }




    private static Date getSunRiseTime(CurrentLocationListener currentLocationListener) {

        Date sunRiseTime = null;
        Location lastKnowLocation;

        int attempt = 0;
        try {

        do {
            Thread.sleep(5000);
            lastKnowLocation = currentLocationListener.getLastKnowLocation();
            attempt++;
        }
            while (attempt <3 && lastKnowLocation ==null);

        if(lastKnowLocation==null)
        {

            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.HOUR_OF_DAY,6);
            instance.set(Calendar.MINUTE,20);
            return instance.getTime();
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url+"?lat="+lastKnowLocation.getLatitude()+"&lng="+lastKnowLocation.getLongitude(), null, future, future);
        queue.add(request);


            JSONObject response = future.get();
            String sunRiseStr = (String)((JSONObject)response.get("results")).get("sunrise");
            Log.i("MDCAL",sunRiseStr);
            sunRiseTime = Utility.getUTCdate(sunRiseStr);
        } catch (Exception e) {
            Log.e("MDCAL","Error",e);
        }

        return sunRiseTime;
    }



    public static int getSunPos(Date moodTime) {
        return getSunSign(moodTime).getSignNumber();
        
    }

    private static RisingSign getSunSign(Date moodTime) {
        calendar.setTime(moodTime);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        for(RisingSign risingSign : RisingSign.values())
        {

            if ((risingSign.getStartMonth() == month && risingSign.getStartDay()<=day) || (risingSign.getEndMonth() == month && risingSign.getEndDay()>=day))
            {
                return risingSign;
            }
        }
        return RisingSign.NoSign;
    }

    public static void setContext(Context context) {
        if(CalculatorUtils.context==null) {
            CalculatorUtils.context = context;
            CalculatorUtils.url = CalculatorUtils.context.getResources().getString(R.string.sun_rise_api_url);
            CalculatorUtils.queue = Volley.newRequestQueue(CalculatorUtils.context);
        }
    }

    private static double normalize(double v) {
        v = v - Math.floor(v);
        if (v < 0) {
            v = v + 1;
        }
        return  v;
    }
    public static int getMoonPos(Date moodTime) {
         /*double age, // Moon"s age
          distance, // Moon"s distance in earth radii
          latitude; // Moon"s ecliptic latitude
         */
         double longitude; // Moon"s ecliptic longitude;
        RisingSign zodiac;
                // Moon"s zodiac sign
        /*        String  phase,// Moon"s phase
                        trajectory; // Moon"s trajectory*/
        double yy, mm, k1, k2, k3, jd;
        double ip, dp, np, rp;
        calendar.setTime(moodTime);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        yy = year - Math.floor((12 - month) / 10);
        mm = month + 9;
        if (mm >= 12) {
            mm = mm - 12;
        }

        k1 = Math.floor(365.25 * (yy + 4712));
        k2 = Math.floor(30.6 * mm + 0.5);
        k3 = Math.floor(Math.floor((yy / 100) + 49) * 0.75) - 38;

        jd = k1 + k2 + day + 59;  // for dates in Julian calendar
        if (jd > 2299160) {
            jd = jd - k3;      // for Gregorian calendar
        }

        ip = normalize((jd - 2451550.1) / 29.530588853);
        ip = ip * 2 * Math.PI;
        dp = 2 * Math.PI * normalize((jd - 2451562.2) / 27.55454988);
        rp = normalize((jd - 2451555.8) / 27.321582241);
        longitude = 360 * rp + 6.3 * Math.sin(dp) + 1.3 * Math.sin(2 * ip - dp) + 0.7 * Math.sin(2 * ip);

        if (longitude <  33.18) {
            zodiac = RisingSign.Pisces;
        } else if (longitude <  51.16) {
            zodiac = RisingSign.Aries;
        } else if (longitude <  93.44) {
            zodiac = RisingSign.Taurus;
        } else if (longitude < 119.48) {
            zodiac = RisingSign.Gemini;
        } else if (longitude < 135.30) {
            zodiac = RisingSign.Cancer;
        } else if (longitude < 173.34) {
            zodiac = RisingSign.Leo;
        } else if (longitude < 224.17) {
            zodiac = RisingSign.Virgo;
        } else if (longitude < 242.57) {
            zodiac = RisingSign.Libra;
        } else if (longitude < 271.26) {
            zodiac = RisingSign.Scorpio;
        } else if (longitude < 302.49) {
            zodiac = RisingSign.Sagittarius;
        } else if (longitude < 311.72) {
            zodiac = RisingSign.Capricorn;
        } else if (longitude < 348.58) {
            zodiac = RisingSign.Aquarius;
        } else {
            zodiac = RisingSign.Pisces;
        }

        return zodiac.getSignNumber();
  }






    public static int getRahuPos(Date moodTime) {
        return 0;
    }

    public static int getMarPos(Date moodTime) {
        return 0;
    }

    public static int getKetuPos(Date moodTime) {
        return 0;
    }

    public static int getSatPos(Date moodTime) {
        return 0;
    }

    public static int getMerPos(Date moodTime) {
        return 0;
    }

    public static int getJupPos(Date moodTime) {
        return 0;
    }

    public static int getVenPos(Date moodTime) {
        return 0;
    }
}
