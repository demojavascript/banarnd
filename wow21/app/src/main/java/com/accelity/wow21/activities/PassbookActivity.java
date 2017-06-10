package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.PassbookListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Passbook;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PassbookActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private PassbookActivity fthis;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private String walletObject;
    private Double walletAmount = 0.0;
    private JSONArray passBookObject;
    private NetConnection netConnection;
    private ArrayList<Passbook> passbookList, paidPassbookList, receivedPassbookList;
    private RecyclerView rvPassbook;
    private RecyclerView.Adapter adapterPassbookList;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tv_totalbalance;
    private LinearLayout lout_ewallet_balance;


    private ViewPager view_pager;
    private LinearLayout tabbox, lout_notrans;
    ViewPagerAdapter adapter;
    TabLayout tab_layout;
    private RelativeLayout rlout_walletbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Passbook_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlout_walletbox = (RelativeLayout) findViewById(R.id.rlout_walletbox);

        lout_ewallet_balance = (LinearLayout)findViewById(R.id.lout_ewallet_balance);
        tabbox = (LinearLayout)findViewById(R.id.tabbox);
        lout_notrans = (LinearLayout)findViewById(R.id.lout_notrans);


        tv_totalbalance = (TextView)findViewById(R.id.tv_totalbalance);

        passbookList = new ArrayList<Passbook>();
        paidPassbookList = new ArrayList<Passbook>();
        receivedPassbookList = new ArrayList<Passbook>();
        rvPassbook = (RecyclerView)findViewById(R.id.recycleview_passbooklist);
        layoutManager = new LinearLayoutManager(this);
        rvPassbook.setLayoutManager(layoutManager);
        rvPassbook.setHasFixedSize(true);
        adapterPassbookList = new PassbookListAdapter(passbookList, this, rvPassbook);
        rvPassbook.setAdapter(adapterPassbookList);

        refreshContent();

    }

    public void activateToolbar(){
        view_pager = (ViewPager) findViewById(R.id.pager);

        adapter = new ViewPagerAdapter (getSupportFragmentManager());
        adapter.addFragment(new AllPassbookFragment(), "All");
        adapter.addFragment(new PaidPassbookFragment(), "Paid");
        adapter.addFragment(new ReceivedPassbookFragment(), "Received");
        //adapter.addFragment(new AddedPassbookFragment(), "Added");
        //adapter.add
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(0);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public ArrayList<Passbook> getAllData(int status){
        ArrayList<Passbook> mPassbook = passbookList;
        if(status ==1){
            mPassbook = passbookList;
        }else if(status == 2){
            mPassbook = paidPassbookList;
        }else if(status == 3){
            mPassbook = receivedPassbookList;
        }else{
            mPassbook = passbookList;
        }
        return mPassbook;
    }

    private void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(PassbookActivity.this);
        if(!networkDetails.isEmpty()) {
            new eWalletLoading().execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }

    class eWalletLoading extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PassbookActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                walletObject = connectionFromServer(URLManager.getUsereWalletUrl() + "userid=" + user.getID());
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(PassbookActivity.this);
            if (!networkDetails.isEmpty()) {
                new GetPassbook().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
            if(walletObject != null){
                rlout_walletbox.setVisibility(View.VISIBLE);
                if(Double.parseDouble(walletObject.toString()) > 0.0){
                    lout_ewallet_balance.setVisibility(View.VISIBLE);
                    walletAmount = Double.parseDouble(walletObject.toString());
                    tv_totalbalance.setText(walletAmount+"");
                }else{
                    walletAmount = 0.0;
                    tv_totalbalance.setText(walletAmount+"");
                }
            }
        }
    }

    class GetPassbook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PassbookActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                passBookObject = jsonParser.getJSONFromUrlAsGET(URLManager.getPassbookURL() + "userid=" + user.getID()+"&lowestid=0");
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(passBookObject != null){
                if(passBookObject.length() > 0){
                    passbookList.clear();
                    tabbox.setVisibility(View.VISIBLE);
                    try {
                        for (int i = 0; i < passBookObject.length(); i++) {
                            JSONObject objbook = passBookObject.getJSONObject(i);
                            Passbook passbook = new Passbook();
                            passbook.setId(objbook.getString("id"));
                            passbook.setTransid(objbook.getString("tranid"));
                            passbook.setAppname(objbook.getString("appname"));
                            passbook.setUserid(objbook.getString("userid"));
                            passbook.setSource(objbook.getString("source"));
                            passbook.setSourceno(objbook.getString("sourceno"));
                            passbook.setOpeningbalance(Float.parseFloat(objbook.getString("openingbalance")));
                            passbook.setDebit(Float.parseFloat(objbook.getString("debit")));
                            passbook.setCredit(Float.parseFloat(objbook.getString("credit")));
                            passbook.setCurrentbalance(Float.parseFloat(objbook.getString("currentbalance")));
                            passbook.setRemark(objbook.getString("remark"));
                            passbook.setWhendate(CustomUtil.getDate(objbook.getString("createddate"))+" "+CustomUtil.getTime(objbook.getString("createddate")));
                            passbookList.add(passbook);
                            if(Float.parseFloat(objbook.getString("debit")) > 0){
                                paidPassbookList.add(passbook);
                            }else{
                                receivedPassbookList.add(passbook);
                            }
                        }
                        adapterPassbookList.notifyDataSetChanged();
                        Log.d("passbookList", passbookList.size()+"");
                        activateToolbar();
                    }catch(Exception e){
                        e.printStackTrace();
                        lout_notrans.setVisibility(View.VISIBLE);
                    }
                }else{
                    lout_notrans.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.passbook_notransfound), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static String connectionFromServer(String url) throws IOException {
        String result = null;
        try {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line + "\n");

            result = sb.toString(); // Result is here

            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
