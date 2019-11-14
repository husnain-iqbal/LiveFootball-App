package com.live.sports;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.applovin.sdk.AppLovinSdk;
import com.live.sports.data.Channels;
import com.live.sports.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AppData extends Application {

    public static boolean isFirst = true;

    public static boolean admobStart = false;
    public static boolean admobbeforeV = false;
    public static boolean admobAfterV = false;
    public static boolean admobMiddle = false;
    public static boolean appLovinStart = false;
    public static boolean appLovinBeforeV = false;
    public static boolean appLovinAfterV = false;
    public static boolean appLovinMiddle = false;
    public static boolean showAd = false;

    public static boolean admobtype1 = false;
    public static boolean admobtype2Top = false;
    public static boolean admobtype2Bottom = false;

    public static final String CACHE_DIR = "images";
    public static RequestQueue requestQueue;
    public static RequestQueue imageRequestQueue;
    public static ArrayList<String> colorsList = new ArrayList<String>(Arrays.asList("#B4D766",
            "#BC89A3", "#D77D90", "#AAD7C7", "#4DB3B9", "#EB973D", "#BBAF4D", "#E3566B", "#AAD7C7",
            "#4DB3B9", "#BC89A3", "#B4D766", "#BC89A3", "#D77D90", "#AAD7C7", "#4DB3B9", "#EB973D",
            "#BC89A3", "#D77D90", "#AAD7C7", "#4DB3B9", "#EB973D", "#BC89A3", "#D77D90", "#AAD7C7",
            "#4DB3B9", "#EB973D", "#BC89A3", "#D77D90", "#AAD7C7", "#4DB3B9", "#EB973D", "#BC89A3",
            "#D77D90", "#AAD7C7", "#4DB3B9", "#EB973D", "#BC89A3", "#D77D90", "#AAD7C7", "#4DB3B9",
            "#EB973D")
    );
    public static Map<String, ArrayList<Channels>> liveData = new HashMap<String, ArrayList<Channels>>();
    public static ArrayList<Channels> liveDataCategories = new ArrayList<Channels>();
    public static int deviceScreenWidth;
    public static int deviceScreenHeight;
    private static int dimAppThumbSize = -1;
    private static int dimShowThumbWidth = -1;
    private static int dimShowFullThumbWidth = -1;
    private static int dimShowThumbHeight = -1;
    private static int dimShowWallpaperWidth = -1;
    private static int dimShowWallpaperHeight = -1;
    private static int dimTabletShowThumbWidth = -1;
    private static int dimTabletShowThumbHeight = -1;
    private static float density = 0;
    private static Typeface myriad_pro = null;
    private static Typeface fontAwesome = null;
    public static Hashtable<String, String> appUrls = new Hashtable<>();


    public static Typeface getfontAwesome(Context mcontext) {
        if (fontAwesome == null) {
            return Typeface.createFromAsset(mcontext.getAssets(), "fontawesome-webfont.ttf");
        }
        return fontAwesome;
    }

    public static void setdensity(float value) {
        density = value;
    }


    private static Typeface getFont(Context caller, Typeface font, String fontname) {
        if (font == null) {
            font = Typeface.createFromAsset(caller.getAssets(), fontname);
        }
        return font;
    }

    public static Typeface getMyriad_pro(Context caller) {
        return getFont(caller, AppData.myriad_pro, Settings.FONT_myriadPro);
    }

    public static AppLovinSdk getApplovinSdk(Context context) {
        return AppLovinSdk.getInstance(context.getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLovinSdk.initializeSdk(this);
    }

    private static void calculateDeviceScreenWidth(Context parent) {
        AppData.setdensity(parent.getResources().getDisplayMetrics().density);
        Configuration conf = parent.getResources().getConfiguration();

        AppData.deviceScreenWidth = parent.getResources().getDisplayMetrics().widthPixels;
        AppData.deviceScreenHeight = parent.getResources().getDisplayMetrics().heightPixels;

        if (AppData.deviceScreenWidth > AppData.deviceScreenHeight) // If For
        {
            int temp = AppData.deviceScreenWidth;
            AppData.deviceScreenWidth = AppData.deviceScreenHeight;
            AppData.deviceScreenHeight = temp;
        }
    }

    public static void loadSetAppGfxDims(Context parent) {
        calculateDeviceScreenWidth(parent);
        String cachedDimensions = ((SharedPreferences) parent
                .getSharedPreferences(Settings.INSTANCE_NAME, 0)).getString(
                Settings.Prefs.APP_DIMENSIONS_JSON, "");
        if (!isStringEmpty(cachedDimensions)) // Re-use the cached
        {
            try {
                JSONObject obj = new JSONObject(cachedDimensions);
                AppData.dimAppThumbSize = obj
                        .getInt(Settings.JSON_DIM_APP_THUMB_SIZE);
                AppData.dimShowFullThumbWidth = obj
                        .getInt(Settings.JSON_DIM_APP_THUMB_SIZE_FULL);
                AppData.dimShowThumbWidth = obj
                        .getInt(Settings.JSON_DIM_SHOW_THUMB_WIDTH);
                AppData.dimShowThumbHeight = obj
                        .getInt(Settings.JSON_DIM_SHOW_THUMB_HEIGHT);
                AppData.dimShowWallpaperWidth = obj
                        .getInt(Settings.JSON_DIM_SHOW_WALLPAPER_WIDTH);
                AppData.dimShowWallpaperHeight = obj
                        .getInt(Settings.JSON_DIM_SHOW_WALLPAPER_HEIGHT);
                AppData.dimTabletShowThumbWidth = obj
                        .getInt(Settings.JSON_DIM_TABLET_BULLETIN_THUMB_WIDTH);
                AppData.dimTabletShowThumbHeight = obj
                        .getInt(Settings.JSON_DIM_TABLET_BULLETIN_THUMB_HEIGHT);

            } catch (JSONException e) {
                calcSetAppGfxDims(parent);
            }
        } else {
            calcSetAppGfxDims(parent);
        }

    }

    private static void checkValue(int value, Context caller) {
        if (value <= 0) {
            loadSetAppGfxDims(caller);
        }

    }

    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void calcSetAppGfxDims(Context parent) {

        final int deviceScreenWidth = AppData.deviceScreenWidth;

        AppData.dimShowFullThumbWidth = deviceScreenWidth;
        AppData.dimShowThumbWidth = ((int) (deviceScreenWidth - (AppData.density * 15))) / 2;
        AppData.dimShowThumbHeight = (int) (AppData.dimShowThumbWidth / Settings.N_IMAGE_ASPECT_RATIO);

        AppData.dimAppThumbSize = (Settings.N_PRECENT_SIZE_APPS_THUMB_SIZE * deviceScreenWidth) / 100;

        AppData.dimShowWallpaperWidth = deviceScreenWidth;
        AppData.dimShowWallpaperHeight = (int) (AppData.dimShowWallpaperWidth / 1.60);

        AppData.dimTabletShowThumbWidth = ((int) (deviceScreenWidth - (AppData.density * 20))) / 3;
        AppData.dimTabletShowThumbHeight = (int) (AppData.dimTabletShowThumbWidth / Settings.N_IMAGE_ASPECT_RATIO);


        JSONObject obj = new JSONObject();
        try {
            obj.put(Settings.JSON_DIM_APP_THUMB_SIZE_FULL, AppData.dimShowFullThumbWidth);
            obj.put(Settings.JSON_DIM_APP_THUMB_SIZE, AppData.dimAppThumbSize);
            obj.put(Settings.JSON_DIM_SHOW_THUMB_WIDTH, AppData.dimShowThumbWidth);
            obj.put(Settings.JSON_DIM_SHOW_THUMB_HEIGHT,
                    AppData.dimShowThumbHeight);
            obj.put(Settings.JSON_DIM_SHOW_WALLPAPER_WIDTH,
                    AppData.dimShowWallpaperWidth);
            obj.put(Settings.JSON_DIM_SHOW_WALLPAPER_HEIGHT,
                    AppData.dimShowWallpaperHeight);
            obj.put(Settings.JSON_DIM_TABLET_BULLETIN_THUMB_WIDTH,
                    AppData.dimTabletShowThumbWidth);
            obj.put(Settings.JSON_DIM_TABLET_BULLETIN_THUMB_HEIGHT,
                    AppData.dimTabletShowThumbHeight);

            String jsonAppDims = obj.toString();
            SharedPreferences prefs = parent.getSharedPreferences(
                    Settings.INSTANCE_NAME, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Settings.Prefs.APP_DIMENSIONS_JSON, jsonAppDims);
            editor.commit(); // Time to commit changes.

        } catch (Exception exc) {
            Log.i("Dottech",
                    "Exception Occured While making json array of the App dimensions");
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getdimAppThumbSize(Context caller) {
        checkValue(dimAppThumbSize, caller);
        return dimAppThumbSize;
    }

    public static int getdimShowThumbWidth(Context caller) {
        checkValue(dimShowThumbWidth, caller);
        return dimShowThumbWidth;
    }

    public static int getdimShowThumbHeight(Context caller) {
        checkValue(dimShowThumbHeight, caller);
        return dimShowThumbHeight;
    }

    public static int getdimTabletShowThumbHeight(Context caller) {
        checkValue(dimTabletShowThumbHeight, caller);
        return dimTabletShowThumbHeight;
    }

    public static int getdimShowFullThumbWidth(Context caller) {
        checkValue(dimShowFullThumbWidth, caller);
        return dimShowFullThumbWidth;
    }


    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = createRequestQueue(context);
        }
        return requestQueue;
    }

    public static RequestQueue getImageRequestQueue(Context context) {
        if (imageRequestQueue == null) {
            imageRequestQueue = createImageRequestQueue(context);
        }
        return imageRequestQueue;
    }


    private static RequestQueue createRequestQueue(Context context) {
        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new NoCache();
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        return queue;

    }

    private static RequestQueue createImageRequestQueue(Context context) {
        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new DiskBasedCache(getDiskCacheDir(context, CACHE_DIR), Settings.CACHE_MAX_SIZE_IN_BYTES);
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        return queue;

    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    (!Environment.isExternalStorageRemovable()))
                cachePath = context.getExternalCacheDir().getPath();
            else
                cachePath = context.getCacheDir().getPath();
        } catch (Exception e) {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


}
