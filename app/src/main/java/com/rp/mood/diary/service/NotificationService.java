package com.rp.mood.diary.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.rp.mood.diary.R;
import com.rp.mood.diary.receiver.NotificationReceiver;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static android.content.Context.ALARM_SERVICE;

public interface NotificationService {


    void createNotificationChannel();

    void setNotificationTimer();
}
