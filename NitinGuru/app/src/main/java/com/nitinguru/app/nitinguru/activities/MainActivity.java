package com.nitinguru.app.nitinguru.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.adapters.ChapterGridAdapter;
import com.nitinguru.app.nitinguru.adapters.SidebarAdapter;
import com.nitinguru.app.nitinguru.components.NavigationDrawerFragment;
import com.nitinguru.app.nitinguru.database.ChapterDataSource;
import com.nitinguru.app.nitinguru.gridviews.ChapterGridView;
import com.nitinguru.app.nitinguru.models.Chapter;
import com.nitinguru.app.nitinguru.models.DBChapter;
import com.nitinguru.app.nitinguru.models.Notes;
import com.nitinguru.app.nitinguru.models.Topic;
import com.nitinguru.app.nitinguru.utils.CustMessages;
import com.nitinguru.app.nitinguru.utils.ErrorMessages;
import com.nitinguru.app.nitinguru.utils.Helper;
import com.nitinguru.app.nitinguru.utils.NetConnection;
import com.nitinguru.app.nitinguru.utils.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    private ListView listviewsidebar;
    private SidebarAdapter sidebarAdapter;
    private String[] menuOptions;
    private DrawerLayout drawerLayout;
    private NetConnection netConnection;
    private MainActivity fthis;
    private ChapterGridAdapter chapterGridAdapter;
    private ChapterGridView chapterGridView;
    private ArrayList<Chapter> chapters;
    private List<DBChapter> allChapter;
    private ChapterDataSource datasource;
    private int[] menuIcons = {
            R.drawable.ic_menu, R.drawable.ic_menu,R.drawable.ic_menu,R.drawable.ic_menu,R.drawable.ic_menu,R.drawable.ic_menu,R.drawable.ic_menu};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fthis = this;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(CustMessages.getAppName(fthis));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        datasource = new ChapterDataSource(this);
        datasource.open();
        allChapter = datasource.getAllChapter();

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        Resources objres = getResources();
        menuOptions = objres.getStringArray(R.array.menuOptions);
        listviewsidebar = (ListView) findViewById(R.id.sidebar_menu);
        sidebarAdapter = new SidebarAdapter(this, menuOptions, menuIcons);
        listviewsidebar.setAdapter(sidebarAdapter);
        listviewsidebar.setOnItemClickListener(this);

        chapters = new ArrayList<Chapter>();
        try{
            chapters = loadChapters();
            chapterGridAdapter = new ChapterGridAdapter(MainActivity.this, allChapter);
            chapterGridView = (ChapterGridView) findViewById(R.id.gridChapter);
            chapterGridView.setNumColumns(2);
            chapterGridView.setAdapter(chapterGridAdapter);
            chapterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, TopicsActivity.class);
                    intent.putExtra("topics", chapters.get(position).getTopic());
                    intent.putExtra("chapter",chapters.get(position).getName());
                    intent.putExtra("chapterid",allChapter.get(position).getId());
                    startActivity(intent);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onResume() {
        allChapter = datasource.getAllChapter();
        chapters = loadChapters();
        chapterGridAdapter = new ChapterGridAdapter(MainActivity.this, allChapter);
        chapterGridView = (ChapterGridView) findViewById(R.id.gridChapter);
        chapterGridView.setNumColumns(2);
        chapterGridView.setAdapter(chapterGridAdapter);
        chapterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TopicsActivity.class);
                intent.putExtra("topics", chapters.get(position).getTopic());
                intent.putExtra("chapter",chapters.get(position).getName());
                intent.putExtra("chapterid",allChapter.get(position).getId());
                startActivity(intent);
            }
        });
        super.onResume();
    }



    private ArrayList<Chapter> loadChapters(){
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        try{
            JSONArray objAll = Helper.loadJSON(this, "chapters").getJSONArray("Chapters");
            for(int i=0;i<objAll.length();i++) {
                JSONObject objChapter = objAll.getJSONObject(i);
                JSONArray arrTopics = objChapter.getJSONArray("topics");
                ArrayList<Topic> topics = new ArrayList<Topic>();
                for(int k=0;k<arrTopics.length();k++) {
                    JSONObject objTopic = arrTopics.getJSONObject(k);
                    JSONArray arrNotes = objTopic.getJSONArray("notes");
                    ArrayList<Notes> notes = new ArrayList<Notes>();
                    for(int j=0;j<arrNotes.length();j++){
                        JSONObject objNote = arrNotes.getJSONObject(j);
                        notes.add(new Notes(objNote.getString("title"), objNote.getString("desc")));
                    }
                    topics.add(new Topic(objTopic.getString("name"), objTopic.getString("id"), notes));
                }
                chapters.add(new Chapter(objChapter.getString("id"),objChapter.getString("name"), topics));
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return  chapters;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        switch(i){
            case 0:
                Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(MainActivity.this, ContentActivity.class);
                startActivity(intent3);
                break;
            case 3:
                netConnection = new NetConnection();
                Map<String, String> networkDetails5 = netConnection.getConnectionDetails(MainActivity.this);
                if(!networkDetails5.isEmpty()) {
                    feedbackMail();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                shareApp();
                break;
            case 5:
                callUs();
                break;
            case 6:
                netConnection = new NetConnection();
                Map<String, String> networkDetails3 = netConnection.getConnectionDetails(MainActivity.this);
                if(!networkDetails3.isEmpty()) {
                    feedbackPlayStore();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void feedbackPlayStore(){
        Uri uri = Uri.parse(URLManager.getPlayStoreMarketURL());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            fthis.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            fthis.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(URLManager.getPlayStoreURL())));
        }
    }
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_google_play_url));
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }
    public void callUs(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+CustMessages.getCustomerCareNo(fthis)));
        startActivity(intent);
    }
    public void feedbackMail(){
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        //String allBody = CustMessages.getFeedbackMailStringBody(fthis)+""+user.getMobile();
        String allBody = CustMessages.getFeedbackMailStringBody(fthis);
        Uri data = Uri.parse("mailto:?subject=" + CustMessages.getFeedbackMailSubject(fthis) + "&body=" + allBody + "&to=" + CustMessages.getFeedbackMailTO(fthis));
        testIntent.setData(data);
        startActivity(testIntent);
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
}
