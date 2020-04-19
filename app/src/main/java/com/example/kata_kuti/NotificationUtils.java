package com.example.kata_kuti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


/*
* <<<<<<<<<<<<<<<  CONFESSION   >>>>>>>>>>>>
* Truly speaking..
* Couldn't understand the entire working of the notification...
* But somehow after 2days of continuous googling.. finally able to understand pieces and implemented
* successfully the way i wanted this notification to work.
*
* "IGNORE" / "DISMISS" of notification was too easy...
*
* But to RESUME the game where it was left ON_CLICKING of the notification was tough as hell for me.. :D
*
* NOTE: SOME OF THE COMMENTS AMONG THE CODE DOWN BELOW MIGHT NOT BE ACCURATE (AS THEY ARE DONE MY ME... XD)
*  */
public class NotificationUtils {

    public static final int MATCH_IN_PROGRESS_NOTIFICATION_ID = 1010; //Notification ID

    public static final String MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = "match_in_progress_notification_channel"; //Notification Channel ID

    public static void remindUserOfTheOnGoingMatch(Context context){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);  //Getting Notification Service

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // Since this is not available below Oreo and stuff (I will provide links!!!)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID,
                    "MATCH IN PROGRESS CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

        /*
        * This next 4 lines of code is the golden egg for me... i have to study those more ..
        * ACTION: TO RESUME THE GAME WHERE LEFT
        * */
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, 0);

        /*
        * ACTION: FOR THE "IGNORE" ACTION BUTTON TO TASK
        * */
        Intent broadCastIntent = new Intent(context,NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(context,0,broadCastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary_fire))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Kata Kuti")
                .setContentText("Match in Progress..")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current match is in progress... Tap here to resume!!"))
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_notification_action,"IGNORE",actionIntent)
                .setAutoCancel(true);

        if(MainActivity.isNotificationVibrationAllowed){
            notificationBuilder.setVibrate(new long[] { 0, 200, 300, 200, 300 });
        }

        if(MainActivity.isNotificationSoundAllowed){
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH); // FOR VERSIONS ABOVE JELLY_BEAN AND BELOW OREO
        }

        notificationManager.notify(MATCH_IN_PROGRESS_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications(Context context){  // To be called when finished() is called and onDestroy()

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll(); //if wished NOTIFICATION_ID could also have been given.
    }


}
