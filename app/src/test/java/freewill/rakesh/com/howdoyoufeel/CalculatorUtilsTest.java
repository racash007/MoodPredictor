package freewill.rakesh.com.howdoyoufeel;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.utility.PlanetoryPositionCalculator;

public class CalculatorUtilsTest {
    private CurrentLocationListener currentLocationListener ;

    @Test
        public void calculateAscendant()
    {
        int calculateAscendant = PlanetoryPositionCalculator.calculateAscendant(new Date(), currentLocationListener);
        System.out.println(calculateAscendant);
        Assert.assertEquals(calculateAscendant,9);
    }
    @Test
    public void testGetMoonPos()
    {
        int moonPos = PlanetoryPositionCalculator.getMoonPos(Calendar.getInstance().getTime());
        System.out.println(moonPos);
    }
}
