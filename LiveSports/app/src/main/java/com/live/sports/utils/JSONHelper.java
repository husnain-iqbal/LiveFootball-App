package com.live.sports.utils;


import com.google.gson.Gson;
import com.live.sports.AppData;
import com.live.sports.data.Channels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class JSONHelper {

    public static void buildUrlList(String stringJSON) throws JSONException {
        JSONObject jsonObj = null;
        JSONArray ListJSON = null;
        Object json = new JSONTokener(stringJSON).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jb = new JSONObject(stringJSON);
            ListJSON = jb.getJSONArray("data");

        } else if (json instanceof JSONArray) ListJSON = new JSONArray(stringJSON);

        for (int i = 0; i < ListJSON.length(); i++) {

            jsonObj = ListJSON.getJSONObject(i);
            AppData.appUrls.put(Settings.JSON_ADS_LINKS, jsonObj.getString(Settings.JSON_ADS_LINKS));
            AppData.appUrls.put(Settings.JSON_LIVE_LINKS, jsonObj.getString(Settings.JSON_LIVE_LINKS));
            AppData.appUrls.put(Settings.JSON_APP_CONTENT_LINKS, jsonObj.getString(Settings.JSON_APP_CONTENT_LINKS));

        }

    }

    public static void buildAdsData(String stringJSON) throws JSONException {
        JSONObject jsonObj = null;
        JSONArray ListJSON = null;
        Object json = new JSONTokener(stringJSON).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jb = new JSONObject(stringJSON);
            ListJSON = jb.getJSONArray("data");

        } else if (json instanceof JSONArray) ListJSON = new JSONArray(stringJSON);

        for (int i = 0; i < ListJSON.length(); i++) {

            jsonObj = ListJSON.getJSONObject(i);
            AppData.admobStart = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_START);
            AppData.admobbeforeV = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_BEFORE_VIDEO);
            AppData.admobAfterV = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_AFTER_VIDEO);
            AppData.admobMiddle = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_MIDDLE);

            AppData.admobtype1 = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_type1);

            AppData.admobtype2Bottom = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_type2);

            AppData.admobtype2Top = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_ADMOB_type2_TOP);


            AppData.appLovinStart = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_APPLOVIN_START);
            AppData.appLovinBeforeV = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_APPLOVIN_BEFORE_VIDEO);
            AppData.appLovinAfterV = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_APPLOVIN_AFTER_VIDEO);
            AppData.appLovinMiddle = checkValueAndAssign(jsonObj, Settings.ADS_SETTINGS.JSON_APPLOVIN_MIDDLE);
        }

    }

    public static boolean checkValueAndAssign(JSONObject jsonObj, String checkKey) throws JSONException {
        if (jsonObj.has(checkKey)) {
            if (jsonObj.getString(checkKey).equals("True")) {
                return true;
            }
        }
        return false;
    }

    public static void buildLiveData(String stringJSON) throws JSONException {
        Gson gson = new Gson();
        Channels[] data = gson.fromJson(stringJSON, Channels[].class);
        if (AppData.liveData.size() > 0) {
            AppData.liveData.clear();
            AppData.liveDataCategories.clear();
        }

        for (Channels d : data) {
            if (d.catStatus.equals("On Air")) {
                if (d.channelStatus.equals("On Air")) {
                    if (AppData.liveData.containsKey(d.categoryName)) {
                        AppData.liveData.get(d.categoryName).add(d);
                        Collections.sort(AppData.liveData.get(d.categoryName), new Comparator<Channels>() {
                            public int compare(Channels p1, Channels p2) {
                                return Integer.valueOf(p1.channelPriority).compareTo(p2.channelPriority);
                            }
                        });
                    } else {
                        ArrayList<Channels> channelList = new ArrayList<Channels>();
                        channelList.add(d);
                        AppData.liveData.put(d.categoryName, channelList);
                        AppData.liveDataCategories.add(d);
                    }
                }
            }
        }

        Collections.sort(AppData.liveDataCategories, new Comparator<Channels>() {
            public int compare(Channels p1, Channels p2) {
                return Integer.valueOf(p1.catPriority).compareTo(p2.catPriority);
            }
        });

    }


}