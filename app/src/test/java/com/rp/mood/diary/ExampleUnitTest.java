package com.rp.mood.diary;

import org.junit.Test;

import java.util.Date;

import com.rp.mood.diary.utils.DateUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testGetUTCDate() throws Exception {
        Date utcDate = DateUtils.getUTCdate("7:27:02 AM");
        long millis = utcDate.getTime() - new Date().getTime();
        int hours = (int) (millis/(1000 * 60 * 60));
        int mins = (int) ((millis/(1000*60)) % 60);

        String diff = hours + ":" + mins;
        System.out.println(diff);

    }
}