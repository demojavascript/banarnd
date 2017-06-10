package com.bana.rahul.orderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rahul on 29-01-2016.
 */

public class StoreDispAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Stores> store;
    public StoreDispAdapter(Context c, ArrayList<Stores> store) {
        this.mContext = c;
        this.store = store;
    }

    public int getCount() {
        return store.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.store_custom_list, null);
            mHolder = new Holder();
            mHolder.txt_storename = (TextView) child.findViewById(R.id.store_list_name);
            mHolder.txt_storecity = (TextView) child.findViewById(R.id.store_list_city);
            mHolder.txt_storedist = (TextView) child.findViewById(R.id.store_list_distance);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        String name = store.get(pos).getName()+"";
        String city = store.get(pos).getCity()+"";
        String distance = store.get(pos).getDistance()+"";
        mHolder.txt_storename.setText(name);
        mHolder.txt_storecity.setText(city);
        mHolder.txt_storedist.setText(distance);
        return child;
    }

    public class Holder {
        TextView txt_storename;
        TextView txt_storecity;
        TextView txt_storedist;
    }

}