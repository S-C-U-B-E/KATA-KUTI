package com.example.kata_kuti;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;

import static com.example.kata_kuti.MainActivity.mThemeChoice;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mThemeChoice.equals("fire")){
            setTheme(R.style.SettingAppTheme_fire);
        }else if(mThemeChoice.equals("red")){
            setTheme(R.style.AppTheme_fire);
        }else if(mThemeChoice.equals("lime")){
            setTheme(R.style.AppTheme_fire);
        }else if(mThemeChoice.equals("green")){
            setTheme(R.style.AppTheme_fire);
        }else if(mThemeChoice.equals("water")){
            setTheme(R.style.SettingAppTheme_water);
        }
        setContentView(R.layout.activity_settings);
        Toast.makeText(SettingsActivity.this,"stngs onCreate()",Toast.LENGTH_SHORT).show();
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
        Toast.makeText(SettingsActivity.this,"stngs onStop()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(SettingsActivity.this,"stngs onDestroy()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(SettingsActivity.this,"stngs onResume()",Toast.LENGTH_SHORT).show();
    }
}
