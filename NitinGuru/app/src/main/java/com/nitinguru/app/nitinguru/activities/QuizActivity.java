package com.nitinguru.app.nitinguru.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.libs.JSONParser;
import com.nitinguru.app.nitinguru.models.Quiz;
import com.nitinguru.app.nitinguru.models.UserPref;
import com.nitinguru.app.nitinguru.utils.ErrorMessages;
import com.nitinguru.app.nitinguru.utils.NetConnection;
import com.nitinguru.app.nitinguru.utils.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private UserPref userPref;
    private JSONObject json;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private NetConnection netConnection;
    private QuizActivity fthis;
    private long chapterid;
    private String levelid = "1";
    private ArrayList<Quiz> allQuiz;
    private RadioGroup radio_group;
    private TextView tv_quiz, tv_Score, tv_noquiz, tv_quiztitle;
    private LinearLayout loutQuiz, loutScore;
    private int currQuizIndex = 0;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        fthis = this;
        userPref = new UserPref(this);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Quiz");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loutQuiz = (LinearLayout)findViewById(R.id.loutQuiz);
        loutScore = (LinearLayout)findViewById(R.id.loutScore);
        tv_noquiz = (TextView)findViewById(R.id.tv_noquiz);
        tv_quiztitle = (TextView)findViewById(R.id.tv_quiztitle);
        tv_quiz = (TextView)findViewById(R.id.tv_quiz);
        tv_Score = (TextView)findViewById(R.id.tv_Score);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (!bundle.isEmpty()) {
                chapterid = intent.getExtras().getLong("chapterid");
                netConnection = new NetConnection();
                Map<String, String> networkDetails3 = netConnection.getConnectionDetails(QuizActivity.this);
                if (!networkDetails3.isEmpty()) {
                    new QuizActivity.AttemptQuiz().execute();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(this), Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }


    public void setQuiz(){
        Quiz quiz = allQuiz.get(currQuizIndex);
        radio_group.removeAllViews();
        tv_quiz.setText(quiz.getTitle());
        for (int i = 0; i < quiz.getOptions().size(); i++) {
            RadioButton rbn = new RadioButton(this);
            rbn.setId(i+0);

            rbn.setText((i+1)+". "+quiz.getOptions().get(i).toString());
            rbn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allQuiz.get(currQuizIndex).setAns(view.getId());
                    btnLogin.setEnabled(true);
                }
            });
            radio_group.addView(rbn);
        }
        btnLogin.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==  R.id.btnLogin){
            if(currQuizIndex == (allQuiz.size()-1)){
                loutQuiz.setVisibility(View.GONE);
                submitQuiz();
            }else{
                currQuizIndex++;
                setQuiz();
            }
        }
    }

    void submitQuiz(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails3 = netConnection.getConnectionDetails(QuizActivity.this);
        if(!networkDetails3.isEmpty()) {
            new PostQuiz().execute();
        }else{
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(this), Toast.LENGTH_LONG).show();
        }
    }

    class PostQuiz extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fthis);
            pDialog.setMessage("Quiz Posting");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                JSONObject jObj = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                for (int i = 0; i < allQuiz.size(); i++) {
                    jsonArr.put(allQuiz.get(i).getAns());
                }
                jObj.put("ans", jsonArr);
                jObj.put("usermob", userPref.getUser_mob());
                jObj.put("chapterid", chapterid);
                jObj.put("levelid", levelid);
                json = jsonParser.makeHttpRequestJSON(URLManager.postQuizByChapterAndLevel(), "POST", jObj);
                Log.d("jsonresp", json.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            try{
                if(json.getInt("status") == 1) {
                    tv_quiztitle.setVisibility(View.GONE);
                    tv_Score.setText(json.getString("data").toString());
                    loutScore.setVisibility(View.VISIBLE);
                }
            }catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }

    class AttemptQuiz extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fthis);
            pDialog.setMessage("Quiz loading");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                json = jsonParser.getUrlContents(URLManager.getQuizByChapterAndLevel(chapterid+"", levelid));
                Log.d("jsonresp", json.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            allQuiz = new ArrayList<Quiz>();
            try{
                if(json.getInt("status") == 1){
                    if(json.getJSONArray("data").length() > 0){
                        for(int i=0;i<json.getJSONArray("data").length();i++){
                            JSONObject jsonQuiz = json.getJSONArray("data").getJSONObject(i);
                            Quiz quiz = new Quiz();
                            quiz.setId(jsonQuiz.getString("id"));
                            quiz.setTitle(jsonQuiz.getString("title"));
                            JSONArray objOptions = new JSONArray(jsonQuiz.getString("options"));
                            ArrayList<String> optionsAll = new ArrayList<String>();
                            for(int h=0;h<objOptions.length();h++){
                                optionsAll.add(objOptions.get(h).toString());
                            }
                            quiz.setOptions(optionsAll);
                            allQuiz.add(quiz);
                            if(allQuiz != null && allQuiz.size() > 0){
                                currQuizIndex = 0;
                                setQuiz();
                                loutQuiz.setVisibility(View.VISIBLE);
                                tv_quiztitle.setVisibility(View.VISIBLE);
                            }else{
                                tv_noquiz.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        tv_noquiz.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }catch(Exception ee){
                ee.printStackTrace();
            }

        }
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
    protected void onDestroy() {
        if(pDialog != null){
            pDialog.dismiss();
        }
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
