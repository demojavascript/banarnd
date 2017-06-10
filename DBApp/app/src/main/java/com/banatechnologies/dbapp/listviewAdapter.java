package com.banatechnologies.dbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Rahul on 21-05-2016.
 */

public class listviewAdapter extends BaseAdapter {
    private int listviewTemp;
    private Context context;
    private List<Contact> objArr = null;
    private List<Contact> arraylist = null;
    public listviewAdapter(Context context, List<Contact> objArr, int listviewTemp){
        this.context = context;
        this.objArr = objArr;
        this.listviewTemp = listviewTemp;
        this.arraylist = objArr;
    }

    @Override
    public int getCount() {
        return objArr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objArr.clear();
        if (charText.length() == 0) {
            objArr.addAll(arraylist);
        }else {
            for (Contact wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    objArr.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if(convertView == null) {
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(listviewTemp, null);
            mHolder = new Holder();
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.contactno = (TextView) convertView.findViewById(R.id.tv_contactno);
            convertView.setTag(mHolder);
        }else{
            mHolder = (Holder)convertView.getTag();
        }
        Contact contact = objArr.get(position);
        mHolder.name.setText(contact.getName());
        mHolder.contactno.setText(contact.getPhoneNumber());
        return convertView;
    }
    public class Holder {
        TextView name;
        TextView contactno;
    }
}
