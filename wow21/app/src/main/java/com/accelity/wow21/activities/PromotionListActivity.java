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
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.PromotionListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Promotions;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class PromotionListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private PromotionListActivity fthis;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray promotionObject;
    private NetConnection netConnection;
    private ArrayList<Promotions> promotionsList;
    private RecyclerView rvPromotions;
    private RecyclerView.Adapter adapterPromotionList;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Boolean isRefreshing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Promotions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_promotionlist);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing) {
                    isRefreshing = true;
                    refreshContent();
                }
            }
        });

        promotionsList = new ArrayList<Promotions>();
        rvPromotions = (RecyclerView)findViewById(R.id.recycleview_promotionlist);
        layoutManager = new LinearLayoutManager(this);
        rvPromotions.setLayoutManager(layoutManager);
        rvPromotions.setHasFixedSize(true);
        adapterPromotionList = new PromotionListAdapter(promotionsList, this, rvPromotions);
        rvPromotions.setAdapter(adapterPromotionList);

        refreshContent();

    }
    private void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(PromotionListActivity.this);
        if(!networkDetails.isEmpty()) {
            new GetPromotionList().execute();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            isRefreshing = true;
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }

    class GetPromotionList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!isRefreshing) {
                pDialog = CProgress.ctor(PromotionListActivity.this);
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                promotionObject = jsonParser.getJSONFromUrlAsGET(URLManager.getUserPramotionURL() + "userid=" + user.getID()+"&orderamount=0");
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(!isRefreshing) {
                pDialog.dismiss();
            }else{
                isRefreshing = false;
            }
            if(promotionObject != null){
                if(promotionObject.length() > 0){
                    try {
                        promotionsList.clear();
                        for (int i = 0; i < promotionObject.length(); i++) {
                            Promotions objPromotion = new Promotions();
                            JSONObject objPro = promotionObject.getJSONObject(i);
                            objPromotion.setPromotionType(objPro.getString("type"));
                            objPromotion.setPromotionCode(objPro.getString("text"));
                            objPromotion.setPromotionDesc(objPro.getString("description"));
                            objPromotion.setPromotionFrom(CustomUtil.getFullDate(objPro.getString("startdate")));
                            objPromotion.setPromotionTo(CustomUtil.getFullDate(objPro.getString("enddate")));
                            promotionsList.add(objPromotion);
                        }
                        adapterPromotionList.notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.promotionlist_no), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pDialog!= null){
            pDialog.dismiss();
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
}
