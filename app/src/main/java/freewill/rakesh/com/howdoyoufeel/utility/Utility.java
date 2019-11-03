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
}
