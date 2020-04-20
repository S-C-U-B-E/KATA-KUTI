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

import static com.example.kata_kuti.MainActivity.isTwoPlayerModeAllowed;
import static com.example.kata_kuti.MainActivity.mDifficultyChoice;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference difficultyPreference, themePreference, symbolPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_visualizer);

            String difficulty_summary,theme_summary,symbol_summary;
        difficultyPreference = findPreference("difficulty");
        themePreference = findPreference("theme");
        symbolPreference = findPreference("symbol");

        if(MainActivity.mIsMatchInProgress){
            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary+"\n(Disabled, Match is in progress!!)");
            difficultyPreference.setEnabled(false);

           /* symbol_summary = symbolPreference.getSummary().toString();
            symbolPreference.setSummary(symbol_summary+"\n(Disabled, Match is in progress!!)");
            symbolPreference.setEnabled(false);*/

        }else{

            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary);
            difficultyPreference.setEnabled(true);

           /* symbol_summary = symbolPreference.getSummary().toString();
            symbolPreference.setSummary(symbol_summary);
            symbolPreference.setEnabled(true);*/
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

            /*if(preference instanceof SwitchPreference && preference.getKey().equals("symbol")){
                String value = sharedPreferences.getString(preference.getKey(),"");
                String summary;
                if(value.equals("true")){// SWITCH TURNED ON

                    if(isTwoPlayerModeAllowed){
                        summary = "PLAYER 1- 'X'"+"\nPLAYER 2- 'O'";

                    }else{//IF ONE-PLAYER MODE IS ON

                        if(mDifficultyChoice.equals("insane")){
                            summary = "S.A.I - 'X'"+"\nPLAYER 2- 'O'";

                        }else{
                            summary = "PLAYER 1- 'X'"+"\nS.A.I - 'O'";

                        }

                    }

                }else{//SWITCH TURNED OFF

                    if(isTwoPlayerModeAllowed){
                        summary = "PLAYER 1- 'O'"+"\nPLAYER 2- 'X'";

                    }else{//IF ONE PLAYER MODE IS ON

                        if(mDifficultyChoice.equals("insane")){
                            summary = "S.A.I - 'O'"+"\nPLAYER 2- 'X'";

                        }else{
                            summary = "PLAYER 1- 'O'"+"\nS.A.I - 'X'";

                        }

                    }
                }

                preference.setSummary(summary);
            }*/
        }

    }
}
