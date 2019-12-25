package freewill.rakesh.com.howdoyoufeel.utility;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        sdf.applyPattern("dd-MM-yyyy HH:mm");
      /*  int moonPos = PlanetoryPositionCalculator.getMoonPos(sdf.parse("13-11-2019 14:10"));
        System.out.println(moonPos + " 2" );*/
     /*   int  moonPos = PlanetoryPositionCalculator.getMoonPos(sdf.parse("22-04-1990 12:00"));
        System.out.println(moonPos + " 10" );*/


       /* int merPos = PlanetoryPositionCalculator.getMerPos(sdf.parse("22-04-1990 12:00"));
        System.out.println(merPos);*/
        int merPos = PlanetoryPositionCalculator.getMerPos(sdf.parse("14-08-2019 00:37"));
        System.out.println(merPos);

    }
    @Test
    public void getUTTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd-MM-yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse("08-08-2019 09:58"));
       System.out.println(Utility.getUT(calendar));
    }
}