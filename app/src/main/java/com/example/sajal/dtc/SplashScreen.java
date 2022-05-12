package com.example.sajal.dtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.IntArrayEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sajal.dtc.R;

public class SplashScreen extends AppCompatActivity {
    ImageView img1;
    TextView text1, text2;
    Animation main, top, buttom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // This is used to hide the status bar and
        // make the splash screen as a full screen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // This code use for hide toolbar for this activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        img1 = findViewById(R.id.img1);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        // Perform animation    -------- just try to understand the meaning the code .
        main = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_slide);
        top = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_slide);
        buttom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttom_animation);
        img1.setAnimation(main);
        text1.setAnimation(top);
        text2.setAnimation(buttom);
        // Go to next activity...
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(LoginPage.PREFS_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);

                if (hasLoggedIn){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashScreen.this,LoginPage.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 5000);
    }
}