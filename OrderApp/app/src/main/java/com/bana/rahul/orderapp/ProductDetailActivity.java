package com.bana.rahul.orderapp;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ProductDetailActivity extends ActionBarActivity {

    Toolbar toolbar;
    ListView objnearList;
    StoreDispAdapter storeAdapter;
    private ArrayList<Stores> objStores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        objStores = new ArrayList<Stores>();
        objStores.add(new Stores("Store 1", "Noida", "2.1"));
        objStores.add(new Stores("Store 2", "Meerut", "4.1"));
        objStores.add(new Stores("Store 3", "Mayur Vihar", "5.1"));
        objStores.add(new Stores("Store 4", "Vaishali", "3.1"));
        objStores.add(new Stores("Store 5", "MohanNagar", "1.1"));
        objnearList = (ListView)findViewById(R.id.nearStore);
        storeAdapter = new StoreDispAdapter(this, objStores);
        objnearList.setAdapter(storeAdapter);
        setListViewHeightBasedOnChildren(objnearList);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("Product Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
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
