package freewill.rakesh.com.howdoyoufeel.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriver;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriverDao;
import freewill.rakesh.com.howdoyoufeel.utility.PlanetoryPositionCalculator;

public class PositionCalculatorTask extends AsyncTask<Mood,Void,Void> {
    private final MoodDriverDao moodDriverDao;
    private final CurrentLocationListener currentLocationListener;
    private final Context context;
    private final RequestQueue queue;
    private static final  String LOG_TAG = "PositionCalculatorTask";
    private final View view;

    public PositionCalculatorTask(MoodDriverDao moodDriverDao, CurrentLocationListener currentLocationListener, Context context, View view) {
        this.moodDriverDao = moodDriverDao;
        this.currentLocationListener = currentLocationListener;
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.view = view;
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
        try {
            saveInApp(mood,moodDriver);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private synchronized String getUniqueID() {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = this.context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
    private void saveInApp(Mood mood, MoodDriver moodDriver) throws ExecutionException, InterruptedException, JSONException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("mood",new JSONObject(mood.toString()));
        jsonRequest.put("userID",this.getUniqueID());
        jsonRequest.put("driver",new JSONObject(moodDriver.toString()));
        JsonObjectRequest request = new JsonObjectRequest(this.context.getResources().getString(R.string.app_url)+"mood" ,jsonRequest , future, future);
        queue.add(request);
        JSONObject response = future.get();
        if("Success".equalsIgnoreCase(response.get("response").toString()))
        {
            Snackbar.make(this.view, "Mood saved Successfully" , Snackbar.LENGTH_LONG).show();
        }

    }
}
