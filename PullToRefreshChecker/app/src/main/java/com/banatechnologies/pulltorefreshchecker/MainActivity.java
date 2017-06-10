package com.banatechnologies.pulltorefreshchecker;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<String>  mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mListView = (ListView) findViewById(R.id.activity_main_listview);
        //mListView.setAdapter(new ArrayAdapter<String>(){
            String[] fakeTweets = getResources().getStringArray(R.array.cat_names);
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fakeTweets);
            mListView.setAdapter(mAdapter);
        //});

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getNewCatNames());
                mListView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            };
        }, 5000);
    }

    private List<String> getNewCatNames() {
        List<String> newCatNames = new ArrayList<String>();
        String[] mCatNames = getResources().getStringArray(R.array.cat_names);
        for (int i = 0; i < mCatNames.length; i++) {
            int randomCatNameIndex = new Random().nextInt(mCatNames.length - 1);
            newCatNames.add(mCatNames[randomCatNameIndex]);
        }
        return newCatNames;
    }
}
