package com.example.kata_kuti;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.preference.CheckBoxPreference;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.kata_kuti.MainActivity.isNotificationAllowed;
import static com.example.kata_kuti.MainActivity.isSettingsScreenOpened;
import static com.example.kata_kuti.MainActivity.isUserInSettingsScreen;
import static com.example.kata_kuti.MainActivity.mIsMatchInProgress;
import static com.example.kata_kuti.MainActivity.mMainActivityCurrentThemeChoice;
import static com.example.kata_kuti.MainActivity.mThemeChoice;

public class SettingsActivity extends AppCompatActivity {

    Button shareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isUserInSettingsScreen = true;

        if(mMainActivityCurrentThemeChoice.equals("fire")){
            setTheme(R.style.SettingAppTheme_fire);

        }else if(mMainActivityCurrentThemeChoice.equals("earth")){
            setTheme(R.style.SettingAppTheme_earth);

        }else if(mMainActivityCurrentThemeChoice.equals("water")){
            setTheme(R.style.SettingAppTheme_water);
        }

        setContentView(R.layout.activity_settings);

        // Setup FAB to share my app
        FloatingActionButton share = (FloatingActionButton) findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMyApp();
            }
        });


        //Toast.makeText(SettingsActivity.this,"stngs onCreate()",Toast.LENGTH_SHORT).show();
    }

    private void shareMyApp(){

        /*
        * Atlast after 3weeks i have finally updated Google Drive space to have my first complete application.. I am more than just happy :D
        * */
        String shareQuery = "Hello,\n" +
                "I am Shankha Shubhra Sanyal,\n" +
                "I've been learning android for a long time now.\n" +
                "For the first time I have built a complete android app.\n" +
                "\n" +
                "Give it a try. Download it from link below.\n" +
                "(You have to download it from G-drive,\n" +
                "as I don't have much resources to upload it to\n" +
                "any renowned cloud store.)\n" +
                "\n" +
                "I have kept the entire code open-source \n" +
                "for anyone who wants to learn android\n" +
                "and built their own apps.\n" +
                "(The source is given in the app itself)\n" +
                "\n" +
                "Download it, play and enjoy with your friends and family.\n" +
                "Hope you will like it.\n" +
                "Don't forget to give a feedback.\n" +
                "Your feedback is very important to me.\n" +
                "\n" +
                "https://drive.google.com/open?id=1oE6eBm6eB_etAiLt2mXtSH27J7i0V9Hn";

            String mimeType = "text/plain";
            String title = "Sharing is caring!!";
            String text = shareQuery;

            shareText(mimeType,title,text);

    }


    private void shareText(String mimeType, String title, String text){

        Intent intent = ShareCompat.IntentBuilder.from(this).setChooserTitle(title).setType(mimeType).setText(text).getIntent();

        if(null != intent.resolveActivity(getPackageManager())){

            Toast.makeText(this, "Sharing is Caring!!", Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }else {
            Toast.makeText(this, "No such App found!!", Toast.LENGTH_SHORT).show();
        }

    }

    /*@Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(SettingsActivity.this,"stngs onPause()",Toast.LENGTH_SHORT).show();

    }*/

    /*@Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(SettingsActivity.this,"stngs onStart()",Toast.LENGTH_SHORT).show();

    }
*/
    /*@Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(SettingsActivity.this,"stngs onRestart()",Toast.LENGTH_SHORT).show();

    }*/

    @Override
    protected void onStop() {
        super.onStop();

        if(mIsMatchInProgress && isNotificationAllowed && isUserInSettingsScreen ){
            NotificationUtils.remindUserOfTheOnGoingMatchFromSettings(SettingsActivity.this);}

        //CHANGE 09/08/2020
        Toast.makeText(SettingsActivity.this,"Sound:"+MainActivity.isNotificationSoundAllowed+"\nVibration:"+MainActivity.isNotificationVibrationAllowed,Toast.LENGTH_SHORT).show();
        //Toast.makeText(SettingsActivity.this,"stngs onStop()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NotificationUtils.clearAllNotifications(SettingsActivity.this);

        //Toast.makeText(SettingsActivity.this,"stngs onDestroy()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationUtils.clearAllNotifications(SettingsActivity.this);

        //Toast.makeText(SettingsActivity.this,"stngs onResume()",Toast.LENGTH_SHORT).show();
    }
}
