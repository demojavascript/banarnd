package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.StoreListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Store;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class StoreListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_storelist;
    private RecyclerView.Adapter adapterStore;
    private RecyclerView.LayoutManager layoutManagerStore;
    private User user;
    private UserPref userPref;
    private ProgressDialog pDialog = null;
    private ArrayList<Store> allStore;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonStore;
    private NetConnection netConnection;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private StoreListActivity fthis;
    private Boolean isRefreshing = false;
    private TextView tv_nostore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        fthis = this;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.StoreList_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tv_nostore = (TextView) findViewById(R.id.tv_nostore);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_storelist);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing) {
                    isRefreshing = true;
                    refreshContent();
                }
            }
        });
        userPref = new UserPref(this);
        user = userPref.getUser(this);

        allStore = new ArrayList<Store>();
        rv_storelist = (RecyclerView)findViewById(R.id.recycleview_storelist);
        layoutManagerStore = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_storelist.setLayoutManager(layoutManagerStore);
        rv_storelist.setHasFixedSize(true);
        adapterStore = new StoreListAdapter(allStore, this, rv_storelist);
        rv_storelist.setAdapter(adapterStore);
        refreshContent();
    }
    private void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(StoreListActivity.this);
        if(!networkDetails.isEmpty()) {
            new GettingStores().execute();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            isRefreshing = true;
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }
    class GettingStores extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!isRefreshing) {
                pDialog = CProgress.ctor(StoreListActivity.this);
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeStoreListURL() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonStore = jsonParser.getJSONFromUrlAsGET(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(!isRefreshing) {
                pDialog.dismiss();
            }else{
                isRefreshing = false;
            }
            try {
                if (jsonStore != null) {
                    if (jsonStore.length() > 0) {
                        rv_storelist.setVisibility(View.VISIBLE);
                        Store objSt;
                        allStore.clear();
                        for (int k = 0; k < jsonStore.length(); k++) {
                            JSONObject jobj;
                            try {
                                jobj = jsonStore.getJSONObject(k);
                                objSt = new Store(  jobj.getString("storeName"),
                                        "",
                                        jobj.getString("distance"),
                                        jobj.getString("storeID"),
                                        jobj.getString("storeIcon"),
                                        jobj.getString("warehouseID"),
                                        jobj.getString("storeAddress"),
                                        jobj.getString("storeContact"),
                                        jobj.getString("latitude"),
                                        jobj.getString("longitude"),
                                        jobj.getString("closingday")
                                );
                                allStore.add(objSt);
                            } catch (JSONException ex) {

                            }
                        }
                        adapterStore.notifyDataSetChanged();
                    }else{
                        //tv_nostore.setVisibility(View.VISIBLE);
                        //rv_storelist.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoStore(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //tv_nostore.setVisibility(View.VISIBLE);
                    //rv_storelist.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoStore(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                //tv_nostore.setVisibility(View.VISIBLE);
                //rv_storelist.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pDialog!= null){
            pDialog.dismiss();
        }
    }
}
