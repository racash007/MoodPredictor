package com.rp.mood.diary.service.impl;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.rp.mood.diary.R;
import com.rp.mood.diary.receiver.NotificationReceiver;
import com.rp.mood.diary.service.NotificationService;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static android.content.Context.ALARM_SERVICE;

public class NotificationServiceImpl implements NotificationService {
    private final Context context;

    @Inject
    public NotificationServiceImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.context.getString(R.string.channel_name);
            String description = this.context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(this.context.getString(R.string.channel_Id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void setNotificationTimer() {
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(this.context.getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context.getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR*2,pendingIntent);
    }
}
