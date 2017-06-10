package com.nitinguru.app.nitinguru.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.database.ChapterDataSource;
import com.nitinguru.app.nitinguru.database.NotesDataSource;
import com.nitinguru.app.nitinguru.database.TopicDataSource;
import com.nitinguru.app.nitinguru.models.Chapter;
import com.nitinguru.app.nitinguru.models.DBChapter;
import com.nitinguru.app.nitinguru.models.DBNotes;
import com.nitinguru.app.nitinguru.models.DBTopic;
import com.nitinguru.app.nitinguru.models.Notes;
import com.nitinguru.app.nitinguru.models.Topic;
import com.nitinguru.app.nitinguru.models.UserPref;
import com.nitinguru.app.nitinguru.utils.Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private UserPref userPref;
    private DBChapter dbChapter;
    private DBTopic dbTopic;
    private ArrayList<Chapter> chapters;
    private ChapterDataSource datasource;
    private TopicDataSource topicdb;
    private NotesDataSource notesdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userPref = new UserPref(this);
        if(userPref.getUser_firstkey().length()<1){

            chapters = new ArrayList<Chapter>();
            chapters = loadChapters();

            datasource = new ChapterDataSource(this);
            topicdb = new TopicDataSource(this);
            notesdb = new NotesDataSource(this);
            datasource.open();
            topicdb.open();
            notesdb.open();

            for(int i=0;i<chapters.size();i++){
                Chapter chapter = chapters.get(i);
                dbChapter = datasource.createChapter(new DBChapter(chapter.getName(), chapter.getTopic().size()));
                for(int k=0;k<chapter.getTopic().size();k++){
                    dbTopic = topicdb.createTopic(new DBTopic(dbChapter.getId(), chapter.getTopic().get(k).getName()));
                    for(int m=0;m<chapter.getTopic().get(k).getNotes().size();m++){
                        DBNotes note = new DBNotes(chapter.getTopic().get(k).getNotes().get(m).getTitle(), chapter.getTopic().get(k).getNotes().get(m).getDesc(), dbTopic.getId());
                        notesdb.createNotes(note);
                    }
                }
            }
            userPref.setUser_firstkey("1");
        }
        startGo();
    }
    public void startGo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent objintent;
                if(userPref.getUser_mob().length() <1){
                    objintent = new Intent(SplashActivity.this, LoginActivity.class);
                }else {
                    objintent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(objintent);
                finish();
            }
        }, SPLASH_TIME_OUT);
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
}
