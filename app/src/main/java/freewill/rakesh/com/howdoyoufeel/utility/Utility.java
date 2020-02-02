package freewill.rakesh.com.howdoyoufeel.utility;

import android.content.Context;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.model.RisingSign;


public class Utility {

    private static GregorianCalendar calendar = new GregorianCalendar();
    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getFriendlyDayString(Context context, Date date) {

        Calendar current = Calendar.getInstance();

        calendar.setTime(date);


        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (current.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(formatId),today,getFormattedMonthDay(date));
        } else if ((calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR))&&( calendar.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR) + 7 )) {
            sdf.applyPattern("EEEE");
            return sdf.format(date);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            sdf.applyPattern("dd MMM");
            return sdf.format(date);
        }
    }


        public static String getFormattedMonthDay(Date date ) {
            sdf.applyPattern("dd MMM");
            return sdf.format(date);
    }

    public static String getFormattedTime(Date moodTime) {
        sdf.applyPattern("HH:mm:ss");
        return sdf.format(moodTime);
    }
    public static Date getUTCdate(String str) throws ParseException {
        sdf.applyPattern("HH:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return  sdf.parse(str);
    }

    public static long getTimeDiff(Calendar cal1, Calendar cal2) {
        long hourDiff = cal2.get(Calendar.HOUR_OF_DAY)-cal1.get(Calendar.HOUR_OF_DAY);
        long minDiff =  cal2.get(Calendar.MINUTE)-cal1.get(Calendar.MINUTE);
        long diff = hourDiff*60 + Math.abs(minDiff);
        return diff / 120;
    }
    public static double getJulianDate(Date moodTime) {
        calendar.setTime(moodTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int julianDate = 367 * year - 7 * (year + ((month + 9) / 12)) / 4 - 3 * ((year + (month - 9) / 7) / 100 + 1) / 4 + 275 * month / 9 + day - 730515;
        return  (julianDate + getUT(calendar) / 24.0);
    }
    public static double getUT(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) + (double)calendar.get(Calendar.MINUTE) / 60;
    }
    public static double normalize(double v) {
        return 360 - Math.abs(v)%360;
    }

    public static RisingSign getZodiacSignFromEclipticLongitude(double longitude)
    {
        if (longitude <  33.18) {
            return RisingSign.Pisces;
        } else if (longitude <  51.16) {
            return RisingSign.Aries;
        } else if (longitude <  93.44) {
            return RisingSign.Taurus;
        } else if (longitude < 119.48) {
            return RisingSign.Gemini;
        } else if (longitude < 135.30) {
            return RisingSign.Cancer;
        } else if (longitude < 173.34) {
            return RisingSign.Leo;
        } else if (longitude < 224.17) {
            return RisingSign.Virgo;
        } else if (longitude < 242.57) {
            return RisingSign.Libra;
        } else if (longitude < 271.26) {
            return RisingSign.Scorpio;
        } else if (longitude < 302.49) {
            return RisingSign.Sagittarius;
        } else if (longitude < 311.72) {
            return RisingSign.Capricorn;
        } else if (longitude < 348.58) {
            return RisingSign.Aquarius;
        } else {
            return RisingSign.Pisces;
        }

    }
}
