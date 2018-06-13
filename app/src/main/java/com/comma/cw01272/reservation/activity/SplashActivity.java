package com.comma.cw01272.reservation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.comma.cw01272.reservation.activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(this, LoginActivity.class);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}