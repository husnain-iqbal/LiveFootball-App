package com.live.sports.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.devbrackets.android.exomedia.EMVideoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.live.sports.AppData;
import com.live.sports.R;
import com.live.sports.utils.Settings;

public class Explayer extends AppCompatActivity {


    String playerUrl = "";
    private LinearLayout customAdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        playerUrl = getIntent().getStringExtra("url");
        setupVideoView(playerUrl);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);

        customAdLayout = (LinearLayout) findViewById(R.id.customadLayout);
        customAdLayout.setVisibility(View.GONE);
        WebView customAdView = (WebView) findViewById(R.id.customAdView);
        customAdView.loadUrl(Settings.CUSTOM_AD_VIEW_FILE_URL);
        customAdView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Uri uri = Uri.parse(Settings.CUSTOM_AD_VIEW_OPEN_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });

        if (AppData.admobtype2Top) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else if (Settings.CUSTOM_AD_VIEW_ENABLE) {
            customAdLayout.setVisibility(View.VISIBLE);
        }

        if (AppData.admobtype2Bottom) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

    }

    private void setupVideoView(String url) {
        final EMVideoView emVideoView = (EMVideoView) findViewById(R.id.video_play_activity_video_view);
        emVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                emVideoView.start();

            }
        });
        emVideoView.setVideoURI(Uri.parse(url));
    }


    @Override
    protected void onDestroy() {
        AppData.showAd = true;
        super.onDestroy();
    }
}
