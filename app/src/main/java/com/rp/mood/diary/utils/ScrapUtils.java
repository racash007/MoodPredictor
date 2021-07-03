package com.rp.mood.diary.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rp.mood.diary.model.MoodDriver;
import com.rp.mood.diary.model.enums.RisingSign;

public class ScrapUtils {
    public static MoodDriver populateDriver(MoodDriver moodDriver)  {
        Map<String,String> planetPosMap = new HashMap<>();
        try {
            String url = "https://www.drikpanchang.com/planet/position/planetary-positions-sidereal.html";
            Document document = Jsoup.connect(url).get();
            document.select("div[class=dpPlanetCardContent dpRow dpFlex]");
            Elements elements = document.select("div[class=dpPlanetCardContent dpRow dpFlex]");

            for(Element element : elements)
            {
                planetPosMap.put(element.select("div[class=dpPlanetCell]").first().html(),element.select("div[class=dpLongitudeCell dpFlexEqual]").first().select("strong").first().html());
            }

        }catch (IOException e)
        {}
        return populateMoodDriver(planetPosMap,moodDriver);
    }

    private static MoodDriver populateMoodDriver(Map<String, String> planetPosMap, MoodDriver moodDriver) {
        moodDriver.setAscendantSign(getSignNumber(planetPosMap.get("Asc")));
        moodDriver.setSunPosition(getSignNumber(planetPosMap.get("Sun")));
        moodDriver.setMoonPosition(getSignNumber(planetPosMap.get("Moon")));
        moodDriver.setRahuPosition(getSignNumber(planetPosMap.get("Rahu")));
        moodDriver.setKetuPosition(getSignNumber(planetPosMap.get("Ketu")));
        moodDriver.setJupiterPosition(getSignNumber(planetPosMap.get("Jup")));
        moodDriver.setSaturnPosition(getSignNumber(planetPosMap.get("Sat")));
        moodDriver.setVenusPosition(getSignNumber(planetPosMap.get("Ven")));
        moodDriver.setMercuryPosition(getSignNumber(planetPosMap.get("Mer")));
        moodDriver.setMarPosition(getSignNumber(planetPosMap.get("Mar")));
        return moodDriver;
    }

    private static int getSignNumber(String sign) {
        switch(sign)
        {
            case "Ari":
                return RisingSign.Aries.getSignNumber();
            case "Lib":
                return RisingSign.Libra.getSignNumber();
            case "Cap":
                return RisingSign.Capricorn.getSignNumber();
            case "Vir":
                return RisingSign.Virgo.getSignNumber();
            case "Sco":
                return RisingSign.Scorpio.getSignNumber();
            case "Sag":
                return RisingSign.Sagittarius.getSignNumber();
            case "Gem":
                return RisingSign.Gemini.getSignNumber();
            case "Aqu":
                return RisingSign.Aquarius.getSignNumber();
            case "Can":
                return RisingSign.Cancer.getSignNumber();
            case "Leo":
                return RisingSign.Leo.getSignNumber();
            case "Pis":
                return RisingSign.Pisces.getSignNumber();
            case "Tau":
                return RisingSign.Taurus.getSignNumber();
            default:
                return RisingSign.NoSign.getSignNumber();

        }
    }
}
