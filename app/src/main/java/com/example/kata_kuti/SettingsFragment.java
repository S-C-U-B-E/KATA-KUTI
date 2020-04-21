package com.example.kata_kuti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
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

    /*
    * Need to unregister the resources..that's a good practise
    * */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    /*
    * whenever user changes an option.. it needs to be immediately shown to him/her right???
    * */
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

    /*
    * I have made a google form to take feedback from users and this will open from this preference screen
    * This idea came to me yesterday night after goinf to bed XD (Me Iz-smart AF LMAO)
    * */
    @Override
    public boolean onPreferenceTreeClick (Preference preference)
    {
        String key = preference.getKey();
        if(key.equals("feedback")){
            Toast.makeText(getContext(), "Every Opinion Matters..", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();

            String googleFormFeedback = "https://docs.google.com/forms/d/e/1FAIpQLSe_-pufKfnUmv06yXEIALgBKyxub3J8JHqFBaL54mljWidYLQ/viewform?usp=sf_link";
            openGoogleForm(googleFormFeedback);


            return true;
        }
        return false;
    }

    private void openGoogleForm(String Url){

        Uri googleForm = Uri.parse(Url);

        Intent intent = new Intent(Intent.ACTION_VIEW,googleForm);

        if(intent.resolveActivity(getActivity().getPackageManager()) != null){

            startActivity(intent);

        }else {
            Toast.makeText(getContext(), "No such App found!!", Toast.LENGTH_SHORT).show();
        }
    }
}


/*
* public void onClickShareTextButton(View v) {
        String shareQuery = query.getText().toString();

        if (!shareQuery.isEmpty()) {

                String mimeType = "text/plain";
                String title = "Learn How To Share!";
                String text = shareQuery;

                shareText(mimeType,title,text);

        } else {
            Toast.makeText(this, "Enter a text to send!!!", Toast.LENGTH_SHORT).show();
        }
    }
*
* */

/*
* private void shareText(String mimeType, String title, String text){

       Intent intent = ShareCompat.IntentBuilder.from(this).setChooserTitle(title).setType(mimeType).setText(text).getIntent();

        if(null != intent.resolveActivity(getPackageManager())){

            startActivity(intent);

        }else {
            Toast.makeText(this, "No such App found!!", Toast.LENGTH_SHORT).show();
        }

    }
*
* */