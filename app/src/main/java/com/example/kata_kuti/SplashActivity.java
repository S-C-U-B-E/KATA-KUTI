package com.example.kata_kuti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView icon;
    /*private TextView textView;*/
    private RelativeLayout parent;

    SharedPreferences sharedPreferences;
    Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        parent = findViewById(R.id.parent_linear);
        icon = findViewById(R.id.imageView);
        /*textView = findViewById(R.id.textview);*/

        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime",true);



        Animation iconAnim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        iconAnim.setDuration(4000);

        icon.startAnimation(iconAnim);
        /*textView.startAnimation(iconAnim);*/



        if(firstTime) {
            parent.setBackgroundResource(R.drawable.first_time_gradient_animation_list);
            AnimationDrawable animationDrawable = (AnimationDrawable) parent.getBackground();

            animationDrawable.setEnterFadeDuration(1080);
            animationDrawable.setExitFadeDuration(1000);

            animationDrawable.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime",firstTime);
                    editor.apply();

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 18000);

        }else{
            parent.setBackgroundResource(R.drawable.other_time_gradient_animation_list);
            AnimationDrawable animationDrawable_1 = (AnimationDrawable) parent.getBackground();
            //Toast.makeText(this, "done",Toast.LENGTH_SHORT).show();
            animationDrawable_1.setEnterFadeDuration(1700);
            animationDrawable_1.setExitFadeDuration(1000);

            animationDrawable_1.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 6000);
        }


    }

}

