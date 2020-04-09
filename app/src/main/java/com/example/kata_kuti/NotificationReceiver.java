package com.example.kata_kuti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/*
* To create a Broadcast stuff or something XD...
* seriously i have to work a lot on this ..
*
* ACTION: CLEARS NOTIFICATION WHEN NOT REQUIRED.
* */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.clearAllNotifications(context);
    }
}
