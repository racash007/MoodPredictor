package com.rp.mood.diary.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.rp.mood.diary.R;
import com.rp.mood.diary.async.PositionCalculatorTask;
import com.rp.mood.diary.database.DaoHelper;
import com.rp.mood.diary.fragment.LoggerFragment;
import com.rp.mood.diary.fragment.PredictionFragment;
import com.rp.mood.diary.fragment.StatsFragment;
import com.rp.mood.diary.fragment.ViewerFragment;
import com.rp.mood.diary.listener.CurrentLocationListener;
import com.rp.mood.diary.listener.OnItemClickListener;
import com.rp.mood.diary.model.DaoSession;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.model.MoodDriverDao;
import com.rp.mood.diary.service.NotificationService;
import com.rp.mood.diary.service.RestCallService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MoodLogger extends AppCompatActivity implements OnItemClickListener {


    String selectedMood;
    DaoSession daoSession;
    MoodDao moodDao;
    MoodDriverDao moodDriverDao;
    PositionCalculatorTask positionCalculatorTask;
    CurrentLocationListener currentLocationListener;
    LocationManager locationManager;
    private Map<Integer, Fragment> selectedFragmentMapper;
    FloatingActionButton fab;
    RestCallService restCallService;
    private CompositeSubscription mCompositeSubscription =  new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intialize();
        setContentView(R.layout.activity_mood_logger);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.design_bottom_sheet);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragmentMapper.get(menuItem.getItemId())).commit();
                    return true;
                }
        );
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoggerFragment()).commit();
        NotificationService notificationService = new NotificationService(this);
        notificationService.createNotificationChannel();
        notificationService.setNotificationTimer();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener((vw) -> {
            processFabClick(vw);
        });


    }

    private void intialize() {
        daoSession = DaoHelper.getDaoSession(this);
        moodDao = daoSession.getMoodDao();
        moodDriverDao = daoSession.getMoodDriverDao();
        restCallService = new RestCallService(this);
        positionCalculatorTask = new PositionCalculatorTask(restCallService);
        currentLocationListener = new CurrentLocationListener(positionCalculatorTask);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 0, currentLocationListener);
        }
        selectedFragmentMapper = new HashMap<>();
        selectedFragmentMapper.put(R.id.nav_log, new LoggerFragment());
        selectedFragmentMapper.put(R.id.nav_read, new ViewerFragment());
        selectedFragmentMapper.put(R.id.nav_predict, new PredictionFragment());
        selectedFragmentMapper.put(R.id.nav_analyze, new StatsFragment());
    }

    public PositionCalculatorTask getPositionCalculatorTask()
    {
        return this.positionCalculatorTask;
    }


    private void saveMood(View view,String moodDesc)
    {
        Mood mood = new Mood(selectedMood, moodDesc);
        moodDao.insert(mood);
        mCompositeSubscription.add(Observable.defer(() -> Observable.just(positionCalculatorTask.getMoodDriver(mood)))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe((moodDriver) -> {
                    moodDriverDao.save(moodDriver);

                    try {
                        Observable<JSONObject> observable = Observable.just(restCallService.persistMoodInApp(mood, moodDriver));

                        observable.subscribe(
                                (response) -> {
                                    try {
                                        if ("Success".equalsIgnoreCase(response.get("response").toString())) {
                                            Snackbar.make(view, "Mood saved Successfully", Snackbar.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },(error)->{
                                        error.printStackTrace();
                                    }
                        );


                        observable.retryWhen(errors ->
                                errors
                                        .zipWith(Observable.range(1, 3), (n, i) -> i)
                                        .flatMap(retryCount -> Observable.timer((long) Math.pow(1, retryCount), TimeUnit.MINUTES))
                        );
                    } catch (JSONException | ExecutionException | InterruptedException  e) {
                        e.printStackTrace();
                    }

                }));
       }
    public void processFabClick(View view)
    {
        if(selectedMood==null)
        {
            Snackbar.make(view, "Please select a mood first", Snackbar.LENGTH_LONG).show();
        }else {
            showDescriptionAlert(view);
        }
    }
    public void showDescriptionAlert(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comment (Optional)");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSingleLine(false);  //add this
        input.setLines(4);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setHorizontalScrollBarEnabled(false);
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog,which)->{ saveMood(view,input.getText().toString());});
        builder.setNegativeButton("Cancel", (dialog,which)->{dialog.dismiss();});
        builder.show();
    }



    public boolean checkLocationPermission()
    {
        String finePermission = "android.permission.ACCESS_FINE_LOCATION";
        String coarsePermission = "android.permission.ACCESS_COARSE_LOCATION";
        String backGroundPermission = "android.permission.ACCESS_BACKGROUND_LOCATION";
        int resFinePermission = this.checkCallingOrSelfPermission(finePermission);
        int resCoarsePermission = this.checkCallingOrSelfPermission(coarsePermission);
        int resbackGroundPermission = this.checkCallingOrSelfPermission(coarsePermission);
        return (resFinePermission == PackageManager.PERMISSION_GRANTED || resbackGroundPermission == PackageManager.PERMISSION_GRANTED || resCoarsePermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mood_logger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_savedValue) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewerFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* public void deleteAll(View view)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Confirm dialog demo !");
        builder.setMessage("Do you really want to kill the mood?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog,which)->{
            daoSession.deleteAll(Mood.class);
            Toast.makeText(getApplicationContext(), "All records have been deleted Successfully.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
   */
   @Override
    public void onItemClicked(String data) {
        this.selectedMood = data;
    }
    public MoodDao getMoodDao()
    {
        return this.moodDao;
    }
    public void hideFab()
    {
        this.fab.hide();
    }
    public void showFab()
    {
        this.fab.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    public RestCallService getRestCallService() {
        return this.restCallService;
    }
}
