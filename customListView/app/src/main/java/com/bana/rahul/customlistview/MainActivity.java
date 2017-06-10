package com.bana.rahul.customlistview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;


public class MainActivity extends Activity {

    String REST_URL = "http://api.androidhive.info/contacts/";
    ListView testList;
    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    Integer[] imgid={
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
            R.drawable.icon1,
    };
    private ProgressDialog pDialog;
    private static String url = "http://api.androidhive.info/contacts/";
    JSONParser jsonParser = new JSONParser();
    TextView objtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testList = (ListView)findViewById(R.id.listTest);
        objtxt = (TextView)findViewById(R.id.txt_All);
        new RestService().execute();
    }
    class RestService extends AsyncTask<String, String, String> {
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting Service...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(REST_URL, "GET", null);
            Log.d("Login Successful!", json.toString());
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            objtxt.setText(file_url.toString());
        }
    }
}
