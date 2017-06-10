package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.etc.CustomGrid;
import com.accelity.wow21.etc.DealGridView;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.HomeDeal;
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

public class MoreDealActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView leftTime;
    private CustomGrid dealAdapter;
    private DealGridView dealGrid;
    private ArrayList<HomeDeal> homeDeal;
    private User user;
    private UserPref userPref;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonDeals;
    private NetConnection netConnection;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountDownTimer countDownTimer;
    private String str_endDate;
    private CardView timerCard;
    private MoreDealActivity fthis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_deal);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("WOW Deals");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_moredeallist);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        timerCard = (CardView)findViewById(R.id.timerCard);

        homeDeal = new ArrayList<HomeDeal>();
        dealAdapter = new CustomGrid(MoreDealActivity.this, homeDeal);
        dealGrid = (DealGridView) findViewById(R.id.gridProduct);
        dealGrid.setNumColumns(2);
        dealGrid.setAdapter(dealAdapter);

        leftTime = (TextView)findViewById(R.id.leftTime);

        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(MoreDealActivity.this);
        if(!networkDetails.isEmpty()) {
            new GettingDeals().execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }


    }

    class GettingDeals extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MoreDealActivity.this);
            pDialog.setMessage("Deals Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeDealsUrl() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonDeals = jsonParser.getUrlContents(newURL);
            Log.d("newURL " , jsonDeals+"" );
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            try {
                if (jsonDeals != null) {
                    if(jsonDeals.getJSONArray("deals").length() > 0) {
                        homeDeal.clear();
                        JSONArray dealData = jsonDeals.getJSONArray("deals");
                        HomeDeal deal;
                        timerCard.setVisibility(View.VISIBLE);
                        for (int k = 0; k < dealData.length(); k++) {
                            if(k==0){
                                str_endDate = dealData.getJSONObject(k).getString("enddate").toString();
                                if(CustomUtil.totalTimeDiff(str_endDate) != 0) {
                                    countDownTimer = new CountDownTimer(CustomUtil.totalTimeDiff(str_endDate), 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            leftTime.setText(CustomUtil.getAcualTimeLeft(str_endDate));
                                        }

                                        public void onFinish() {
                                            countDownTimer.cancel();
                                            leftTime.setText("Timeout");
                                        }
                                    }.start();
                                }
                            }
                            deal = new HomeDeal(dealData.getJSONObject(k).getString("variantID").toString(),
                                    dealData.getJSONObject(k).getString("title").toString(),
                                    dealData.getJSONObject(k).getString("dealid").toString(),
                                    dealData.getJSONObject(k).getString("deal_name").toString(),
                                    dealData.getJSONObject(k).getString("deal_desc").toString(),
                                    dealData.getJSONObject(k).getString("variantIcon").toString(),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("maxPerCustomer").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("stockavailabe").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("totalqty").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("mrp").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("sellingprice").toString()),
                                    dealData.getJSONObject(k).getString("discount").toString(),
                                    Boolean.parseBoolean(dealData.getJSONObject(k).getString("is_super").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("superdealminprice").toString()),
                                    null,
                                    null,
                                    0, "", ""
                            );
                            homeDeal.add(deal);
                        }
                        dealAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    private void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(MoreDealActivity.this);
        if(!networkDetails.isEmpty()) {
            new GettingDealsRefresh().execute();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }

    class GettingDealsRefresh extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeDealsUrl() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonDeals = jsonParser.getUrlContents(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            mSwipeRefreshLayout.setRefreshing(false);
            try {
                if (jsonDeals != null) {
                    if(jsonDeals.getJSONArray("deals").length() > 0) {
                        homeDeal.clear();
                        JSONArray dealData = jsonDeals.getJSONArray("deals");
                        HomeDeal deal;
                        homeDeal.clear();
                        for (int k = 0; k < dealData.length(); k++) {
                            if(k==0){
                                countDownTimer.cancel();
                                str_endDate = dealData.getJSONObject(k).getString("enddate").toString();
                                if(CustomUtil.totalTimeDiff(str_endDate) != 0) {
                                    countDownTimer = new CountDownTimer(CustomUtil.totalTimeDiff(str_endDate), 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            leftTime.setText(CustomUtil.getAcualTimeLeft(str_endDate));
                                        }
                                        public void onFinish() {
                                            countDownTimer.cancel();
                                            leftTime.setText("Timeout");
                                        }
                                    }.start();
                                }
                            }
                            deal = new HomeDeal(dealData.getJSONObject(k).getString("variantID").toString(),
                                    dealData.getJSONObject(k).getString("title").toString(),
                                    dealData.getJSONObject(k).getString("dealid").toString(),
                                    dealData.getJSONObject(k).getString("deal_name").toString(),
                                    dealData.getJSONObject(k).getString("deal_desc").toString(),
                                    dealData.getJSONObject(k).getString("variantIcon").toString(),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("maxPerCustomer").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("stockavailabe").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("totalqty").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("mrp").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("sellingprice").toString()),
                                    dealData.getJSONObject(k).getString("discount").toString(),
                                    Boolean.parseBoolean(dealData.getJSONObject(k).getString("is_super").toString()),
                                    Double.parseDouble(dealData.getJSONObject(k).getString("superdealminprice").toString()),
                                    null,
                                    null,
                                    0, "", ""
                            );
                            homeDeal.add(deal);
                        }
                        dealAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
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
