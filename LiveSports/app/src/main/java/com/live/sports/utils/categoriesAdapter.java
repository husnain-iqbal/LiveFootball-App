package com.live.sports.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.live.sports.AppData;
import com.live.sports.R;
import com.live.sports.data.Channels;

import java.util.ArrayList;

/**
 * Created by tahirfazal on 01/07/15.
 */
public class categoriesAdapter extends BaseAdapter {

    private static Context mContext;
    private ArrayList<Channels> channels;

    public categoriesAdapter(Context context, int caller, ArrayList<Channels> data) {
        this.mContext = context;
        this.channels = data;
    }


    public int getCount() {
        return channels.size();
    }

    public Channels getItem(int position) {
//        if (channels.size() < position)
        return channels.get(position);
//        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_ite, null);

            // set image based on selected text

        } else {
            gridView = (View) convertView;
        }

        Channels channel = getItem(position);
        LinearLayout topLayout = (LinearLayout) gridView.findViewById(R.id.toplayout);

        NetworkImageView imageView = (NetworkImageView) gridView
                .findViewById(R.id.grid_item_image);

        if (channel != null) {
            TextView categoryTitle = (TextView) gridView.findViewById(R.id.title);
            categoryTitle.setText(channel.categoryName);
            categoryTitle.setTypeface(AppData.getMyriad_pro(mContext));
            if (!Utils.isStringEmpty(channel.categoryImageLink)) {
                imageView.setImageUrl(channel.categoryImageLink, ((ImageLoader.ImageLoaderProvider) mContext).getImageLoaderInstance());
            } else {
                imageView.setDefaultImageResId(R.drawable.place_holder);
            }

        }


        if (position < AppData.colorsList.size()) {
            topLayout.setBackgroundColor(Color.parseColor(AppData.colorsList.get(position)));
        } else {
            int pos = position % AppData.colorsList.size() - 1;
            topLayout.setBackgroundColor(Color.parseColor(AppData.colorsList.get(pos)));
        }

        adjustIvDimensions(imageView);

        return gridView;
    }


    public static void adjustIvDimensions(ImageView imageview) {
        android.view.ViewGroup.LayoutParams layoutparams = imageview.getLayoutParams();
        if (AppData.isTablet(mContext)) {
            layoutparams.height = AppData.getdimTabletShowThumbHeight(mContext) - 20;
        } else {
            layoutparams.height = AppData.getdimShowThumbHeight(mContext) - 20;
        }
        imageview.setLayoutParams(layoutparams);
        imageview.refreshDrawableState();
//        imageview.setPadding(40, 40, 40, 40);
    }

}
