package com.accelity.wow21.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.OrderListAdapter;
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
import java.util.Map;


public class ConfirmOrderFragment extends Fragment {

    private ArrayList<Order> allOrder;
    private RecyclerView rvOrder;
    private RecyclerView.Adapter adapterOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private NetConnection netConnection;
    private User user;

    private JSONParser jsonParser = new JSONParser();
    private JSONArray orderListObject;
    private UserPref userPref;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPref = new UserPref(getActivity());
        user = userPref.getUser(getActivity());
        allOrder = new ArrayList<Order>();
        OrderListActivity fgh = (OrderListActivity)getActivity();
        allOrder = fgh.getOrders(1);
        rvOrder = (RecyclerView)getActivity().findViewById(R.id.recycleview_confirmOrderList);
        adapterOrderList = new OrderListAdapter(allOrder, getActivity(), rvOrder);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        rvOrder = (RecyclerView)fragmentView.findViewById(R.id.recycleview_confirmOrderList);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setHasFixedSize(true);
        rvOrder.setAdapter(adapterOrderList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshContent();
            }
        });
        mSwipeRefreshLayout.setEnabled(true);
        return fragmentView;
    }


    public void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(getActivity());
        if(!networkDetails.isEmpty()) {
            new GettingOrderList().execute();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), ErrorMessages.getNoInternet(getContext()), Toast.LENGTH_LONG).show();
        }
    }


    class GettingOrderList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            mSwipeRefreshLayout.setRefreshing(false);
            if(orderListObject != null){
                if(orderListObject.length() > 0){
                    try {
                        allOrder.clear();
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
                            morder.setTotalamount(Double.parseDouble(objOrder.getString("amount")));
                            morder.setTotaldeal(Integer.parseInt(objOrder.getString("orderquantity")));
                            morder.setTotalsavings(Double.parseDouble(objOrder.getString("totalsavings")));
                            morder.setuserid(objOrder.getString("userid"));
                            morder.setOrderrating(Integer.parseInt(objOrder.getString("rating")));
                            if(morder.getOrderstatus() == 1){
                                allOrder.add(morder);
                            }

                        }
                        adapterOrderList.notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(), ErrorMessages.getNoInternet(getContext()), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
