package com.live.sports.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.GeneralStringRequest;
import com.android.volley.toolbox.plus.BitmapCache;
import com.applovin.adview.AppLovinInterstitialAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.live.sports.AppData;
import com.live.sports.R;
import com.live.sports.data.Channels;
import com.live.sports.utils.DrawerAdapter;
import com.live.sports.utils.JSONHelper;
import com.live.sports.utils.Settings;
import com.live.sports.utils.Utils;
import com.live.sports.utils.categoriesAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ImageLoader.ImageLoaderProvider {

    private ImageLoader imageLoader;
    private BitmapCache imageCache;
    private ProgressDialog progDialog;
    protected boolean isClosed = false;
    private ListView mDrawerList;
    private DrawerAdapter mAdapter;
    InterstitialAd mInterstitialAd;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    public boolean confirmExit = false;
    public String resp = "";
    GridView gridview = null;
    private com.live.sports.utils.categoriesAdapter categoriesAdapter;
    SwipeRefreshLayout swipeLayout;
    private String adsData = "";

    AdView mAdView = null;

    public static Map<String, ArrayList<Channels>> appLiveData = new HashMap<String, ArrayList<Channels>>();
    public static ArrayList<Channels> appLiveDataCategories = new ArrayList<Channels>();
    private LinearLayout customAdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }
        });


        imageCache = BitmapCache.getInstance(this.getSupportFragmentManager());
        imageLoader = new ImageLoader(AppData.getImageRequestQueue(MainActivity.this), imageCache);
        Log.i("Creation", "Main Activity");

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Utils.isConnectedToInternet(MainActivity.this)) {
                    requestChannelsData();
                } else {
                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.not_connected),
                            Toast.LENGTH_SHORT).show();
                    if (swipeLayout.isRefreshing()) {
                        swipeLayout.setRefreshing(false);
                    }
                }

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 10000);
            }
        });


        gridview = (GridView) findViewById(R.id.gridview);
        categoriesAdapter = new categoriesAdapter(this, 0, appLiveDataCategories);
        gridview.setAdapter(categoriesAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String catName = ((Channels) parent.getAdapter().getItem(position)).categoryName;
                Intent i = new Intent(MainActivity.this, ChannelDetails.class);
                i.putExtra(Settings.SELECTED_CAT, catName);
                startActivity(i);


            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.RATE_US_PACKAGE_NAME));
                    startActivity(rateIntent);
                }
                if (position == 1) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{Settings.FEEDBACK_EMAIL});
                    i.putExtra(Intent.EXTRA_SUBJECT, Settings.FEEDBACK_EMAIL_SUBJECT);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        setupDrawer();
        addDrawerItems();

        mAdView = (AdView) findViewById(R.id.adView);


        if (AppData.liveData.size() == 0) {
            sendRequest();
        }


//        if (savedInstanceState == null) {
////            Initialize Leadbolt SDK with your api key
//            AppTracker.startSession(getApplicationContext(), Settings.LEADBOARD_AD_ID);
//        }
////        cache Leadbolt Ad without showing it
//        AppTracker.loadModuleToCache(getApplicationContext(), "inapp");


    }

    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    private void addDrawerItems() {
        ArrayList<String> data = new ArrayList<String>();
        data.add("Rate Us");
        data.add("FeedBack");
        mAdapter = new DrawerAdapter(this, data);
        mDrawerList.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        isClosed = true;
        AppData.isFirst = false;
        super.onDestroy();
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerLayout != null) {
            mDrawerToggle.syncState();

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerLayout != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void requestAdsData() {
        HashMap<String, String> params = new HashMap<String, String>();
        GeneralStringRequest request = new GeneralStringRequest(AppData.appUrls.get(Settings.JSON_ADS_LINKS), params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!isClosed) {
                    adsRequest();
                }
                closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        }, new Response.ParseListener<String>() {

            @Override
            public void onParse(String response) throws VolleyError {
                try {

                    String ads = Utils.decript_data(response, Settings.DECRIPTION_KEY);
                    JSONHelper.buildAdsData(ads);
                } catch (Exception e) {
                    throw new VolleyError(e);
                }

            }
        });


        AppData.getRequestQueue(this.getApplicationContext()).add(request);


    }

    private void requestChannelsData() {

        showProgressDialog("Downloading Live  Data");
        HashMap<String, String> params = new HashMap<String, String>();
        GeneralStringRequest request = new GeneralStringRequest(AppData.appUrls.get(Settings.JSON_LIVE_LINKS), params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!isClosed) {
                    appLiveDataCategories.clear();
                    appLiveDataCategories.addAll(AppData.liveDataCategories);
                    appLiveData.clear();
                    appLiveData.putAll(AppData.liveData);
                    categoriesAdapter.notifyDataSetChanged();
                    requestAdsData();
                }
                closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        }, new Response.ParseListener<String>() {

            @Override
            public void onParse(String response) throws VolleyError {
                try {

                    JSONHelper.buildLiveData(Utils.decript_data(response, Settings.LIVE_TOKEN));
                    resp = response;
                } catch (Exception e) {
                    throw new VolleyError(e);
                }

            }
        });


        AppData.getRequestQueue(this.getApplicationContext()).add(request);


    }


    private void sendRequest() {


        showProgressDialog("Downloading Data");
        HashMap<String, String> params = new HashMap<String, String>();
        GeneralStringRequest request = new GeneralStringRequest(Settings.MAIN_URL, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                closeProgressDialog();
                requestChannelsData();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        }, new Response.ParseListener<String>() {

            @Override
            public void onParse(String response) throws VolleyError {
                try {
                    JSONHelper.buildUrlList(Utils.decript_data(response, Settings.DECRIPTION_KEY));
                    resp = response;
                } catch (Exception e) {
                    throw new VolleyError(e);
                }

            }
        });


        AppData.getRequestQueue(this.getApplicationContext()).add(request);


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

            if (progDialog.isShowing()) {
                progDialog.dismiss();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (confirmExit) {
            destroyData();
            finish();
        }
        this.confirmExit = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                confirmExit = false;

            }
        }, 1500);
    }

    private void adsRequest() {
        if (AppData.admobStart) {
            if (AppData.isFirst)
                requestNewInterstitial();
        }
        if (AppData.appLovinStart) {
            if (AppLovinInterstitialAd.isAdReadyToDisplay(this)) {
                AppLovinInterstitialAd.show(this);

            }
        }
        if (AppData.admobtype1) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        } else if (Settings.CUSTOM_AD_VIEW_ENABLE) {
            customAdLayout.setVisibility(View.VISIBLE);
        }

    }


    private void destroyData() {
        AppData.admobStart = false;
        AppData.admobbeforeV = false;
        AppData.admobAfterV = false;
        AppData.appLovinStart = false;
        AppData.appLovinBeforeV = false;
        AppData.appLovinAfterV = false;
        AppData.showAd = false;
        AppData.isFirst = false;
        AppData.liveData.clear();
        AppData.liveDataCategories.clear();

    }

    @Override
    public ImageLoader getImageLoaderInstance() {
        return imageLoader;
    }
}
