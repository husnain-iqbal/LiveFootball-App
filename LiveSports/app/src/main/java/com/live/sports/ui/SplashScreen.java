package com.live.sports.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.live.sports.R;
import com.live.sports.utils.Settings;


public class SplashScreen extends Activity {
    protected boolean isClosed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isClosed) {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, 2500);


    }

    @Override
    protected void onDestroy() {
        isClosed = true;
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}

