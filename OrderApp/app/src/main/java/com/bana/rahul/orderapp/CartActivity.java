package com.bana.rahul.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CartActivity extends ActionBarActivity {

    ListView lvCartProduct;
    ArrayList<Cart> objCart;
    CartAdapter cartAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Your Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        objCart = new ArrayList<Cart>();
        objCart.add(new Cart("Nestle 1", "20", "2"));
        objCart.add(new Cart("Nestle 2", "200", "1"));
        objCart.add(new Cart("Nestle 3", "20", "2"));
        objCart.add(new Cart("Nestle 4", "200", "1"));
        objCart.add(new Cart("Nestle 5", "20", "2"));
        lvCartProduct = (ListView)findViewById(R.id.list_cart_products);
        cartAdapter = new CartAdapter(this, objCart);
        lvCartProduct.setAdapter(cartAdapter);
        setListViewHeightBasedOnChildren(lvCartProduct);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart){
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
