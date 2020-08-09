package com.example.kata_kuti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

import static com.example.kata_kuti.MainActivity.mMainActivityCurrentThemeChoice;
import static com.example.kata_kuti.MainActivity.mThemeChoice;


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

    public static  String MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = "match_in_progress_notification_channel"; //Notification Channel ID

    public static String NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = "";

    public static long ID_Counter = 0;

    public static void remindUserOfTheOnGoingMatch(Context context){

        ID_Counter++;
        NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID+Long.toString(ID_Counter);
/*
* Each time the Notification channel gets created with same name but different ID... to apply changes in shared preferences.
* */
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);  //Getting Notification Service


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // Since this is not available below Oreo and stuff (I will provide links!!!)
        {

            //Toast.makeText(context,"API 29 check main screen",Toast.LENGTH_SHORT).show();

            //CHANGE 09/08/2020
            if(null != notificationManager.getNotificationChannel(MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID))
                {notificationManager.deleteNotificationChannel(MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID);}


            NotificationChannel mChannel = new NotificationChannel(
                    NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID,
                    "MATCH IN PROGRESS",
                    NotificationManager.IMPORTANCE_HIGH);        //CHANGE MADE 08/08

            if(!MainActivity.isNotificationSoundAllowed){
                mChannel.setSound(null, null);
            }

            if(MainActivity.isNotificationVibrationAllowed){
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[] { 0, 200, 300, 200, 300 });

            }else{
                mChannel.enableVibration(false);
            }

            MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID;
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
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Kata Kuti")
                .setContentText("Match in Progress..")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current match is in progress... Tap here to resume!!"))
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_notification_action,"IGNORE",actionIntent)
                .setAutoCancel(true);

        if(mMainActivityCurrentThemeChoice.equals("fire")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary_fire));

        }else if(mMainActivityCurrentThemeChoice.equals("water")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary_water));

        }else if(mMainActivityCurrentThemeChoice.equals("earth")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark_earth));

        }




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            //Toast.makeText(context,"API Others check main screen",Toast.LENGTH_SHORT).show();
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH); // FOR VERSIONS ABOVE JELLY_BEAN AND BELOW OREO

            if(MainActivity.isNotificationVibrationAllowed){
            notificationBuilder.setVibrate(new long[] { 0, 200, 300, 200, 300 });
        }

        if(MainActivity.isNotificationSoundAllowed){
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }

        }       //CHANGE MADE 08/08

        notificationManager.notify(MATCH_IN_PROGRESS_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void remindUserOfTheOnGoingMatchFromSettings(Context context){

        ID_Counter++;
        NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID+Long.toString(ID_Counter);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);  //Getting Notification Service


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // Since this is not available below Oreo and stuff (I will provide links!!!)
        {

            //Toast.makeText(context,"API 29 check settings",Toast.LENGTH_SHORT).show();

            //CHANGE 09/08/2020
            if(null != notificationManager.getNotificationChannel(MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID))
            {notificationManager.deleteNotificationChannel(MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID);}


            NotificationChannel mChannel = new NotificationChannel(
                    NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID,
                    "MATCH IN PROGRESS",
                    NotificationManager.IMPORTANCE_HIGH);        //CHANGE MADE 08/08

            if(!MainActivity.isNotificationSoundAllowed){
                mChannel.setSound(null, null);
            }

            if(MainActivity.isNotificationVibrationAllowed){
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[] { 0, 200, 300, 200, 300 });

            }else{
                mChannel.enableVibration(false);
            }

            MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID = NEW_MATCH_IN_PROGRESS_NOTIFICATION_CHANNEL_ID;
            notificationManager.createNotificationChannel(mChannel);
        }

        /*
         * This next 4 lines of code is the golden egg for me... i have to study those more ..
         * ACTION: TO RESUME THE GAME WHERE LEFT
         * */
        Intent resultIntent = new Intent(context, SettingsActivity.class);
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
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Kata Kuti")
                .setContentText("Match in Progress..")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current match is in progress... Tap here to resume!!"))
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_notification_action,"IGNORE",actionIntent)
                .setAutoCancel(true);

        if(mMainActivityCurrentThemeChoice.equals("fire")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary_fire));

        }else if(mMainActivityCurrentThemeChoice.equals("water")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary_water));

        }else if(mMainActivityCurrentThemeChoice.equals("earth")){
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark_earth));

        }

        /*if(MainActivity.isNotificationVibrationAllowed){
            notificationBuilder.setVibrate(new long[] { 0, 200, 300, 200, 300 });
        }

        if(MainActivity.isNotificationSoundAllowed){
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }
*/

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            //Toast.makeText(context,"API Others check settings",Toast.LENGTH_SHORT).show();
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH); // FOR VERSIONS ABOVE JELLY_BEAN AND BELOW OREO

            if(MainActivity.isNotificationVibrationAllowed){
                notificationBuilder.setVibrate(new long[] { 0, 200, 300, 200, 300 });
            }

            if(MainActivity.isNotificationSoundAllowed){
                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            }

        }  //CHANGE MADE 08/08

        notificationManager.notify(MATCH_IN_PROGRESS_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications(Context context){  // To be called when finished() is called and onDestroy()

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll(); //if wished NOTIFICATION_ID could also have been given.
    }


}
