package com.abhigyan.user.instagramcloneacadview;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView textView;
    ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.textView);
        textView.setTranslationY(1000f);
        logoView = findViewById(R.id.logoImage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logoView.animate().translationYBy(-2000f).setDuration(700);
                textView.animate().translationYBy(-1000f).setDuration(700);
            }
        },3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        },5000);
    }
}
