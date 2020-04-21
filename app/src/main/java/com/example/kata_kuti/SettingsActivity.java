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

import static com.example.kata_kuti.MainActivity.isNotificationAllowed;
import static com.example.kata_kuti.MainActivity.isSettingsScreenOpened;
import static com.example.kata_kuti.MainActivity.isUserInSettingsScreen;
import static com.example.kata_kuti.MainActivity.mIsMatchInProgress;
import static com.example.kata_kuti.MainActivity.mThemeChoice;

public class SettingsActivity extends AppCompatActivity {

    Button shareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isUserInSettingsScreen = true;

        if(mThemeChoice.equals("fire")){
            setTheme(R.style.SettingAppTheme_fire);
        }else if(mThemeChoice.equals("earth")){
            setTheme(R.style.SettingAppTheme_earth);
        }else if(mThemeChoice.equals("water")){
            setTheme(R.style.SettingAppTheme_water);
        }

        setContentView(R.layout.activity_settings);

        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMyApp();
            }
        });

        Toast.makeText(SettingsActivity.this,"stngs onCreate()",Toast.LENGTH_SHORT).show();
    }

    private void shareMyApp(){

        String shareQuery = "Hello,I have been playing this app for some time now, this is a wonderfull app,\nPlease Rate this app in the link below\n\n https://docs.google.com/forms/d/e/1FAIpQLSe_-pufKfnUmv06yXEIALgBKyxub3J8JHqFBaL54mljWidYLQ/viewform?usp=sf_link";

            String mimeType = "text/plain";
            String title = "Sharing is caring!!";
            String text = shareQuery;

            shareText(mimeType,title,text);

    }


    private void shareText(String mimeType, String title, String text){

        Intent intent = ShareCompat.IntentBuilder.from(this).setChooserTitle(title).setType(mimeType).setText(text).getIntent();

        if(null != intent.resolveActivity(getPackageManager())){

            startActivity(intent);

        }else {
            Toast.makeText(this, "No such App found!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(SettingsActivity.this,"stngs onPause()",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(SettingsActivity.this,"stngs onStart()",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(SettingsActivity.this,"stngs onRestart()",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mIsMatchInProgress && isNotificationAllowed && isUserInSettingsScreen ){
            NotificationUtils.remindUserOfTheOnGoingMatchFromSettings(SettingsActivity.this);}


        Toast.makeText(SettingsActivity.this,"stngs onStop()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NotificationUtils.clearAllNotifications(SettingsActivity.this);

        Toast.makeText(SettingsActivity.this,"stngs onDestroy()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationUtils.clearAllNotifications(SettingsActivity.this);

        Toast.makeText(SettingsActivity.this,"stngs onResume()",Toast.LENGTH_SHORT).show();
    }
}
