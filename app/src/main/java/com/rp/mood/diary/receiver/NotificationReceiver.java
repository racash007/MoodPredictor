package com.rp.mood.diary.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rp.mood.diary.activity.MoodActivity;

import com.rp.mood.diary.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent requesting_intent = new Intent(context, MoodActivity.class);
        requesting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,requesting_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,context.getResources().getString(R.string.channel_Id))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("How are you feeling now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_launcher,context.getString(R.string.log),pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());

    }

}
