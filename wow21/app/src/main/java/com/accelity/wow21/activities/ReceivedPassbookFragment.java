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
import com.accelity.wow21.adapters.PassbookListAdapter;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Passbook;
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


public class ReceivedPassbookFragment extends Fragment {
    private ArrayList<Passbook> passbookList;
    private RecyclerView rvPassbook;
    private RecyclerView.Adapter adapterPassbookList;
    private RecyclerView.LayoutManager layoutManager;
    private NetConnection netConnection;
    private User user;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray passBookObject;;
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
        passbookList = new ArrayList<Passbook>();
        PassbookActivity fgh = (PassbookActivity)getActivity();
        passbookList = fgh.getAllData(3);
        rvPassbook = (RecyclerView)getActivity().findViewById(R.id.recycleview_passbooklist);
        adapterPassbookList = new PassbookListAdapter(passbookList, getActivity(), rvPassbook);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_received_passbook, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        rvPassbook = (RecyclerView)fragmentView.findViewById(R.id.recycleview_passbooklist);
        rvPassbook.setLayoutManager(layoutManager);
        rvPassbook.setHasFixedSize(true);
        rvPassbook.setAdapter(adapterPassbookList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshContent();
            }
        });
        mSwipeRefreshLayout.setDistanceToTriggerSync(180);
        mSwipeRefreshLayout.setEnabled(true);
        return fragmentView;
    }


    private void refreshContent(){
        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(getActivity());
        if(!networkDetails.isEmpty()) {
            new GetPassbook().execute();
        } else {
            Toast.makeText(getActivity(), ErrorMessages.getNoInternet(getActivity()), Toast.LENGTH_LONG).show();
        }
    }


    class GetPassbook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            mSwipeRefreshLayout.setRefreshing(false);
            if(passBookObject != null){
                if(passBookObject.length() > 0){
                    passbookList.clear();
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
                            //passbookList.add(passbook);
                            if(Float.parseFloat(objbook.getString("debit")) > 0){
                                //passbookList.add(passbook);
                            }else{
                                passbookList.add(passbook);
                            }
                        }
                        adapterPassbookList.notifyDataSetChanged();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.passbook_notransfound), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
