package com.example.kata_kuti;

import com.example.kata_kuti.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference difficultyPreference, themePreference;
    private String googleFormFeedback = "https://docs.google.com/forms/d/e/1FAIpQLSe_-pufKfnUmv06yXEIALgBKyxub3J8JHqFBaL54mljWidYLQ/viewform?usp=sf_link";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_visualizer);

            String difficulty_summary,theme_summary,symbol_summary;
        difficultyPreference = findPreference("difficulty");
        themePreference = findPreference("theme");

        if(MainActivity.mIsMatchInProgress){
            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary+"\n(Disabled, Match is in progress!!)");
            difficultyPreference.setEnabled(false);



        }else{

            difficulty_summary = difficultyPreference.getSharedPreferences().getString("difficulty","easy");
            difficultyPreference.setSummary(difficulty_summary);
            difficultyPreference.setEnabled(true);


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

        if(key.equals("feedback"))
        {

            openGoogleForm(googleFormFeedback);

            return true;
        }else if(key.equals("about_me"))
        {

            aboutMePressed();

            return true;
        }else if(key.equals("about_game"))
        {

            aboutGamePressed();

            return true;
        }

        return false;
    }


    private void openGoogleForm(String Url){

        Toast.makeText(getContext(), "Every Opinion Matters..", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();

        Uri googleForm = Uri.parse(Url);

        Intent intent = new Intent(Intent.ACTION_VIEW,googleForm);

        if(intent.resolveActivity(getActivity().getPackageManager()) != null){

            startActivity(intent);

        }else {
            Toast.makeText(getContext(), "No such App found!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void aboutGamePressed() {

        // Otherwise if there are unfinished match, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        openGoogleForm(googleFormFeedback);
                    }
                };

        // Show dialog that there are unfinished match.
        showDetailsAboutGame(discardButtonClickListener);
    }

    private void showDetailsAboutGame(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the splash_welcome_message, and click listeners
        // for the positive and negative response buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_stat_name);
        builder.setTitle(getContext().getResources().getString(R.string.about_game_title));
        builder.setMessage(getContext().getResources().getString(R.string.about_game_message));
        builder.setPositiveButton("Rate This App", discardButtonClickListener);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep Playing" button, so dismiss the dialog
                // and continue enjoying the match XD.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void aboutMePressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.my_avatar);
        builder.setTitle(getContext().getResources().getString(R.string.about_me_title));
        builder.setMessage(getContext().getResources().getString(R.string.about_me_message));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep Playing" button, so dismiss the dialog
                // and continue enjoying the match XD.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
