package com.live.sports.utils;

/**
 * Created by tahirfazal on 01/07/15.
 */
public class Settings {


    public static final String INSTANCE_NAME = "Live Sports TV";
    public final static String JSON_DIM_SHOW_THUMB_WIDTH = "JSON_DIM_SHOW_THUMB_WIDTH";
    public final static String JSON_DIM_SHOW_THUMB_HEIGHT = "JSON_DIM_SHOW_THUMB_HEIGHT";
    public final static String JSON_DIM_SHOW_WALLPAPER_WIDTH = "JSON_DIM_SHOW_WALLPAPER_WIDTH";
    public final static String JSON_DIM_SHOW_WALLPAPER_HEIGHT = "JSON_DIM_SHOW_WALLPAPER_HEIGHT";
    public final static String JSON_DIM_TABLET_BULLETIN_THUMB_WIDTH = "JSON_DIM_TAB_BULLETIN_THUMB_WIDTH";
    public final static String JSON_DIM_TABLET_BULLETIN_THUMB_HEIGHT = "JSON_DIM_TAB_BULLETIN_THUMB_HEIGHT";
    public final static String JSON_DIM_APP_THUMB_SIZE = "JSON_DIM_APP_THUMB_SIZE";
    public final static String JSON_DIM_APP_THUMB_SIZE_FULL = "JSON_DIM_APP_THUMB_SIZE_FULL";
    public static final int CACHE_MAX_SIZE_IN_BYTES = 20 * 1024 * 1024; //20MB
    public final static int N_PRECENT_SIZE_APPS_THUMB_SIZE = 30;    // 25
    public final static double N_IMAGE_ASPECT_RATIO = 1; // Ratio that has to be maintained while determining the App's Image/Gfx dimensions.
    public final static String SELECTED_CAT = "selected_cat";
    public static final String FONT_myriadPro = "MyriadPro-Regular.otf";

    public static final String STARTUP_AD_ID = "ca-app-pub-3628814949671769/2184907931";
    public static final String LEADBOARD_AD_ID = "7IjVWMYpdRQZDYjCPAfnj2EmbdPHxWC1";

    public interface Prefs {
        public final static String APP_DIMENSIONS_JSON = "APP_DIMENSIONS_JSON";
    }


    public final static String INSTANCE_NAME_VERBOSE = "Sports TV";
    public final static String DECRIPTION_KEY = "CMS5P@ssword786";
    //"exp3!99B0]n3P0s#s.w0rd";
    public final static String LIVE_TOKEN = "CMS5P@ssword786";
    //"Putty99B0n3P@ssword786";
//    public final static String MAIN_URL = "http://alirazasoft.com/CMS5/cms/XVer/getConttV1-0.php";
    public final static String MAIN_URL = "http://cms27.iptvsale.com/CMS27/i856tre/getConttV1-0.php";
    public final static String CUSTOM_AD_VIEW_FILE_URL = "file:///android_asset/watch_now_button.gif";
    public final static String CUSTOM_AD_VIEW_OPEN_URL = "http://adsrvmedia.adk2x.com/imp?p=75160948&ct=html&ap=1303";
    public final static String FEEDBACK_EMAIL = "cricketcrazeios@gmail.com";
    public final static String FEEDBACK_EMAIL_SUBJECT = "Live Streming app";
    public final static String RATE_US_PACKAGE_NAME = "market://details?id=com.jakejiagames.live.sports.tv";
    //           "http://alirazasoft.com/AlirazaCms/cms/XVer/getConttV1-0.php";

    //JSON Keys
    public final static String JSON_ADS_LINKS = "AdsLink";
    public final static String JSON_APP_CONTENT_LINKS = "AppContLink";
    public final static String JSON_LIVE_LINKS = "LiveLink";
    public final static Boolean CUSTOM_AD_VIEW_ENABLE = true;


    public class ADS_SETTINGS {
        public final static String JSON_ADMOB_START = "start:admob";
        public final static String JSON_ADMOB_BEFORE_VIDEO = "beforevideo:admob";
        public final static String JSON_ADMOB_AFTER_VIDEO = "aftervideo:admob";
        public final static String JSON_ADMOB_MIDDLE = "middle:admob";

        public final static String JSON_ADMOB_type1 = "type1:admob";
        public final static String JSON_ADMOB_type2 = "type2:admobbottom";
        public final static String JSON_ADMOB_type2_TOP = "type2:admobtop";

        public final static String JSON_APPLOVIN_START = "start:applovin";
        public final static String JSON_APPLOVIN_BEFORE_VIDEO = "beforevideo:applovin";
        public final static String JSON_APPLOVIN_AFTER_VIDEO = "aftervideo:applovin";
        public final static String JSON_APPLOVIN_MIDDLE = "middle:applovin";

    }

}
