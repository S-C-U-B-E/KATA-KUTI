package com.example.kata_kuti;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference difficultyPreference, themePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_visualizer);

            String difficulty_summary,theme_summary;
        difficultyPreference = findPreference("difficulty");
        themePreference = findPreference("theme");

        if(MainActivity.mIsMatchInProgress){
            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary+"\n(Disabled, Match is in progress!!)");
            difficultyPreference.setEnabled(false);

        }else{
            difficultyPreference.setEnabled(true);
            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary);
        }

            theme_summary = themePreference.getSharedPreferences().getString("theme","water");
            themePreference.setSummary(theme_summary);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);

        if(null != preference){
            if( !(preference instanceof CheckBoxPreference) && !(preference instanceof SwitchPreference) ){
                String value = sharedPreferences.getString(preference.getKey(),"");
                preference.setSummary(value);

            }
        }

    }
}
