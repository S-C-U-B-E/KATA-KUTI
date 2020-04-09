package com.example.kata_kuti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtils {

    public static final int TEST_NOTIFICATION_ID = 1010;

    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    public static final String TEST_NOTIFICATION_CHANNEL_ID = "test_notification_channel";

    public static void remindUserOfTheOnGoingMatch(Context context){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    TEST_NOTIFICATION_CHANNEL_ID,
                    "MATCH IN PROGRESS",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, 0);
        Intent broadCastIntent = new Intent(context,NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(context,0,broadCastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,TEST_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Kata Kuti")
                .setContentText("Match in Progress..")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current match is in progress...Tap here to resume!!"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground,"IGNORE",actionIntent)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(TEST_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications(Context context){  // To be called when finished() is called and onDestroy()

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }


}
