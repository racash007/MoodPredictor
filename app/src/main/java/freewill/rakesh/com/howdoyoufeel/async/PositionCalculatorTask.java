package freewill.rakesh.com.howdoyoufeel.async;

import android.content.Context;
import android.os.AsyncTask;

import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriver;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriverDao;
import freewill.rakesh.com.howdoyoufeel.utility.PlanetoryPositionCalculator;

public class PositionCalculatorTask extends AsyncTask<Mood,Void,Void> {
    private final MoodDriverDao moodDriverDao;
    private final CurrentLocationListener currentLocationListener;
    private final Context context;

    public PositionCalculatorTask(MoodDriverDao moodDriverDao, CurrentLocationListener currentLocationListener, Context context) {
        this.moodDriverDao = moodDriverDao;
        this.currentLocationListener = currentLocationListener;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Mood... moods) {
        Mood mood = moods[0];
        MoodDriver moodDriver = new MoodDriver(mood);
        PlanetoryPositionCalculator.setContext(context);
         /*moodDriver = ScrapUtils.populateDriver(moodDriver);*/
       moodDriver.setAscendantSign(PlanetoryPositionCalculator.calculateAscendant(mood.getMoodTime(),currentLocationListener));
        moodDriver.setSunPosition(PlanetoryPositionCalculator.getSunPos(mood.getMoodTime()));
        moodDriver.setMoonPosition(PlanetoryPositionCalculator.getMoonPos(mood.getMoodTime()));
        moodDriver.setMarPosition(PlanetoryPositionCalculator.getMarPos(mood.getMoodTime()));
        moodDriver.setJupiterPosition(PlanetoryPositionCalculator.getJupPos(mood.getMoodTime()));
        moodDriver.setMercuryPosition(PlanetoryPositionCalculator.getMerPos(mood.getMoodTime()));
        moodDriver.setSaturnPosition(PlanetoryPositionCalculator.getSatPos(mood.getMoodTime()));
        moodDriver.setVenusPosition(PlanetoryPositionCalculator.getVenPos(mood.getMoodTime()));
        moodDriverDao.save(moodDriver);
        return null;
    }
}
