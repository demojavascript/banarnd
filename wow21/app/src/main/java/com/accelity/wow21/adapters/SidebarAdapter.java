package com.accelity.wow21.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.etc.DatabaseHandler;

/**
 * Created by Rahul on 29-08-2016.
 */
public class SidebarAdapter extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String[] titles;
    DatabaseHandler db;
    public SidebarAdapter(Context c, String[] menuOptions, int sideIcons[]){
        super(c, R.layout.sidebar_list_layout, R.id.option_txt, menuOptions);
        this.context = c;
        this.images = sideIcons;
        this.titles = menuOptions;
        db = new DatabaseHandler(c);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.sidebar_list_layout, parent, false);
        ImageView imgview = (ImageView)row.findViewById(R.id.option_img);
        TextView txtview = (TextView)row.findViewById(R.id.option_txt);
        TextView tcBadge = (TextView)row.findViewById(R.id.tv_badge);
        if(position == 4){
            if(db.getNotificationUnRead() > 0){
                tcBadge.setVisibility(View.VISIBLE);
                tcBadge.setText(db.getNotificationUnRead()+"");
            }
        }

        imgview.setImageResource(images[position]);
        txtview.setText(titles[position]);
        return row;
    }
}