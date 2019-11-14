package com.live.sports.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.live.cryptorlib.org.cryptonode.jncryptor.AES256JNCryptor;
import com.live.cryptorlib.org.cryptonode.jncryptor.CryptorException;

import java.io.UnsupportedEncodingException;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.decode;

/**
 * Created by tahirfazal on 04/04/16.
 */
public class Utils {

    public static String buildMediaURL(String url) {
        byte[] data = decode(url, DEFAULT);
        String text = null;
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String decript_data(String response, String token) {

        AES256JNCryptor cryptor = new AES256JNCryptor();

        try {

            byte[] decoded_data = Base64.decode(response, 1);
            byte[] decription = cryptor.decryptData(decoded_data, token.toCharArray());
            return new String(decription, "UTF-8");

        } catch (CryptorException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";

    }

    //    public static void playUrl(Context parent, VideoListItem videoitem, boolean live) throws UnsupportedEncodingException
//    {
//        Intent i = new Intent(parent, ExoPlayerActivity.class);
//        i.putExtra("videoitem", videoitem);
//        i.putExtra("live", live);
//        parent.startActivity(i);
//    }
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || str.length() == 0;
    }

    public static boolean isConnectedToInternet(Context parent) {
        ConnectivityManager connectivity = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

}
