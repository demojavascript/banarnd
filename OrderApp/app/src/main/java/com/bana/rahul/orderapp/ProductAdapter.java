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
public class ProductAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Products> products;

    public ProductAdapter(Context c, ArrayList<Products> products) {
        this.mContext = c;
        this.products = products;
    }

    public int getCount() {
        return products.size();
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
            child = layoutInflater.inflate(R.layout.custom_list_products, null);
            mHolder = new Holder();
            mHolder.txt_productname = (TextView) child.findViewById(R.id.list_product_name);
            mHolder.txt_productop = (TextView) child.findViewById(R.id.list_product_cp);
            mHolder.txt_productcp = (TextView) child.findViewById(R.id.list_product_op);
            mHolder.txt_productoff = (TextView) child.findViewById(R.id.list_product_off);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        String prod_name = products.get(pos).getName()+"";
        String prod_op = "Rs. "+products.get(pos).getCp()+"";
        String prod_cp = "Rs. "+products.get(pos).getOp()+"";
        String prod_off = products.get(pos).getOff()+"% OFF";
        mHolder.txt_productname.setText(prod_name);
        mHolder.txt_productop.setText(prod_op);
        mHolder.txt_productcp.setText(prod_cp);
        mHolder.txt_productoff.setText(prod_off);
        return child;
    }

    public class Holder {
        TextView txt_productname;
        TextView txt_productop;
        TextView txt_productcp;
        TextView txt_productoff;
    }
}
