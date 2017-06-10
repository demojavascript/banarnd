package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.OrderListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Order;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {

    private Boolean isFromOrderSuccess = false;
    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private OrderListActivity fthis;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray orderListObject;
    private NetConnection netConnection;
    private ArrayList<Order> objOrders, objCancelOrder, objConfirmOrder;
    private RecyclerView rvOrder;
    private RecyclerView.Adapter adapterOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private ViewPager view_pager;
    private OrderListActivity.ViewPagerAdapter adapter;
    private TextView tv_noOrder;
    private TabLayout tab_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.MyOrders_title));
        tv_noOrder = (TextView) findViewById(R.id.tv_noOrder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (!bundle.isEmpty()) {
                isFromOrderSuccess = intent.getBooleanExtra("isSuccess", false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        objOrders = new ArrayList<Order>();
        objCancelOrder = new ArrayList<Order>();
        objConfirmOrder = new ArrayList<Order>();
        rvOrder = (RecyclerView)findViewById(R.id.recycleview_orderlist);
        layoutManager = new LinearLayoutManager(this);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setHasFixedSize(true);
        adapterOrderList = new OrderListAdapter(objOrders, this, rvOrder);
        rvOrder.setAdapter(adapterOrderList);
        refreshContent();
    }

    public void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(OrderListActivity.this);
        if (!networkDetails.isEmpty()) {
            new GettingOrderList().execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }

    class GettingOrderList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                pDialog = CProgress.ctor(OrderListActivity.this);
                pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                orderListObject = jsonParser.getJSONFromUrlAsGET(URLManager.getUserOrderListURL() + "&userid=" + user.getID());
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(orderListObject != null){
                if(orderListObject.length() > 0){
                    tv_noOrder.setVisibility(View.GONE);
                    try {
                        objOrders.clear();
                        objCancelOrder.clear();
                        objConfirmOrder.clear();

                        for(int i=0;i<orderListObject.length();i++){
                            JSONObject objOrder = orderListObject.getJSONObject(i);
                            Order morder = new Order();
                            morder.setOrderno(objOrder.getString("orderno"));
                            morder.setDate(CustomUtil.getDate(objOrder.getString("whendate"))+" "+CustomUtil.getTime(objOrder.getString("whendate")));
                            morder.setOrderid(objOrder.getString("orderid"));
                            morder.setOrderstatus(Integer.parseInt(objOrder.getString("orderstatus")));
                            morder.setOrderStatusDesc(objOrder.getString("statusdescription"));
                            morder.setPaymenttype(objOrder.getString("paymenttype"));
                            morder.setStorname(objOrder.getString("storename"));
                            String tmnt = CustomUtil.convertTo2Decimal(Double.parseDouble(objOrder.getString("amount")));
                            morder.setTotalamount(Double.parseDouble(tmnt));
                            morder.setTotaldeal(Integer.parseInt(objOrder.getString("orderquantity")));
                            String tmntsave = CustomUtil.convertTo2Decimal(Double.parseDouble(objOrder.getString("totalsavings")));
                            morder.setTotalsavings(Double.parseDouble(tmntsave));
                            morder.setuserid(objOrder.getString("userid"));
                            morder.setOrderrating(Integer.parseInt(objOrder.getString("rating")));
                            objOrders.add(morder);
                            if(morder.getOrderstatus() == 1){
                                objConfirmOrder.add(morder);
                            }else if(morder.getOrderstatus() == 2){
                                objCancelOrder.add(morder);
                            }
                        }
                        adapterOrderList.notifyDataSetChanged();
                        activateToolbar();
                    }catch(Exception e){
                        e.printStackTrace();
                        tv_noOrder.setVisibility(View.VISIBLE);
                    }
                }else{
                    tv_noOrder.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_noorderfound), Toast.LENGTH_LONG).show();
                }
            }else{
                tv_noOrder.setVisibility(View.VISIBLE);
            }
        }
    }

    public ArrayList<Order> getOrders(int status){
        ArrayList<Order> mOrder = objOrders;
        if(status == 0){
            mOrder = objOrders;
        }else if(status == 1){
            mOrder = objConfirmOrder;
        }else if(status == 2){
            mOrder = objCancelOrder;
        }else{
            mOrder = objOrders;
        }
        return mOrder;
    }
    public void activateToolbar(){
        view_pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllOrderFragment(), getResources().getString(R.string.order_tab1));
        adapter.addFragment(new ConfirmOrderFragment(), getResources().getString(R.string.order_tab2));
        adapter.addFragment(new CancelOrderFragment(), getResources().getString(R.string.order_tab3));
        //adapter.addFragment(new AddedPassbookFragment(), "Added");
        //adapter.add
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(0);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.setVisibility(View.VISIBLE);
        view_pager.setVisibility(View.VISIBLE);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    @Override
    public void onBackPressed() {
        if(isFromOrderSuccess){
            Intent intent  = new Intent(OrderListActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else {
            super.onBackPressed();
            finish();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if(isFromOrderSuccess){
                Intent intent  = new Intent(OrderListActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
