package com.briontacticalsystems.tsubuild152;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final int Splash_time = 3500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation fadeImage = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView imageView = findViewById(R.id.splash);
        imageView.setImageResource(R.drawable.tsulogo);
        imageView.startAnimation(fadeImage);

        AlphaAnimation fadeText = new AlphaAnimation(0.0f, 1.0f);
        fadeText.setDuration(1500);

        TextView textView = findViewById(R.id.text);
        textView.startAnimation(fadeText);

        TextView PortName = findViewById(R.id.PortalName);
        PortName.startAnimation(fadeText);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, Splash_time);

    }
}