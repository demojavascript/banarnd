package com.nitinguru.app.nitinguru.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.adapters.VideoListAdapter;
import com.nitinguru.app.nitinguru.models.Video;
import com.nitinguru.app.nitinguru.utils.Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_videolist;
    private RecyclerView.Adapter adapterVideo;
    private RecyclerView.LayoutManager layoutManagerVideo;
    private ArrayList<Video> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Videos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videos = new ArrayList<Video>();
        try{
            videos = loadVideos();
            rv_videolist = (RecyclerView)findViewById(R.id.recycleview_videolist);
            layoutManagerVideo = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv_videolist.setLayoutManager(layoutManagerVideo);
            rv_videolist.setHasFixedSize(true);
            adapterVideo = new VideoListAdapter(videos, this, rv_videolist);
            rv_videolist.setAdapter(adapterVideo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<Video> loadVideos(){
        ArrayList<Video> videos = new ArrayList<Video>();
        try{
            JSONArray objAll = Helper.loadJSON(this, "video").getJSONArray("videos");
            for(int i=0;i<objAll.length();i++) {
                JSONObject obj = objAll.getJSONObject(i);
                videos.add(new Video(obj.getString("title"),obj.getString("url"), obj.getString("key")));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return  videos;
    }

    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
