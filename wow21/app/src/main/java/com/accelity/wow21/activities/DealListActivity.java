package com.accelity.wow21.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.DealListAdapter;
import com.accelity.wow21.adapters.DealListAdapter2;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.CartData;
import com.accelity.wow21.models.HomeDeal;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DealListActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final static int REQUEST_LOCATION = 199;
    private Double Latitude,  Longitude;
    private String TAddress = "", TCountry, TState, TCity, TknownName = "";
    private Toolbar toolbar;
    private Button btnRefLoc, btnGoToStore, btnCheckout;
    private TextView tv_tQty, tv_totalAmount, tv_tSavings, near_deal_storename;
    private RecyclerView rvDealLIst, rvNoDealLIst;
    private RecyclerView.Adapter adapterDealLIst, adapterNoDealLIst;
    private RecyclerView.LayoutManager layoutManager, layoutManagerNo;
    private ArrayList<HomeDeal> homeDeal, homeDealNo;
    private RelativeLayout ll_footer_summary;
    private LinearLayout belowApp, lout_for_nodeals, lout_for_storesummary, lout_step1, lout_step2;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonDeals, jsonDealsHome, jsonStoreInfo;
    private JSONArray jsonObjectStock;
    private NetConnection netConnection;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private User user;
    private UserPref userPref;
    private DealListActivity fthis;
    private String warehouseID;
    ArrayList<CartData> cartData;
    String  objVariantIDs, objStoreID, ErrorMsg;
    private Boolean isFromOrderSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Deallist_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (!bundle.isEmpty()) {
                isFromOrderSuccess = intent.getBooleanExtra("isSuccess", false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        btnRefLoc = (Button)findViewById(R.id.btnRefreshLoc) ;
        btnRefLoc.setOnClickListener(this);
        btnGoToStore = (Button)findViewById(R.id.btnStoreList) ;
        btnGoToStore.setOnClickListener(this);
        btnCheckout = (Button)findViewById(R.id.btnCheckout) ;
        btnCheckout.setOnClickListener(this);

        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        tv_tSavings = (TextView)findViewById(R.id.tv_tSavings);
        tv_tQty = (TextView)findViewById(R.id.tv_tQty);
        near_deal_storename = (TextView)findViewById(R.id.near_deal_storename);

        ll_footer_summary = (RelativeLayout)findViewById(R.id.ll_footer_summary);
        lout_step1 = (LinearLayout)findViewById(R.id.lout_step1);
        lout_step2 = (LinearLayout)findViewById(R.id.lout_step2);
        belowApp = (LinearLayout) findViewById(R.id.belowApp);
        lout_for_nodeals = (LinearLayout) findViewById(R.id.lout_for_nodeals);
        lout_for_storesummary = (LinearLayout) findViewById(R.id.lout_for_storesummary);

        homeDeal = new ArrayList<HomeDeal>();
        rvDealLIst = (RecyclerView)findViewById(R.id.recycleview_deallist);
        layoutManager = new LinearLayoutManager(this);
        rvDealLIst.setLayoutManager(layoutManager);
        rvDealLIst.setHasFixedSize(true);
        adapterDealLIst = new DealListAdapter(homeDeal, this, rvDealLIst, tv_totalAmount, tv_tQty, tv_tSavings, R.layout.row_deallist);
        rvDealLIst.setAdapter(adapterDealLIst);
        rvDealLIst.setNestedScrollingEnabled(false);

        homeDealNo = new ArrayList<HomeDeal>();
        rvNoDealLIst = (RecyclerView)findViewById(R.id.recycleview_nodeallist);
        layoutManagerNo = new LinearLayoutManager(this);
        rvNoDealLIst.setLayoutManager(layoutManagerNo);
        rvNoDealLIst.setHasFixedSize(true);
        adapterNoDealLIst = new DealListAdapter2(homeDealNo, this, rvNoDealLIst, tv_totalAmount, tv_tQty, tv_tSavings, R.layout.row_deallist_no);
        rvNoDealLIst.setAdapter(adapterNoDealLIst);
        rvNoDealLIst.setNestedScrollingEnabled(false);

        netConnection = new NetConnection();
        Map<String, String> networkDetails = netConnection.getConnectionDetails(DealListActivity.this);
        if(!networkDetails.isEmpty()) {
            new GettingDeals().execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if(isFromOrderSuccess){
                Intent intent  = new Intent(DealListActivity.this, HomeActivity.class);
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
    public void onBackPressed() {
        if(isFromOrderSuccess){
            Intent intent  = new Intent(DealListActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRefreshLoc:
                //refLoc();
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[] {
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.WRITE_SETTINGS
                        }, REQUEST_CODE_ASK_PERMISSIONS);

                        return;
                    }else{
                        refLoc();
                    }
                }else{
                    refLoc();
                }
                break;
            case R.id.btnStoreList:
                startActivity(new Intent(DealListActivity.this, StoreListActivity.class));
                break;
            case R.id.btnCheckout:
                gotoCheckout();
                break;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    refLoc();
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public  void gotoCheckout(){
        objVariantIDs = "";
        cartData = new ArrayList<CartData>();
        for(Integer k=0;k<homeDeal.size();k++){
            TextView tvQty = (TextView)rvDealLIst.getChildAt(k).findViewById(R.id.tv_dealQty);
            if (Integer.parseInt(tvQty.getText().toString()) > 0){
                CartData objCartData = new CartData(homeDeal.get(k).getVariantID(), homeDeal.get(k).getVariantTitle(), homeDeal.get(k).getDealID(),
                        homeDeal.get(k).getDealName(), homeDeal.get(k).getDealDesc(), homeDeal.get(k).getDealIcon(),
                        homeDeal.get(k).getMaxPerCustomer(), homeDeal.get(k).getStockAvailable(), homeDeal.get(k).getDealTotalQty(),
                        homeDeal.get(k).getMrp(), homeDeal.get(k).getSellingPrice(), homeDeal.get(k).getDealDiscount(),
                        homeDeal.get(k).getIsSuper(), homeDeal.get(k).getSuperDealMinAmount(),   null,   null, homeDeal.get(k).getDealQty(), Integer.parseInt(tvQty.getText().toString().trim()), homeDeal.get(k).getCatID(), homeDeal.get(k).getBrandID());
                cartData.add(objCartData);
                if(objVariantIDs == "" || objVariantIDs == null){
                    objVariantIDs = homeDeal.get(k).getVariantID();
                }else{
                    objVariantIDs = objVariantIDs + "," + homeDeal.get(k).getVariantID();
                }
            }
        }
        if(cartData.size() < 1) {
            Toast.makeText(getApplicationContext(), ErrorMessages.getSelectAnyDeal(fthis), Toast.LENGTH_LONG).show();
        }else {
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(DealListActivity.this);
            if(!networkDetails.isEmpty()) {
                new gettingStockInfo().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    class gettingStockInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(DealListActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL =  URLManager.getStockCheckURL() + "&storeid="+objStoreID+"&variantid="+objVariantIDs;
            jsonObjectStock = jsonParser.getJSONFromUrlAsGET(newURL);
            return newURL;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            int hh = 0;
            String ErrorMsg = "";
            try {
                for (int h = 0; h < jsonObjectStock.length(); h++) {
                    JSONObject objData = jsonObjectStock.getJSONObject(h);
                    if( cartData.get(h).getMineQty() > Integer.parseInt(objData.get("stock").toString()) ){
                        hh++;
                        ErrorMsg = ErrorMsg + cartData.get(h).getDealName() + getResources().getString(R.string.wStock_txt1) + objData.get("stock").toString() + getResources().getString(R.string.wStock_txt2);
                    }
                }
                if(hh < 1){
                    Intent intent = new Intent(DealListActivity.this, CheckoutActivity.class);
                    intent.putExtra("warehouseID", warehouseID);
                    intent.putExtra("storeID", objStoreID);
                    intent.putExtra("cartData", cartData);
                    startActivity(intent);
                }else{
                    alertStoreMessage2(ErrorMsg);
                }
            }catch(JSONException jx){

            }
        }
    }
    public void alertStoreMessage2(String msgh) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msgh)
                .setTitle("Error")
                .setCancelable(false)
                .setNegativeButton("OK", dialogClickListener).show();
    }
    public void refLoc(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(DealListActivity.this).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        processLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(DealListActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 199){
            switch (requestCode) {
                case REQUEST_LOCATION:
                    switch (resultCode) {
                        case Activity.RESULT_CANCELED: {
                            finish();
                            break;
                        }
                        case Activity.RESULT_OK: {
                            processLocation();
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
            }
        }
    }

    private void processLocation(){
        LocationManager locmanager = (LocationManager) DealListActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsStatus = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            pDialog = CProgress.ctor(DealListActivity.this);
            pDialog.show();
            locationManager = (LocationManager) DealListActivity.this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    locationManager.removeUpdates(locationListener);
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {
                        List<Address> addresses  = geocoder.getFromLocation(Latitude, Longitude, 1);
                        userPref.setAddress(addresses.get(0).getAddressLine(0));
                        userPref.setCity(addresses.get(0).getLocality());
                        userPref.setState(addresses.get(0).getAdminArea());
                        userPref.setCountry(addresses.get(0).getCountryName());
                        userPref.setLat(Latitude+"");
                        userPref.setLong(Longitude+"");
                        user = userPref.getUser(fthis);
                        pDialog.dismiss();
                        netConnection = new NetConnection();
                        Map<String, String> networkDetails = netConnection.getConnectionDetails(DealListActivity.this);
                        if (!networkDetails.isEmpty()) {
                            new GettingStoreInfo().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }

                    }catch(IOException ex) {
                        ex.printStackTrace();
                        pDialog.dismiss();
                        new AttemptInfoLocation().execute();
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class GettingStoreInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(DealListActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getNearStoreUrl()+"lat="+user.getLat()+"&long="+user.getLong()+"&userid="+user.getID();
            jsonStoreInfo = jsonParser.getUrlContents(newURL);
            return newURL;
        }
        protected void onPostExecute(String responseData) {
            if(jsonStoreInfo == null) {
                pDialog.dismiss();
                alertStoreMessage();
            }else {
                pDialog.dismiss();
                lout_for_storesummary.setVisibility(View.INVISIBLE);
                lout_for_nodeals.setVisibility(View.GONE);
                homeDeal.clear();
                adapterDealLIst.notifyDataSetChanged();
                ll_footer_summary.setVisibility(View.INVISIBLE);
                lout_step1.setVisibility(View.GONE);
                lout_step2.setVisibility(View.GONE);
                new GettingDeals().execute();
            }
        }
    }
    public void alertStoreMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ErrorMessages.getHomeNoStore(fthis))
                .setCancelable(false)
                .setNegativeButton("OK", dialogClickListener).show();
    }


    class AttemptInfoLocation extends AsyncTask<String, String, String> {
        JSONObject jsonObj;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(DealListActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            jsonObj = jsonParser.getJSONFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Latitude + ","
                    + Longitude + "&sensor=true");
            return null;
        }
        protected void onPostExecute(String file_url) {
            String Address = "", City = "", State = "", Country = "", knownName = "";
            try {
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {

                            }else if (Type.equalsIgnoreCase("route")) {

                            }else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                TknownName = long_name;
                            } else if (Type.equalsIgnoreCase("sublocality") || Type.equalsIgnoreCase("sublocality_level_1")) {
                                TAddress = long_name;
                            } else if (Type.equalsIgnoreCase("locality")) {
                                TCity = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {

                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                TState = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                TCountry = long_name;

                            } else if (Type.equalsIgnoreCase("postal_code")) {

                            }
                        }
                    }
                    if(TknownName.length()> 0){
                        TAddress = TknownName+", "+TAddress;
                    }
                    pDialog.dismiss();
                    userPref.setCountry(Country);
                    userPref.setCity(City);
                    userPref.setState(State);
                    userPref.setAddress(Address);
                    userPref.setLat(Latitude.toString());
                    userPref.setLong(Longitude.toString());
                    user = userPref.getUser(fthis);
                    netConnection = new NetConnection();
                    pDialog.dismiss();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(DealListActivity.this);
                    if (!networkDetails.isEmpty()) {
                        new GettingStoreInfo().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }else{
                    pDialog.dismiss();
                }
            }catch(JSONException jex){
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), ErrorMessages.getUnexpectedError(fthis), Toast.LENGTH_LONG).show();
                jex.printStackTrace();
            }
        }
    }


    class GettingDeals extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(DealListActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getNearDealsUrl() + "lat=" + user.getLat() + "&long=" + user.getLong()+"&userid="+user.getID();
            jsonDeals = jsonParser.getUrlContents(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            try {
                if (jsonDeals != null) {
                    if(jsonDeals.getJSONArray("deals").length() > 0) {
                        homeDeal.clear();
                        ll_footer_summary.setVisibility(View.VISIBLE);
                        lout_step1.setVisibility(View.VISIBLE);
                       // lout_for_storesummary.setVisibility(View.VISIBLE);
                        warehouseID = jsonDeals.getJSONObject("store").getString("warehouseID").toString();
                        objStoreID = jsonDeals.getJSONObject("store").getString("storeID").toString();
                        near_deal_storename.setText(jsonDeals.getJSONObject("store").getString("storeName").toString());
                        belowApp.setVisibility(View.VISIBLE);
                        JSONArray dealData = jsonDeals.getJSONArray("deals");
                        HomeDeal deal;
                        rvDealLIst.setVisibility(View.VISIBLE);
                        rvNoDealLIst.setVisibility(View.GONE);
                        for (int k = 0; k < dealData.length(); k++) {
                            String tmntmrp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("mrp").toString()));
                            String tmntallp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("sellingprice").toString()));
                            String tmntsdealp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("superdealminprice").toString()));
                            deal = new HomeDeal(dealData.getJSONObject(k).getString("variantID").toString(),
                                    dealData.getJSONObject(k).getString("title").toString(),
                                    dealData.getJSONObject(k).getString("dealid").toString(),
                                    dealData.getJSONObject(k).getString("deal_name").toString(),
                                    dealData.getJSONObject(k).getString("deal_desc").toString(),
                                    dealData.getJSONObject(k).getString("variantIcon").toString(),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("maxPerCustomer").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("stockavailabe").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("totalqty").toString()),
                                    Double.parseDouble(tmntmrp),
                                    Double.parseDouble(tmntallp),
                                    dealData.getJSONObject(k).getString("discount").toString(),
                                    Boolean.parseBoolean(dealData.getJSONObject(k).getString("is_super").toString()),
                                    Double.parseDouble(tmntsdealp),
                                    null,
                                    null,
                                    0,
                                    dealData.getJSONObject(k).getString("categoryID").toString(),
                                    dealData.getJSONObject(k).getString("brandID").toString()
                            );
                            ll_footer_summary.setVisibility(View.VISIBLE);
                            homeDeal.add(deal);
                        }
                        adapterDealLIst.notifyDataSetChanged();
                        pDialog.dismiss();
                    }else{
                        new GettingDealsHome().execute();
                    }
                } else {
                    pDialog.dismiss();
                    new GettingDealsHome().execute();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                pDialog.dismiss();
                new GettingDealsHome().execute();
                ex.printStackTrace();
            }
        }
    }


    class GettingDealsHome extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeDealsUrl() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonDealsHome = jsonParser.getUrlContents(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            try {
                if (jsonDealsHome != null) {
                    if(jsonDealsHome.getJSONArray("deals").length() > 0) {
                        lout_for_storesummary.setVisibility(View.GONE);
                        ll_footer_summary.setVisibility(View.VISIBLE);
                        lout_for_nodeals.setVisibility(View.VISIBLE);
                        lout_step2.setVisibility(View.VISIBLE);
                        homeDealNo.clear();
                        JSONArray dealData = jsonDealsHome.getJSONArray("deals");
                        HomeDeal deal;
                        rvDealLIst.setVisibility(View.GONE);
                        rvNoDealLIst.setVisibility(View.VISIBLE);
                        for (int k = 0; k < dealData.length(); k++) {
                            String tmntmrp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("mrp").toString()));
                            String tmntallp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("sellingprice").toString()));
                            String tmntsdealp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("superdealminprice").toString()));

                            deal = new HomeDeal(dealData.getJSONObject(k).getString("variantID").toString(),
                                    dealData.getJSONObject(k).getString("title").toString(),
                                    dealData.getJSONObject(k).getString("dealid").toString(),
                                    dealData.getJSONObject(k).getString("deal_name").toString(),
                                    dealData.getJSONObject(k).getString("deal_desc").toString(),
                                    dealData.getJSONObject(k).getString("variantIcon").toString(),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("maxPerCustomer").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("stockavailabe").toString()),
                                    Integer.parseInt(dealData.getJSONObject(k).getString("totalqty").toString()),
                                    Double.parseDouble(tmntmrp),
                                    Double.parseDouble(tmntallp),
                                    dealData.getJSONObject(k).getString("discount").toString(),
                                    Boolean.parseBoolean(dealData.getJSONObject(k).getString("is_super").toString()),
                                    Double.parseDouble(tmntsdealp),
                                    null,
                                    null,
                                    0,
                                    dealData.getJSONObject(k).getString("categoryID").toString(),
                                    dealData.getJSONObject(k).getString("brandID").toString()
                            );
                            homeDealNo.add(deal);
                        }
                        pDialog.dismiss();
                        //adapterNoDealLIst = new DealListAdapter(homeDeal, fthis, rvDealLIst, tv_totalAmount, tv_tQty, tv_tSavings, R.layout.row_deallist_no);
                        //rvDealLIst.setAdapter(adapterDealLIst);
                        adapterNoDealLIst.notifyDataSetChanged();
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoDeal(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                pDialog.dismiss();
                ex.printStackTrace();
            }
        }
    }
}
