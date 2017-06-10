package com.nitinguru.app.nitinguru.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.adapters.TopicListAdapter;
import com.nitinguru.app.nitinguru.database.TopicDataSource;
import com.nitinguru.app.nitinguru.models.DBTopic;
import com.nitinguru.app.nitinguru.models.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_topiclist;
    private RecyclerView.Adapter adapterTopic;
    private RecyclerView.LayoutManager layoutManagerTopic;
    private ArrayList<Topic> topics;
    private String chaptername;
    private List<DBTopic> allTopics;
    private TopicDataSource topicdb;
    private long chapterid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        topicdb = new TopicDataSource(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        topics = new ArrayList<Topic>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (!bundle.isEmpty()) {
                topics = (ArrayList<Topic>)intent.getSerializableExtra("topics");
                chaptername = intent.getExtras().getString("chapter");
                chapterid = intent.getExtras().getLong("chapterid");
                topicdb.open();
                allTopics = topicdb.getAllTopicById(chapterid);
                setToolbar(chaptername);
                rv_topiclist = (RecyclerView)findViewById(R.id.recycleview_topiclist);
                layoutManagerTopic = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rv_topiclist.setLayoutManager(layoutManagerTopic);
                rv_topiclist.setHasFixedSize(true);
                adapterTopic = new TopicListAdapter(allTopics, this, rv_topiclist);
                rv_topiclist.setAdapter(adapterTopic);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setToolbar(String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topic_menu, menu);
        return true;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return super.onOptionsItemSelected(item);
            case R.id.videos:
                startActivity(new Intent(this, VideosActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
