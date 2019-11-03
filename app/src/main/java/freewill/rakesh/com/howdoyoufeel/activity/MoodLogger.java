package freewill.rakesh.com.howdoyoufeel.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.adapter.MoodViewAdapter;
import freewill.rakesh.com.howdoyoufeel.async.PositionCalculatorTask;
import freewill.rakesh.com.howdoyoufeel.database.DaoHelper;
import freewill.rakesh.com.howdoyoufeel.listener.CurrentLocationListener;
import freewill.rakesh.com.howdoyoufeel.listener.OnItemClickListener;
import freewill.rakesh.com.howdoyoufeel.model.DaoSession;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDao;
import freewill.rakesh.com.howdoyoufeel.model.MoodDriverDao;
import freewill.rakesh.com.howdoyoufeel.receiver.NotificationReceiver;

public class MoodLogger extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String selectedMood;
    DaoSession daoSession;
    MoodDao moodDao;
    MoodDriverDao moodDriverDao;
    String moodDesc;
    PositionCalculatorTask positionCalculatorTask;
    CurrentLocationListener currentLocationListener;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = DaoHelper.getDaoSession(this);
        moodDao = daoSession.getMoodDao();
        moodDriverDao = daoSession.getMoodDriverDao();
        currentLocationListener = new CurrentLocationListener();
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!checkLocationPermission())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 0, currentLocationListener);
        }
        setContentView(R.layout.activity_mood_logger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter = new MoodViewAdapter(this,this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.mood_selection_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        createNotificationChannel();
        setNotificationTimer();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedMood==null)
                {
                    Snackbar.make(view, "Please select a mood first", Snackbar.LENGTH_LONG).show();
                }else {
                    showDescriptionAlert(view);
                }
            }
        });


    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 0, currentLocationListener);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void saveMood(View view)
    {Mood mood = new Mood(selectedMood,moodDesc);
        moodDao.insert(mood);
        positionCalculatorTask = new PositionCalculatorTask(moodDriverDao,currentLocationListener,this);
        positionCalculatorTask.execute(mood);
        Snackbar.make(view, "Mood saved Successfully" , Snackbar.LENGTH_LONG).show();}

    private void showDescriptionAlert(final View view) {
        moodDesc = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comment (Optional)");

// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSingleLine(false);  //add this
        input.setLines(4);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setHorizontalScrollBarEnabled(false);


        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moodDesc = input.getText().toString();
                saveMood(view);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_Id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void setNotificationTimer() {
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR*2,pendingIntent);
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
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
            Intent intent = new Intent(this,MoodViewer.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(String data) {
        this.selectedMood = data;
    }
}
