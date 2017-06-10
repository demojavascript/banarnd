package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.accelity.wow21.BuildConfig;
import com.accelity.wow21.R;
import com.accelity.wow21.adapters.NotificationListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.etc.DatabaseHandler;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Notification;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private NotificationListActivity fthis;
    private ArrayList<Notification> notificationsList;
    private RecyclerView rvNotification;
    private RecyclerView.Adapter adapterNotificationList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHandler db;
    private Button btn_updateApp;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jNotification;
    private CardView update_bx;
    private NetConnection netConnection;
    private int versionCode = BuildConfig.VERSION_CODE;
    private String versionName = BuildConfig.VERSION_NAME;
    private RelativeLayout relOutNoNotify;
    private int nottCount = 0;
    private Boolean fromHome = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Notifications_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        db.syncNotification();
        //db.deleteNotification();
        ShortcutBadger.removeCount(fthis);

        update_bx = (CardView) findViewById(R.id.update_bx);

        btn_updateApp = (Button) findViewById(R.id.btn_updateApp);
        btn_updateApp.setOnClickListener(this);

        notificationsList = new ArrayList<Notification>();

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (!bundle.isEmpty()) {
                fromHome = intent.getBooleanExtra("fromhome", false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        relOutNoNotify = (RelativeLayout) findViewById(R.id.relOutNoNotify);

        rvNotification = (RecyclerView)findViewById(R.id.recycleview_notificationlist);
        layoutManager = new LinearLayoutManager(this);
        rvNotification.setLayoutManager(layoutManager);
        rvNotification.setHasFixedSize(true);
        adapterNotificationList = new NotificationListAdapter(notificationsList, this, rvNotification);
        rvNotification.setAdapter(adapterNotificationList);

        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(NotificationListActivity.this);
        if(!networkDetails.isEmpty()) {
            new GetNotificationBanner().execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }

    }

    class GetNotificationBanner extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(NotificationListActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                jNotification = jsonParser.getUrlContents(URLManager.getNotificationBanner() + "lat="+user.getLat()+"&long="+user.getLong());
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(jNotification != null){
                try {
                    if(!versionName.equals(jNotification.getString("version"))){
                        update_bx.setVisibility(View.VISIBLE);
                    }
                    if (jNotification.getJSONArray("banner").length() > 0) {
                        for(int h=0; h<jNotification.getJSONArray("banner").length(); h++){
                            Notification objNot = new Notification();
                            objNot.setTitle("");
                            objNot.setDesc("");
                            objNot.setIsClickable(false);
                            objNot.setDate(CustomUtil.getFullDate("/Date(1473238903543)/"));
                            objNot.setScreenId("99");
                            objNot.setLargeIMGURL(jNotification.getJSONArray("banner").get(h).toString());
                            objNot.setSmallIMGURL("");
                            notificationsList.add(objNot);
                        }
                    }else{
                        relOutNoNotify.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    relOutNoNotify.setVisibility(View.VISIBLE);
                }
            }else{
                relOutNoNotify.setVisibility(View.VISIBLE);
            }
            for(int h=0;h<db.getAllNotification().size(); h++){
                Notification notif = db.getAllNotification().get(h);
                notificationsList.add(notif);
            }
            adapterNotificationList.notifyDataSetChanged();
            if(notificationsList.size() < 1){
                relOutNoNotify.setVisibility(View.VISIBLE);
            }else{
                relOutNoNotify.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(fromHome){
            super.onBackPressed();
            finish();
        }else {
            Intent intent  = new Intent(NotificationListActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if(!fromHome){
                Intent intent  = new Intent(NotificationListActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_updateApp){
            Uri uri = Uri.parse(URLManager.getPlayStoreMarketURL());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                fthis.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                fthis.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URLManager.getPlayStoreURL())));
            }
        }
    }
}
