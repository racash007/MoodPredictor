package com.rp.mood.diary;

import android.location.Location;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import com.rp.mood.diary.service.PlanetoryPositionCalculatorService;

public class CalculatorUtilsTest {
    private Location location;

    @Test
        public void calculateAscendant()
    {
        int calculateAscendant = PlanetoryPositionCalculatorService.calculateAscendant(new Date(), location);
        System.out.println(calculateAscendant);
        Assert.assertEquals(calculateAscendant,9);
    }
    @Test
    public void testGetMoonPos()
    {
        int moonPos = PlanetoryPositionCalculatorService.getMoonPos(Calendar.getInstance().getTime());
        System.out.println(moonPos);
    }
}
