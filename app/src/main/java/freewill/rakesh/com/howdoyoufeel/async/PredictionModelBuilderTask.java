package freewill.rakesh.com.howdoyoufeel.async;

import android.os.AsyncTask;

import java.util.List;

import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDao;

public class PredictionModelBuilderTask extends AsyncTask<Void,Void,Void> {
    private final MoodDao moodDao;

    public PredictionModelBuilderTask(MoodDao moodDao) {
        this.moodDao = moodDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Mood> moodList = this.moodDao.queryBuilder().list();

        return null;
    }
}
