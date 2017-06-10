package com.nitinguru.app.nitinguru.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.adapters.NoteListAdapter;
import com.nitinguru.app.nitinguru.database.ChapterDataSource;
import com.nitinguru.app.nitinguru.database.NotesDataSource;
import com.nitinguru.app.nitinguru.database.TopicDataSource;
import com.nitinguru.app.nitinguru.models.DBNotes;
import com.nitinguru.app.nitinguru.models.DBTopic;
import com.nitinguru.app.nitinguru.models.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private String topicname;
    private ArrayList<Notes> notes;
    private RecyclerView rv_notelist;
    private RecyclerView.Adapter adapterNote;
    private RecyclerView.LayoutManager layoutManagerNote;
    private Button btnQuiz;
    private long chapterid, topicid;
    private NotesDataSource noteDB;
    private TopicDataSource topicdb;
    private ChapterDataSource chapterdb;
    private List<DBNotes> allNotes;
    private List<DBTopic> allTopics;
    private int countView;
    private LinearLayout quizLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        quizLayout = (LinearLayout)findViewById(R.id.quizLayout);
        noteDB = new NotesDataSource(this);
        chapterdb = new ChapterDataSource(this);
        topicdb = new TopicDataSource(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        btnQuiz = (Button)findViewById(R.id.btnQuiz);
        btnQuiz.setOnClickListener(this);
        notes = new ArrayList<Notes>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (!bundle.isEmpty()) {
                allNotes = new ArrayList<DBNotes>();
                topicname = intent.getExtras().getString("topic");
                topicid = intent.getExtras().getLong("topicid");
                chapterid = intent.getExtras().getLong("chapterid");
                noteDB.open();
                topicdb.open();
                chapterdb.open();
                allNotes = noteDB.getAllNotesById(topicid);
                if(topicdb.getTopicd(topicid).getView() == 0) {
                    topicdb.updateTopic(topicid);
                    int oldvisit = chapterdb.getChapter(chapterid).getVisits();
                    chapterdb.updateChapter(oldvisit+1, chapterid);
                }
                allTopics = new ArrayList<DBTopic>();
                allTopics = topicdb.getAllTopicById(chapterid);
                for(int h=0;h<allTopics.size();h++){
                    if(allTopics.get(h).getView() == 1){
                        countView += 1;
                    }
                }
                Log.d("countView", countView+"  "+allTopics.size());
                if(countView == allTopics.size() ){
                    quizLayout.setVisibility(View.VISIBLE);
                }

                setToolbar(topicname);
                rv_notelist = (RecyclerView)findViewById(R.id.recycleview_noteslist);
                layoutManagerNote = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rv_notelist.setLayoutManager(layoutManagerNote);
                rv_notelist.setHasFixedSize(true);
                adapterNote = new NoteListAdapter(allNotes, this, rv_notelist);
                rv_notelist.setAdapter(adapterNote);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnQuiz){
            Intent intent = new Intent(NotesActivity.this, QuizActivity.class);
            intent.putExtra("chapterid", chapterid);
            startActivity(intent);
            finish();
        }
    }
}
