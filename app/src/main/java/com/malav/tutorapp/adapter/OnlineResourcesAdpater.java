package com.malav.tutorapp.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malav.tutorapp.R;
import com.malav.tutorapp.beans.OnlineResources;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shahmalav on 08/03/17.
 */

public class OnlineResourcesAdpater extends BaseAdapter{

    Context context;
    private LayoutInflater inflater;
    ArrayList<OnlineResources> listOnlineResources;
    OnlineResources onlineResources;
    private String TAG = "OnlineResourcesAdapter";

    public OnlineResourcesAdpater(Context context, ArrayList<OnlineResources> listOnlineResources) {
        this.listOnlineResources = listOnlineResources;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listOnlineResources.size();
    }

    @Override
    public Object getItem(int location) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_online_resources, null);

        onlineResources = listOnlineResources.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView url = (TextView) convertView.findViewById(R.id.url);
        TextView description = (TextView) convertView.findViewById(R.id.desc);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        title.setText(onlineResources.getTitle());
        url.setText(onlineResources.getUrl());
        description.setText(onlineResources.getDescription());
        date.setText(onlineResources.getDate());

        return convertView;
    }
}
