package com.live.sports.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.plus.BitmapCache;
//import com.apptracker.android.track.AppTracker;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinErrorCodes;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.live.sports.AppData;
import com.live.sports.R;
import com.live.sports.data.Channels;
import com.live.sports.utils.ChannelDetailsAdaper;
import com.live.sports.utils.Settings;


public class ChannelDetails extends AppCompatActivity implements ImageLoader.ImageLoaderProvider {

    InterstitialAd mInterstitialAd;
    InterstitialAd mInterstitialAdAfter;


    InterstitialAd mInterstitialAd2;
    InterstitialAd mInterstitialAdAfter2;

    private String selectedCat = "";
    private ProgressDialog progDialog;
    private String resp = "";
    private String liveLink = "";
    private ChannelDetailsAdaper adapter;
    private ImageLoader imageLoader;
    private BitmapCache imageCache;
    protected boolean isClosed = false;
    private LinearLayout customAdLayout;
    private AppLovinInterstitialAdDialog interstitialAdDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_details);

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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Settings.STARTUP_AD_ID);
        mInterstitialAdAfter = new InterstitialAd(this);
        mInterstitialAdAfter.setAdUnitId(Settings.STARTUP_AD_ID);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (!isClosed)
                    closeProgressDialog();
                playlive(liveLink);
            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (!isClosed)
                    closeProgressDialog();
                playlive(liveLink);
            }
        });


        mInterstitialAdAfter.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (!isClosed)
                    closeProgressDialog();
            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAdAfter.isLoaded()) {
                    mInterstitialAdAfter.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (!isClosed)
                    closeProgressDialog();
            }
        });


        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId(Settings.STARTUP_AD_ID);
        mInterstitialAdAfter2 = new InterstitialAd(this);
        mInterstitialAdAfter2.setAdUnitId(Settings.STARTUP_AD_ID);


        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (!isClosed)
                    closeProgressDialog();

            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (!isClosed)
                    closeProgressDialog();

            }
        });


        mInterstitialAdAfter2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (!isClosed)
                    closeProgressDialog();
            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAdAfter2.isLoaded()) {
                    mInterstitialAdAfter2.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (!isClosed)
                    closeProgressDialog();
            }
        });


        imageCache = BitmapCache.getInstance(this.getSupportFragmentManager());
        imageLoader = new ImageLoader(AppData.getImageRequestQueue(ChannelDetails.this), imageCache);
        selectedCat = getIntent().getStringExtra(Settings.SELECTED_CAT);
        if (selectedCat == null) {
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        GridView gridview = (GridView) findViewById(R.id.detailsgridview);

        adapter = new ChannelDetailsAdaper(this, 0, AppData.liveData.get(selectedCat));
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                sendRequest(((Channels) parent.getAdapter().getItem(position)).channelLink);

            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        if (AppData.admobtype1) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else if (Settings.CUSTOM_AD_VIEW_ENABLE) {
            customAdLayout.setVisibility(View.VISIBLE);
        }

        if (AppData.admobMiddle) {
            requestNewInterstitial2();
        }
        if (AppData.appLovinMiddle) {
            if (AppLovinInterstitialAd.isAdReadyToDisplay(this)) {
                AppLovinInterstitialAd.show(this);
            }
        }


//        if (savedInstanceState == null) {
//             Initialize Leadbolt SDK with your api key
//            AppTracker.startSession(getApplicationContext(), Settings.LEADBOARD_AD_ID);
//        }
//         cache Leadbolt Ad without showing it
//        AppTracker.loadModuleToCache(getApplicationContext(), "inapp");


    }

    @Override
    protected void onDestroy() {
        isClosed = true;
        super.onDestroy();
    }

    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void requestNewInterstitial2() {

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd2.loadAd(adRequest);
    }

    private void sendRequest(final String url) {
        showProgressDialog("Loading Live");
        liveLink = url;
        if (AppData.admobbeforeV) {
            requestNewInterstitial();
        } else if (AppData.appLovinBeforeV) {
            if (AppLovinInterstitialAd.isAdReadyToDisplay(this)) {
                interstitialAdDialog = AppLovinInterstitialAd.create(AppData.getApplovinSdk(this), ChannelDetails.this);


                interstitialAdDialog.setAdLoadListener(new AppLovinAdLoadListener() {
                    @Override
                    public void adReceived(AppLovinAd appLovinAd) {

                    }

                    @Override
                    public void failedToReceiveAd(int errorCode) {


                        Log.i("Test Applovin", "Interstitial Failed");
                        closeProgressDialog();
                        playlive(liveLink);
                        if (errorCode == AppLovinErrorCodes.NO_FILL) {
//                            log("No-fill: No ads are currently available for this device/country");
                        } else {
//                            log("Interstitial failed to load with error code " + errorCode);
                        }
                    }
                });

                interstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                    @Override
                    public void adDisplayed(AppLovinAd appLovinAd) {

                    }

                    @Override
                    public void adHidden(AppLovinAd appLovinAd) {
                        Log.i("Test Applovin", "Interstitial Hidden");

                        closeProgressDialog();
                        playlive(liveLink);
                    }
                });

                interstitialAdDialog.setAdClickListener(new AppLovinAdClickListener() {
                    @Override
                    public void adClicked(AppLovinAd appLovinAd) {

                    }
                });

                // This will only ever be used if you have video ads enabled.
                interstitialAdDialog.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
                    @Override
                    public void videoPlaybackBegan(AppLovinAd appLovinAd) {

                    }

                    @Override
                    public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean wasFullyViewed) {

                        Log.i("Test Applovin", "Video Ended");
                        closeProgressDialog();
                        playlive(liveLink);
                    }
                });

                interstitialAdDialog.show();

            } else {
                closeProgressDialog();
                playlive(liveLink);
            }
        } else {
            closeProgressDialog();
            playlive(liveLink);
        }


    }

    protected void showAdmobAfterV() {

    }

    @Override
    protected void onResume() {
        if (AppData.showAd) {
            AppData.showAd = false;
            if (AppData.admobAfterV) {
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mInterstitialAdAfter.loadAd(adRequest);
            }
            if (AppData.appLovinAfterV) {
                if (AppLovinInterstitialAd.isAdReadyToDisplay(this)) {
                    AppLovinInterstitialAd.show(this);
                }
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }


    private void playlive(String liveURL) {

        Intent i = new Intent(ChannelDetails.this, Explayer.class);
        i.putExtra("url", liveURL);
        startActivity(i);

    }

    void showProgressDialog(final String msg) {
        if (!isClosed) {
            progDialog = ProgressDialog.show(this, Settings.INSTANCE_NAME_VERBOSE, msg, true);
            progDialog.setCancelable(true);
            progDialog.setCanceledOnTouchOutside(false);
        }
    }

    void closeProgressDialog() {

        if (!isClosed && progDialog != null) {

            if (progDialog != null) {

                if (progDialog.isShowing()) {
                    progDialog.dismiss();
                }
            }
        }
    }


    @Override
    public ImageLoader getImageLoaderInstance() {
        return imageLoader;
    }
}


