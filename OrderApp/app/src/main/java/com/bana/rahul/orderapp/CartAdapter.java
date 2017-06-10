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
public class CartAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Cart> cartItem;

    public CartAdapter(Context c, ArrayList<Cart> cartItem) {
        this.mContext = c;
        this.cartItem = cartItem;
    }

    public int getCount() {
        return cartItem.size();
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
            child = layoutInflater.inflate(R.layout.custom_cart_layout, null);
            mHolder = new Holder();
            mHolder.txt_cartpname = (TextView) child.findViewById(R.id.cart_product_name);
            mHolder.txt_cartpprice = (TextView) child.findViewById(R.id.cart_product_price);
            mHolder.txt_cartpqty = (TextView) child.findViewById(R.id.cart_product_quantity);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        String cart_prod_name = cartItem.get(pos).getPname() + "";
        String cart_prod_price = "Rs. "+cartItem.get(pos).getPprice()+"";
        String cart_prod_qty = cartItem.get(pos).getPqty()+"";
        mHolder.txt_cartpname.setText(cart_prod_name);
        mHolder.txt_cartpprice.setText(cart_prod_price);
        mHolder.txt_cartpqty.setText(cart_prod_qty);
        return child;
    }

    public class Holder {
        TextView txt_cartpname;
        TextView txt_cartpprice;
        TextView txt_cartpqty;
    }
}
