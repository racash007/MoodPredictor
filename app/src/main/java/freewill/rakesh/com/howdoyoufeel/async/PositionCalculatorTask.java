package freewill.rakesh.com.howdoyoufeel.async;

import android.content.Context;
import android.os.AsyncTask;

import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriver;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriverDao;
import freewill.rakesh.com.howdoyoufeel.utility.CalculatorUtils;
import freewill.rakesh.com.howdoyoufeel.utility.ScrapUtils;

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
        CalculatorUtils.setContext(context);
         moodDriver = ScrapUtils.populateDriver(moodDriver);
       /* moodDriver.setAscendantSign(CalculatorUtils.calculateAscendant(mood.getMoodTime(),currentLocationListener));
        moodDriver.setSunPosition(CalculatorUtils.getSunPos(mood.getMoodTime()));
        moodDriver.setMoonPosition(CalculatorUtils.getMoonPos(mood.getMoodTime()));
        moodDriver.setRahuPosition(CalculatorUtils.getRahuPos(mood.getMoodTime()));
        moodDriver.setKetuPosition(CalculatorUtils.getKetuPos(mood.getMoodTime()));
        moodDriver.setMarPosition(CalculatorUtils.getMarPos(mood.getMoodTime()));
        moodDriver.setJupiterPosition(CalculatorUtils.getJupPos(mood.getMoodTime()));
        moodDriver.setMercuryPosition(CalculatorUtils.getMerPos(mood.getMoodTime()));
        moodDriver.setSaturnPosition(CalculatorUtils.getSatPos(mood.getMoodTime()));
        moodDriver.setVenusPosition(CalculatorUtils.getVenPos(mood.getMoodTime()));*/
        moodDriverDao.save(moodDriver);
        return null;
    }
}
