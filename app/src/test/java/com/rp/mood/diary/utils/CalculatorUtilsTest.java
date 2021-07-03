package com.rp.mood.diary.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.rp.mood.diary.model.Mood;

public class CalculatorUtilsTest {

    @Test
    public void calculateAscendant() {
    }

    @Test
    public void getSunPos() {
    }

    @Test
    public void getMoonPos() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMM dd HH:mm:ss yyyy");
        Mood mood = new Mood();
       /* String []  times = {"Aug 06 11:08:58  2019",
        "Aug 06 12:50:08  2019",
                "Aug 06 19:32:06  2019",
                "Aug 07 01:21:23  2019",
                "Aug 07 10:31:08  2019",
                "Aug 07 12:26:29  2019",
                "Aug 07 13:32:55  2019",
                "Aug 07 20:13:31  2019",
                "Aug 08 10:16:30  2019",
                "Aug 08 10:16:42  2019",
                "Aug 08 20:34:59  2019",
                "Aug 08 22:26:20  2019",
                "Aug 08 23:55:51  2019",
                "Aug 09 00:30:24  2019",
                "Aug 09 11:12:27  2019",
                "Aug 09 12:31:29  2019",
                "Aug 09 20:33:20  2019",
                "Aug 10 10:21:01  2019",
                "Aug 10 16:56:44  2019",
                "Aug 11 19:53:23  2019",
                "Aug 11 19:55:35  2019",
                "Aug 11 19:59:20  2019",
                "Aug 11 20:09:37  2019",
                "Aug 11 21:10:52  2019",
                "Aug 11 21:54:03  2019",
                "Aug 12 00:10:15  2019",
                "Aug 12 00:29:31  2019",
                "Aug 12 02:29:57  2019",
                "Aug 12 09:39:36  2019",
                "Aug 12 11:41:37  2019",
                "Aug 12 13:41:51  2019",
                "Aug 12 16:58:51  2019",
                "Aug 12 19:45:13  2019",
                "Aug 12 20:32:25  2019",
                "Aug 12 22:32:12  2019",
                "Aug 13 00:42:12  2019",
                "Aug 13 01:26:53  2019",
                "Aug 13 09:54:11  2019",
                "Aug 13 13:53:54  2019",
                "Aug 13 16:10:41  2019",
                "Aug 13 18:19:29  2019",
                "Aug 13 20:54:04  2019",
                "Aug 13 21:28:11  2019",
                "Aug 13 21:33:49  2019",
                "Aug 14 00:03:56  2019",
                "Aug 14 02:05:27  2019",
                "Aug 14 06:11:02  2019",
                "Aug 14 06:32:06  2019",
                "Aug 14 07:39:23  2019",
                "Aug 14 08:07:38  2019",
                "Aug 14 10:11:02  2019",
                "Aug 14 12:40:35  2019",
                "Aug 14 14:48:55  2019",
                "Aug 14 16:03:42  2019",
                "Aug 14 18:13:15  2019",
                "Aug 14 20:17:21  2019",
                "Aug 14 22:42:44  2019",
                "Aug 14 22:57:51  2019",
                "Aug 15 03:49:18  2019",
                "Aug 15 11:14:38  2019",
                "Aug 15 14:26:23  2019",
                "Aug 15 16:36:32  2019",
                "Aug 15 18:37:53  2019",
                "Aug 15 20:39:01  2019",
                "Aug 16 00:50:38  2019",
                "Aug 16 02:10:42  2019",
                "Aug 16 06:09:52  2019",
                "Aug 16 08:15:10  2019",
                "Aug 16 10:49:52  2019",
                "Aug 16 11:00:57  2019",
                "Aug 16 15:10:29  2019",
                "Aug 16 17:22:07  2019",
                "Aug 16 17:44:36  2019",
                "Aug 16 21:45:35  2019",
                "Aug 17 00:17:35  2019",
                "Aug 17 08:19:39  2019",
                "Aug 17 10:23:56  2019",
                "Aug 17 12:35:58  2019",
                "Aug 18 19:58:56  2019",
                "Aug 18 20:04:42  2019",
                "Aug 18 20:34:23  2019",
                "Aug 18 22:44:15  2019",
                "Aug 19 09:02:15  2019",
                "Aug 19 11:10:07  2019",
                "Aug 19 15:14:32  2019",
                "Aug 19 17:33:37  2019",
                "Aug 19 18:30:12  2019",
                "Aug 19 19:35:24  2019",
                "Aug 19 20:17:05  2019",
                "Aug 20 00:42:28  2019",
                "Aug 20 09:10:29  2019",
                "Aug 20 11:23:39  2019",
                "Aug 20 17:07:33  2019",
                "Aug 20 17:29:33  2019",
                "Aug 20 18:51:32  2019",
                "Aug 20 20:43:24  2019",
                "Aug 20 23:08:58  2019",
                "Aug 21 02:25:52  2019",
                "Aug 21 10:23:59  2019",
                "Aug 21 11:17:49  2019",
                "Aug 21 14:07:04  2019",
                "Aug 21 18:30:02  2019",
                "Aug 21 22:39:44  2019",
                "Aug 22 00:49:23  2019",
                "Aug 22 11:20:53  2019",
                "Aug 22 13:25:46  2019",
                "Aug 22 17:30:27  2019",
                "Aug 22 21:32:16  2019",
                "Aug 22 23:43:53  2019",
                "Aug 23 01:58:32  2019",
                "Aug 23 10:25:03  2019",
                "Aug 23 12:43:35  2019",
                "Aug 23 14:24:10  2019",
                "Aug 23 18:33:34  2019",
                "Aug 23 23:11:56  2019",
                "Aug 23 23:44:23  2019",
                "Aug 24 14:40:27  2019",
                "Aug 24 14:40:37  2019",
                "Aug 24 15:02:51  2019",
                "Aug 26 13:10:36  2019",
                "Aug 26 17:20:43  2019",
                "Aug 26 21:20:58  2019",
                "Aug 26 23:42:05  2019",
                "Aug 27 07:42:55  2019",
                "Aug 27 10:38:08  2019",
                "Aug 27 12:38:01  2019",
                "Aug 27 19:12:40  2019",
                "Aug 27 21:21:25  2019",
                "Aug 27 23:36:58  2019",
                "Aug 28 11:39:12  2019",
                "Aug 28 14:03:07  2019",
                "Aug 28 18:03:27  2019",
                "Aug 28 21:06:43  2019",
                "Aug 29 01:09:36  2019",
                "Aug 29 09:07:23  2019",
                "Aug 29 15:46:05  2019",
                "Aug 30 00:00:10  2019",
                "Aug 30 06:32:13  2019",
                "Aug 30 09:21:29  2019",
                "Aug 30 11:27:46  2019",
                "Aug 30 14:51:18  2019",
                "Aug 30 15:33:40  2019",
                "Sep 04 01:55:11  2019",
                "Sep 04 09:39:47  2019",
                "Sep 04 11:42:30  2019",
                "Sep 04 15:43:15  2019",
                "Sep 04 19:45:03  2019",
                "Sep 05 08:43:56  2019",
                "Sep 05 11:02:57  2019",
                "Sep 05 19:08:28  2019",
                "Sep 09 23:22:18  2019",
                "Sep 10 11:47:14  2019",
                "Sep 10 11:48:51  2019",
                "Sep 10 18:27:00  2019",
                "Sep 10 22:53:00  2019",
                "Sep 11 07:34:01  2019",
                "Sep 11 11:39:17  2019",
                "Sep 17 21:05:28  2019",
                "Sep 17 23:08:06  2019",
                "Sep 18 16:07:59  2019",
                "Sep 18 20:37:56  2019",
                "Sep 19 02:38:14  2019",
                "Sep 19 10:19:32  2019",
                "Sep 19 14:23:47  2019",
                "Sep 19 15:57:03  2019",
                "Sep 19 18:28:54  2019",
                "Sep 20 01:10:06  2019",
                "Sep 20 17:14:48  2019",
                "Sep 20 22:17:16  2019",
                "Sep 21 00:17:23  2019",
                "Sep 21 08:55:11  2019",
                "Sep 21 15:45:54  2019",
                "Sep 21 16:30:25  2019",
                "Sep 21 21:52:05  2019",
                "Sep 23 11:55:28  2019",
                "Sep 23 13:56:03  2019",
                "Sep 23 19:59:19  2019",
                "Sep 24 06:13:05  2019",
                "Sep 29 04:20:12  2019",
                "Sep 29 04:27:17  2019",
                "Sep 29 07:10:42  2019",
                "Sep 29 10:14:00  2019",
                "Sep 29 10:46:31  2019",
                "Sep 29 14:47:03  2019",
                "Sep 29 21:32:12  2019",
                "Sep 30 11:33:21  2019",
                "Sep 30 13:35:36  2019",
                "Sep 30 16:21:39  2019",
                "Sep 30 19:28:59  2019",
                "Sep 30 22:00:14  2019",
                "Oct 01 00:15:11  2019",
                "Oct 01 00:41:46  2019",
                "Oct 01 12:50:32  2019",
                "Oct 01 17:59:20  2019",
                "Oct 10 11:37:04  2019",
                "Oct 10 13:41:01  2019",
                "Oct 10 13:41:38  2019",
                "Oct 10 18:01:06  2019",
                "Oct 12 23:57:39  2019",
                "Oct 13 10:35:09  2019",
                "Oct 14 17:55:38  2019",
                "Oct 14 20:00:41  2019",
                "Oct 14 22:18:53  2019",
                "Oct 15 19:00:34  2019",
                "Oct 15 19:00:38  2019",
                "Oct 16 19:35:44  2019",
                "Oct 18 17:06:09  2019",
                "Oct 28 18:05:42  2019",
                "Oct 31 16:50:19  2019",
                "Nov 03 11:51:55  2019",
                "Nov 03 13:11:10  2019",
                "Nov 09 15:41:34  2019"};*/
        String [] times={
                "Jan 02 06:20:00 2020",
                "Jan 02 08:20:00 2020",
                "Jan 02 10:20:00 2020",
                "Jan 02 12:20:00 2020",
                "Jan 02 14:20:00 2020",
                "Jan 02 18:20:00 2020",
                "Jan 02 20:20:00 2020",
                "Jan 02 22:20:00 2020",
                "Jan 03 00:20:00 2020",
                "Jan 03 06:20:00 2020",
                "Jan 03 08:20:00 2020",
                "Jan 03 10:20:00 2020",
                "Jan 03 12:20:00 2020",
                "Jan 03 14:20:00 2020",
                "Jan 03 18:20:00 2020",
                "Jan 03 20:20:00 2020",
                "Jan 03 22:20:00 2020",
                "Jan 04 00:20:00 2020",
                "Jan 04 06:20:00 2020",
                "Jan 04 08:20:00 2020",
                "Jan 04 10:20:00 2020",
                "Jan 04 12:20:00 2020",
                "Jan 04 14:20:00 2020",
                "Jan 04 18:20:00 2020",
                "Jan 04 20:20:00 2020",
                "Jan 04 22:20:00 2020",
                "Jan 05 00:20:00 2020",
                "Jan 05 06:20:00 2020",
                "Jan 05 08:20:00 2020",
                "Jan 05 10:20:00 2020",
                "Jan 05 12:20:00 2020",
                "Jan 05 14:20:00 2020",
                "Jan 05 18:20:00 2020",
                "Jan 05 20:20:00 2020",
                "Jan 05 22:20:00 2020",
                "Jan 06 00:20:00 2020"};
        for(String time :times)
        {
            Date now = sdf.parse(time);
            mood.setMoodTime(now);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            int sun =  PlanetoryPositionCalculatorService.getSunPos(mood.getMoodTime());
            Calendar sunRiseCalender = Calendar.getInstance();
            sunRiseCalender.setTime(now);
            sunRiseCalender.set(Calendar.HOUR_OF_DAY, 6);
            sunRiseCalender.set(Calendar.MINUTE, 20);
            long durationGap = DateUtils.getTimeDiff(sunRiseCalender, calendar);
            int asc = (int)(sun + durationGap) % 12;
            int moon = PlanetoryPositionCalculatorService.getMoonPos(mood.getMoodTime());
            int mar = PlanetoryPositionCalculatorService.getMarPos(mood.getMoodTime());
            int jup = PlanetoryPositionCalculatorService.getJupPos(mood.getMoodTime());
            int mer = PlanetoryPositionCalculatorService.getMerPos(mood.getMoodTime());
            int sat = PlanetoryPositionCalculatorService.getSatPos(mood.getMoodTime());
            int ven = PlanetoryPositionCalculatorService.getVenPos(mood.getMoodTime());
            System.out.println(asc+","+jup+","+mar+","+mer+","+moon+","+sat+","+sun+","+ven);
        }


    }
    @Test
    public void getUTTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd-MM-yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse("08-08-2019 09:58"));
       System.out.println(DateUtils.getUT(calendar));
    }
}