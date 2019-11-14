package com.live.sports.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.live.sports.AppData;
import com.live.sports.R;

import java.util.List;

/**
 * Created by tahirfazal on 21/06/15.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mHadees;

    public DrawerAdapter(Context context, List<String> objects) {
        super(context, R.layout.categories_row_item, objects);
        this.mContext = context;
        this.mHadees = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.drawer_item, null);
        }

        String task = mHadees.get(position);

        TextView icon = (TextView) convertView.findViewById(R.id.icon);
        icon.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf"));
        if (position == 0) {
            icon.setText(getContext().getString(R.string.icon_rate_us));
        }
        if (position == 1) {
            icon.setText(getContext().getString(R.string.icon_feedback));
        }
        TextView descriptionView = (TextView) convertView.findViewById(R.id.task_description);
        descriptionView.setText(task);
        descriptionView.setTypeface(AppData.getMyriad_pro(mContext));
        return convertView;
    }

}
