package com.example.kata_kuti;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference difficultyPreference, themePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_visualizer);

            String summary;
        difficultyPreference = findPreference("difficulty");
        themePreference = findPreference("theme");
        //PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("mode");
        //preferenceCategory.removePreference(somePreference);
        if(MainActivity.mIsMatchInProgress){
            summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(summary+"\n(Disabled, Match is in progress!!)");
            difficultyPreference.setEnabled(false);

        }else{
            difficultyPreference.setEnabled(true);
            summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(summary);
        }


    }
}
