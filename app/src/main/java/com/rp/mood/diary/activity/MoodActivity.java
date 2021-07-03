package com.rp.mood.diary.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rp.mood.diary.R;
import com.rp.mood.diary.async.PositionCalculatorTask;
import com.rp.mood.diary.databinding.MoodLoggerBinding;
import com.rp.mood.diary.fragment.LoggerFragment;
import com.rp.mood.diary.fragment.PredictionFragment;
import com.rp.mood.diary.fragment.StatsFragment;
import com.rp.mood.diary.fragment.ViewerFragment;
import com.rp.mood.diary.listener.CurrentLocationListener;
import com.rp.mood.diary.listener.OnItemClickListener;
import com.rp.mood.diary.model.Mood;
import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.model.MoodDriverDao;
import com.rp.mood.diary.service.AstronomyService;
import com.rp.mood.diary.service.MoodAppService;
import com.rp.mood.diary.service.NotificationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


@AndroidEntryPoint
public class MoodActivity extends AppCompatActivity implements OnItemClickListener {


    String selectedMood;

    LocationManager locationManager;

    private Map<Integer, Fragment> selectedFragmentMapper;

    @Inject
    AstronomyService astronomyService;
    @Inject
    MoodAppService moodAppService;
    @Inject
    PositionCalculatorTask positionCalculatorTask;
    @Inject
    CurrentLocationListener currentLocationListener;
    @Inject
    MoodDao moodDao;
    @Inject
    MoodDriverDao moodDriverDao;
    @Inject
    LoggerFragment loggerFragment;
    @Inject
    ViewerFragment viewerFragment;
    @Inject
    PredictionFragment predictionFragment;
    @Inject
    StatsFragment statsFragment;
    @Inject
    NotificationService notificationService;

    private MoodLoggerBinding binding;

    private CompositeSubscription mCompositeSubscription =  new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intialize();
        binding = MoodLoggerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.designBottomSheet.setOnNavigationItemSelectedListener(menuItem -> {
                    changeFragment(selectedFragmentMapper.get(menuItem.getItemId()));
                    return true;
                }
        );
        changeFragment(loggerFragment);
        notificationService.createNotificationChannel();
        notificationService.setNotificationTimer();
        binding.fab.setOnClickListener((vw) -> {
            processFabClick(vw);
        });


    }

    private void intialize() {
        checkPermission();
        selectedFragmentMapper = new HashMap<>();
        selectedFragmentMapper.put(R.id.nav_log, loggerFragment);
        selectedFragmentMapper.put(R.id.nav_read, viewerFragment);
        selectedFragmentMapper.put(R.id.nav_predict, predictionFragment);
        selectedFragmentMapper.put(R.id.nav_analyze, statsFragment);
    }


    private void checkPermission() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 0, currentLocationListener);
        }
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
                        Observable<JSONObject> observable = Observable.just(moodAppService.save(mood, moodDriver));

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
        int id = item.getItemId();
        if (id == R.id.action_savedValue) {
            changeFragment(viewerFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


   @Override
    public void onItemClicked(String data) {
        this.selectedMood = data;
    }

    public void hideFab()
    {
        this.binding.fab.hide();
    }
    public void showFab()
    {
        this.binding.fab.show();
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
