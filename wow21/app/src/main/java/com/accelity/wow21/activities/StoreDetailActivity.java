package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.etc.ImageDownloaderTask;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Store;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Map;

public class StoreDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private StoreDetailActivity fthis;
    private String storeId;
    private ImageView img_Banner, icon_Banner;
    private ProgressDialog pDialog = null;
    private Store objStore;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonStore;
    private NetConnection netConnection;
    private ScrollView dv_topBox;
    private LatLng opmap;
    private SupportMapFragment mapFragment;
    private TextView tv_name, tv_address, tv_distance, tv_showonmap, tv_closingDay;
    private String bannerURL;
    private LinearLayout lout_closedOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.StoreDetail_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.storeMap);
        img_Banner = (ImageView) findViewById(R.id.img_Banner);
        icon_Banner = (ImageView) findViewById(R.id.icon_Banner);
        dv_topBox = (ScrollView) findViewById(R.id.dv_topBox);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_closingDay = (TextView) findViewById(R.id.tv_closingDay);
        tv_showonmap = (TextView) findViewById(R.id.tv_showonmap);
        tv_showonmap.setOnClickListener(this);

        lout_closedOn = (LinearLayout)findViewById(R.id.lout_closedOn);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (!bundle.isEmpty()) {
                storeId = intent.getStringExtra("storeId");
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(StoreDetailActivity.this);
                if(!networkDetails.isEmpty()) {
                    new GettingStores().execute();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void iniMap(){
       if(mapFragment != null){

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Toast.makeText(getApplicationContext(), "map showing...ready", Toast.LENGTH_LONG).show();
                    opmap = new LatLng(Double.parseDouble(objStore.getLatitude()), Double.parseDouble(objStore.getLongitude()));
                    Marker tp = googleMap.addMarker(new MarkerOptions().position(opmap).title(objStore.getName()));
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(opmap, 15));
                }
            });
        }
    }

    public void setDisplay(){
        dv_topBox.setVisibility(View.VISIBLE);
        iniMap();
        tv_name.setText(objStore.getName());
        tv_address.setText(objStore.getAddress());
        float fdis = Float.parseFloat(objStore.getDistance());
        String strDis = String.format("%.02f", fdis);
        Float distance = Float.parseFloat(strDis)/1000;
        String strDis2 = String.format("%.02f", distance);
        if(Float.parseFloat(strDis) < 1000.0) {
            tv_distance.setText(" " + strDis + " mtr");
        }else{
            tv_distance.setText(" " + strDis2.toString() + " km");
        }
        if(objStore.getIcon() != null){
            new ImageDownloaderTask(img_Banner).execute(objStore.getIcon());
            new ImageDownloaderTask(icon_Banner).execute(objStore.getIcon());
        }
        try {
            if (objStore.getClosingDay().length() < 1) {
                lout_closedOn.setVisibility(View.GONE);
            } else {
                tv_closingDay.setText("" + objStore.getClosingDay());
            }
        }catch(Exception e){
            e.printStackTrace();
            lout_closedOn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_showonmap){
            showOnMap();
        }
    }

    public void showOnMap(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+objStore.getLatitude()+","+objStore.getLongitude()));
        startActivity(intent);
    }

    class GettingStores extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(StoreDetailActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getStoreDetail() + "&lat=" + user.getLat() + "&long=" + user.getLong()+"&storeid="+storeId;
            jsonStore = jsonParser.getUrlContents(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            try {
                if (jsonStore != null) {
                    objStore = new Store(  jsonStore.getString("storeName"),
                            "",
                            jsonStore.getString("distance"),
                            jsonStore.getString("storeID"),
                            jsonStore.getString("storeIcon"),
                            jsonStore.getString("warehouseID"),
                            jsonStore.getString("storeAddress"),
                            jsonStore.getString("storeContact"),
                            jsonStore.getString("latitude"),
                            jsonStore.getString("longitude"),
                            jsonStore.getString("closingday")
                    );
                    setDisplay();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoStore(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                ex.printStackTrace();
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
}
