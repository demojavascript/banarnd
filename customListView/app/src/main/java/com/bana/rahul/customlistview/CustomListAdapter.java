package com.bana.rahul.customlistview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rahul on 15-01-2016.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<HashMap<String, String>> itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, ArrayList<HashMap<String, String>> itemname, Integer[] imgid) {
        super(context, R.layout.custom_list);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_cust);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_cust);



        txtTitle.setText(itemname.get(position).get("name"));
        imageView.setImageResource(imgid[0]);
        return rowView;

    }
}