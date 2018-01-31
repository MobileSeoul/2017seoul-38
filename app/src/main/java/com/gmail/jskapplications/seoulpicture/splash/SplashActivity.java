package com.gmail.jskapplications.seoulpicture.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private Intent mMyintent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Start home activity
        mMyintent = new Intent(SplashActivity.this, LoginActivity.class);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(mMyintent);
                finish();
            }
        }, 2000);


    }
}