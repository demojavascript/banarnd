package com.accelity.wow21.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.HorizontalDealAdapter;
import com.accelity.wow21.adapters.HorizontalStoreAdapter;
import com.accelity.wow21.adapters.SidebarAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.etc.DatabaseHandler;
import com.accelity.wow21.etc.DealGridView;
import com.accelity.wow21.etc.NavigationDrawerFragment;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.CustomAdsPref;
import com.accelity.wow21.models.HomeDeal;
import com.accelity.wow21.models.StockDetail;
import com.accelity.wow21.models.Store;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustMessages;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private Toolbar toolbar;
    private Button btnAllStore, btn_shopNow;
    private ImageView img_notify, img_menu, leftTimeImg;
    private TextView tv_hurrydealtext, edLocation, leftTime, tv_address1, tv_username, tv_address, tv_notiCount;
    private ImageView user_profile_pic, img_topaddress;
    private LinearLayout homeSliderBox, hv_catbox, hv_dealsbox, hv_storebox, homeFooter, lout_home_shopnow;
    private NavigationDrawerFragment drawerFragment;
    private DrawerLayout drawerLayout;
    private SliderLayout homeSlider;
    private DealGridView rv_deallist;
    private HorizontalDealAdapter adapterDeal;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_storelist;
    private RecyclerView.Adapter adapterStore;
    private RecyclerView.LayoutManager layoutManagerStore;
    private int notificationCount = 0;
    private User user;
    private UserPref userPref;
    private CustomAdsPref customAdsPref;
    private DatabaseHandler db;
    private SharedPreferences sharedpreferences;
    private ProgressDialog pDialog = null;
    private ArrayList<HomeDeal> homeDeal;
    private ArrayList<Store> homeStore;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonStore;
    private JSONObject jsonDeals, jsonProfile, jsonStoreInfo, jsonProfileImg;
    private int homeDealCount = 0;
    private NetConnection netConnection;
    private HomeActivity fthis;
    private CountDownTimer countDownTimer;
    private String str_endDate, cuufImgname = "";

    private ListView listviewsidebar;
    private SidebarAdapter sidebarAdapter;
    private LinearLayout lout_cloc;
    private String[] menuOptions;
    private int[] menuIcons = {
            R.drawable.ic_cart,R.drawable.ic_store,R.drawable.ic_orderhistory,R.drawable.ic_myprofile,
            R.drawable.ic_notification,R.drawable.ic_wow_cash,R.drawable.ic_promotion,R.drawable.ic_rate_us,
            R.drawable.ic_share,R.drawable.ic_call_us,R.drawable.ic_about,R.drawable.ic_faq,R.drawable.ic_tc,
            R.drawable.ic_privacypolicy
    };
    private String TAddress = "", TCountry, TState, TCity, TknownName = "";
    private ScrollView scrollView;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    GoogleApiClient googleApiClient;
    LocationListener locationListener;
    LocationManager locationManager;
    final static int REQUEST_LOCATION = 199;
    Double Latitude,  Longitude;
    private static HomeActivity inst;
    private String newAdsId = "-1";
    private String newAdsUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        customAdsPref = new CustomAdsPref(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        db = new DatabaseHandler(this);

        tv_notiCount = (TextView)findViewById(R.id.tv_notiCount);

        scrollView = (ScrollView) findViewById(R.id.homeview_Scroll);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.home_swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);

        Resources objres = getResources();
        menuOptions = objres.getStringArray(R.array.menuOptions);
        listviewsidebar = (ListView) findViewById(R.id.sidebar_menu);
        sidebarAdapter = new SidebarAdapter(this, menuOptions, menuIcons);
        listviewsidebar.setAdapter(sidebarAdapter);
        listviewsidebar.setOnItemClickListener(this);

        user_profile_pic = (ImageView)findViewById(R.id.user_profile_pic);
        img_topaddress = (ImageView)findViewById(R.id.img_topaddress);
        img_topaddress.setOnClickListener(this);

        lout_cloc = (LinearLayout) findViewById(R.id.lout_cloc);
        lout_cloc.setOnClickListener(this);


        leftTimeImg = (ImageView) findViewById(R.id.leftTimeImg);
        btnAllStore = (Button)findViewById(R.id.btnAllStore);
        //btnAllStore.setPaintFlags(btnAllStore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnAllStore.setOnClickListener(this);
        btn_shopNow = (Button)findViewById(R.id.btn_shopNow);
        btn_shopNow.setOnClickListener(this);
        homeSliderBox = (LinearLayout)findViewById(R.id.homeSliderBox);
        hv_storebox = (LinearLayout)findViewById(R.id.hv_storebox);
        hv_catbox = (LinearLayout)findViewById(R.id.hv_catbox);
        hv_dealsbox = (LinearLayout)findViewById(R.id.hv_dealsbox);
        homeFooter = (LinearLayout)findViewById(R.id.homeFooter);
        lout_home_shopnow = (LinearLayout)findViewById(R.id.lout_home_shopnow);
        lout_home_shopnow.setOnClickListener(this);
        leftTime = (TextView)findViewById(R.id.leftTime);
        edLocation = (TextView)findViewById(R.id.edLocation);
        edLocation.setOnClickListener(this);

        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_hurrydealtext = (TextView) findViewById(R.id.tv_hurrydealtext);

        img_menu = (ImageView) findViewById(R.id.img_menu);
        img_menu.setOnClickListener(this);
        img_notify = (ImageView)findViewById(R.id.img_notify);
        img_notify.setOnClickListener(this);
        tv_address1 = (TextView)findViewById(R.id.tv_address1);
        tv_address1.setOnClickListener(this);


        //CustomUtil.testtt();

        if(user.getImg() != null && user.getImg().trim().length() > 0){
            if(readFileToInternalStorage() != null){
                user_profile_pic.setImageBitmap(readFileToInternalStorage());
            }else{
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }
        }else{
            if(user.getGender() != null && user.getGender().equals("Male")){
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }else if(user.getGender() != null && user.getGender().equals("Female")){
                user_profile_pic.setImageResource(R.drawable.user_profile_f);
            }else{
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }
        }

        if(user.getLName() != null && user.getLName().trim().length() > 0){
            tv_username.setText(user.getFName() + " " + user.getLName());
        }else {
            tv_username.setText(user.getFName());
        }
        if(user.getCountry() != null && user.getCountry().length()>0) {
            tv_address1.setText(user.getAddress());
            tv_address.setText(user.getAddress());
            if(user.getAddress().length() < 1){
                tv_address1.setText(user.getCity());
                tv_address.setText(user.getCity());
            }
        }
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        homeSliderBox = (LinearLayout)findViewById(R.id.homeSliderBox) ;
        homeSlider = (SliderLayout)findViewById(R.id.homeSlider);
        homeSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        homeSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        homeSlider.setCustomAnimation(new DescriptionAnimation());
        homeSlider.setDuration(6000);
        homeSlider.addOnPageChangeListener(this);

        homeDeal = new ArrayList<HomeDeal>();
        rv_deallist = (DealGridView) findViewById(R.id.horizontal_rv_deals);
        //adapterDeal = new HorizontalDealAdapter(this, homeDeal);
        rv_deallist.setNumColumns(2);
        //rv_deallist.setAdapter(adapterDeal);

        homeStore = new ArrayList<Store>();
        rv_storelist = (RecyclerView)findViewById(R.id.horizontal_rv_storelist);
        layoutManagerStore = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_storelist.setLayoutManager(layoutManagerStore);
        rv_storelist.setHasFixedSize(true);
        adapterStore = new HorizontalStoreAdapter(homeStore, this, rv_storelist);
        rv_storelist.setAdapter(adapterStore);

        try {
            if (user.getLat() != null && !user.getLat().equals("0") && user.getCity().trim().length() > 2) {
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails.isEmpty()) {
                    if(user.getCountry().length()>0){
                        reloadData();
                    }else {
                        getAddress(Double.parseDouble(user.getLat()), Double.parseDouble(user.getLong()));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
            }else{
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails.isEmpty()) {
                    initiatePopupWindow();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
            if(!networkDetails.isEmpty()) {
                initiatePopupWindow();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }

        Double dfg = getScreenSize()[0]*0.611;
        int hH = dfg.intValue();
        homeSliderBox.getLayoutParams().height =  hH;
        ViewGroup.LayoutParams params =    homeSliderBox.getLayoutParams();
        params.height =  hH;
        homeSliderBox.setLayoutParams(params);

    }

    public void reloadData(){
        new GettingDeals().execute();
    }

    public int[] getScreenSize(){
        Point size = new Point();
        WindowManager w = getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x, size.y};
        }else{
            Display d = w.getDefaultDisplay();
            return new int[]{d.getWidth(), d.getHeight()};
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        //scrollView.pageScroll(View.FOCUS_UP);
        switch(position){
            case 0:
                netConnection = new NetConnection();
                Map<String, String> networkDetails1 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails1.isEmpty()) {
                    //scrollView.pageScroll(View.FOCUS_UP);
                    //Intent intentt = new Intent(HomeActivity.this, DealListActivity.class);
                    //startActivity(intentt);
                    shopNow();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                netConnection = new NetConnection();
                Map<String, String> networkDetails2 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails2.isEmpty()) {
                    scrollView.pageScroll(View.FOCUS_UP);
                    Intent stores  = new Intent(HomeActivity.this, StoreListActivity.class);
                    startActivity(stores);
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                netConnection = new NetConnection();
                Map<String, String> networkDetails3 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails3.isEmpty()) {
                    scrollView.pageScroll(View.FOCUS_UP);
                    Intent orders  = new Intent(HomeActivity.this, OrderListActivity.class);
                    startActivity(orders);
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                Intent profile  = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile);
                break;
            case 4:
                //if(db.getNotificationUnRead() > 0) {
                    //int fg = db.syncNotification();
                //}
                Intent intent2  = new Intent(HomeActivity.this, NotificationListActivity.class);
                intent2.putExtra("fromhome", true);
                startActivity(intent2);
                break;
            case 5:
                netConnection = new NetConnection();
                Map<String, String> networkDetails533 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails533.isEmpty()) {
                    //scrollView.pageScroll(View.FOCUS_UP);
                    Intent intentp  = new Intent(HomeActivity.this, PassbookActivity.class);
                    startActivity(intentp);
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 6:
                netConnection = new NetConnection();
                Map<String, String> networkDetails531 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails531.isEmpty()) {
                    Intent intentp  = new Intent(HomeActivity.this, OffersActivity.class);
                    startActivity(intentp);
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 7:
                netConnection = new NetConnection();
                Map<String, String> networkDetails5 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails5.isEmpty()) {
                    //scrollView.pageScroll(View.FOCUS_UP);
                    showFeedback();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 8:
                netConnection = new NetConnection();
                Map<String, String> networkDetails55 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails55.isEmpty()) {
                    shareApp();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case 9:
                callUs();
                break;

            case 10:
                Intent cnt1  = new Intent(HomeActivity.this, ContentActivity.class);
                cnt1.putExtra("contentpage", 1);
                startActivity(cnt1);
                break;
            case 11:
                Intent cnt2  = new Intent(HomeActivity.this, ContentActivity.class);
                cnt2.putExtra("contentpage", 2);
                startActivity(cnt2);
                break;
            case 12:
                Intent cnt3  = new Intent(HomeActivity.this, ContentActivity.class);
                cnt3.putExtra("contentpage", 3);
                startActivity(cnt3);
                break;
            case 13:
                Intent cnt4  = new Intent(HomeActivity.this, ContentActivity.class);
                cnt4.putExtra("contentpage", 4);
                startActivity(cnt4);
                break;
        }
        //scrollView.pageScroll(View.FOCUS_UP);
    }

    public void showFeedback(){
        final Dialog dialog = new Dialog(fthis);
        dialog.setContentView(R.layout.feedback_alert);
        dialog.setTitle(getResources().getString(R.string.home_feeback_title));
        ImageView btnYes = (ImageView) dialog.findViewById(R.id.btnYes);
        ImageView btnNo = (ImageView) dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodFeedback();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badFeedback();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void goodFeedback(){
        final Dialog dialog = new Dialog(fthis);
        dialog.setContentView(R.layout.goodfeedback_alert);
        dialog.setTitle(getResources().getString(R.string.home_feeback_goodfeedback_title));
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackPlayStore();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void badFeedback(){
        final Dialog dialog = new Dialog(fthis);
        dialog.setContentView(R.layout.badfeedback_alert);
        dialog.setTitle(getResources().getString(R.string.home_feeback_badfeedback_title));
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackMail();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void feedbackPlayStore(){
        Uri uri = Uri.parse(URLManager.getPlayStoreMarketURL());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            fthis.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            fthis.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(URLManager.getPlayStoreURL())));
        }
    }
    public void feedbackMail(){
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String allBody = CustMessages.getFeedbackMailStringBody(fthis)+""+user.getMobile();
        Uri data = Uri.parse("mailto:?subject=" + CustMessages.getFeedbackMailSubject(fthis) + "&body=" + allBody + "&to=" + CustMessages.getFeedbackMailTO(fthis));
        testIntent.setData(data);
        startActivity(testIntent);
    }
    public void shopNow(){
        if (homeDealCount > 0) {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
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
                    nowAskLoc();
                }
            }else{
                nowAskLoc();
            }
        }else{
            alertNoDeal();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    nowAskLoc();
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void nowAskLoc(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(HomeActivity.this).build();
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
                            status.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }

    private void processLocation(){
        LocationManager locmanager = (LocationManager) HomeActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsStatus = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            pDialog = CProgress.ctor(HomeActivity.this);
            pDialog.show();
            locationManager = (LocationManager) HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    locationManager.removeUpdates(locationListener);
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    pDialog.dismiss();
                    new AttemptInfoLocation().execute();
                    /*try {
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
                        Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
                        if (!networkDetails.isEmpty()) {
                            new GettingStoreInfo().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }

                    }catch(IOException ex) {
                        ex.printStackTrace();
                        pDialog.dismiss();
                        new AttemptInfoLocation().execute();
                    }*/
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

    public void alertNoDeal() {
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
        builder.setMessage(ErrorMessages.getHomeNoDeal(fthis))
                .setCancelable(false)
                .setNegativeButton("OK", dialogClickListener).show();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updatePlayService();
        }
    }

    public void updatePlayService(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Update Required");
        alertDialogBuilder
                .setMessage("Please update Google Play Service to use this App")
                .setCancelable(false)
                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        startActivityForResult(new Intent("android.settings.SYSTEM_UPDATE_SETTINGS"),0);
                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class GettingStoreInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(HomeActivity.this);
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
                startActivity(new Intent(HomeActivity.this, DealListActivity.class));
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
            pDialog = CProgress.ctor(HomeActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            Log.d("jsonObj22", "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Latitude + ","
                    + Longitude + "&sensor=true");
            jsonObj = jsonParser.getJSONFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Latitude + ","
                    + Longitude + "&sensor=true");
            Log.d("jsonObj22", jsonObj+"");

            return null;
        }
        protected void onPostExecute(String file_url) {
            String Address = "", City = "", State = "", Country = "", knownName = "";
            if(jsonObj != null) {
                try {
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero.getJSONArray("address_components");
                        String jFAdd = zero.getString("formatted_address");
                        String[] allAddress = jFAdd.split(",");
                        Address = allAddress[1] + ", " + allAddress[2] + ", " + allAddress[3];
                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (Type.equalsIgnoreCase("street_number")) {

                            } else if (Type.equalsIgnoreCase("route")) {

                            } else if (Type.equalsIgnoreCase("political")) {

                            } else if (Type.equalsIgnoreCase("political") || Type.equalsIgnoreCase("sublocality_level_1")) {

                            } else if (Type.equalsIgnoreCase("locality")) {
                                City = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {

                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;

                            } else if (Type.equalsIgnoreCase("postal_code")) {

                            }
                        }

                        //pDialog.dismiss();
                        userPref.setCountry(Country);
                        userPref.setCity(City);
                        userPref.setState(State);
                        userPref.setAddress(Address);
                        userPref.setLat(Latitude.toString());
                        userPref.setLong(Longitude.toString());
                        user = userPref.getUser(fthis);


                        if (user.getCountry() != null && user.getCountry().length() > 0) {

                            tv_address1.setText(user.getAddress());
                            tv_address.setText(user.getAddress());
                            if (user.getAddress().length() < 1) {
                                tv_address1.setText(user.getCity());
                                tv_address.setText(user.getCity());
                            }
                        }

                        netConnection = new NetConnection();
                        pDialog.dismiss();
                        Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
                        if (!networkDetails.isEmpty()) {
                            startActivity(new Intent(HomeActivity.this, DealListActivity.class));
                            //new GettingStoreInfo().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        pDialog.dismiss();
                    }
                } catch (JSONException jex) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getUnexpectedError(fthis), Toast.LENGTH_LONG).show();
                    jex.printStackTrace();
                }
            }else{
                pDialog.dismiss();
            }
        }
    }
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_google_play_url));
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }
    public void callUs(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+CustMessages.getCustomerCareNo(fthis)));
        startActivity(intent);
    }
    class GettingDeals extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(HomeActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeDealsUrl() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonDeals = jsonParser.getUrlContents(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(HomeActivity.this);
            if(!networkDetails.isEmpty()) {
                new GettingStores().execute();
            }else{
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }

            try {
                if (jsonDeals != null) {
                    homeSlider.removeAllSliders();
                    hv_catbox.setVisibility(View.VISIBLE);

                    if(jsonDeals.getJSONArray("HomeScreenPopup").length() > 0){
                        JSONObject jObj = jsonDeals.getJSONArray("HomeScreenPopup").getJSONObject(0);
                        newAdsId = jObj.getString("Id");
                        newAdsUrl = jObj.getString("Image");
                    }

                    if(jsonDeals.getJSONArray("banner").length() > 0 ) {
                        homeSliderBox.setVisibility(View.VISIBLE);
                        lout_home_shopnow.setVisibility(View.VISIBLE);
                        for(int hh = 0; hh< jsonDeals.getJSONArray("banner").length(); hh++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(fthis);
                            textSliderView
                                    .description(hh+"")
                                    .image(jsonDeals.getJSONArray("banner").get(hh).toString())
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(fthis);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", hh+"");
                            homeSlider.addSlider(textSliderView);
                        }
                    }else{
                        homeSliderBox.setVisibility(View.GONE);
                    }
                    homeDeal.clear();
                    if(jsonDeals.getJSONArray("deals").length() > 0) {
                        homeDeal.clear();
                        hv_dealsbox.setVisibility(View.VISIBLE);
                        JSONArray dealData = jsonDeals.getJSONArray("deals");
                        homeDealCount = dealData.length();
                        HomeDeal deal;
                        for (int k = 0; k < dealData.length(); k++) {
                            if(k==0){
                                str_endDate = dealData.getJSONObject(k).getString("enddate").toString();
                                if(CustomUtil.totalTimeDiff(str_endDate) != 0) {

                                    countDownTimer = new CountDownTimer(CustomUtil.totalTimeDiff(str_endDate), 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            //Log.d("millisUntilFinished", (millisUntilFinished/1000)%60+"");
                                            String dealdate = str_endDate;
                                            dealdate = dealdate.replaceAll("\\(|\\)", "");
                                            dealdate = dealdate.replace("Date", "");
                                            dealdate = dealdate.replaceAll("\\/","");
                                            long timestampString1 =  Long.parseLong(dealdate);
                                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                            Date value2 = new java.util.Date(timestampString1);
                                            Date value1 = new Date();
                                            //getAcualTimeLeft22(value1, value2);

                                            String eSec = (millisUntilFinished/1000)%60+"";
                                            eSec = ":"+eSec;
                                            String eTime = CustomUtil.getAcualTimeLeft22(value1, value2) + eSec;
                                            //leftTime.setText(eTime+"");
                                            //leftTime.setVisibility(View.VISIBLE);
                                            if(Integer.parseInt(CustomUtil.testtt(str_endDate)) < 24){
                                                leftTimeImg.setVisibility(View.VISIBLE);
                                                leftTime.setVisibility(View.VISIBLE);
                                                leftTime.setText(eTime+"");
                                                tv_hurrydealtext.setText(getResources().getString(R.string.deal_hurryup_txt));

                                            }else{
                                                leftTimeImg.setVisibility(View.GONE);
                                                leftTime.setVisibility(View.GONE);
                                                tv_hurrydealtext.setText(getResources().getString(R.string.deal_hurryup_txt2));
                                            }
                                        }

                                        public void onFinish() {
                                            countDownTimer.cancel();
                                            leftTime.setText("Timeout");
                                        }
                                    }.start();
                                }
                            }
                            String tmntmrp = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("mrp").toString()));
                            String superdpri = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("superdealminprice").toString()));
                            String sellpr = CustomUtil.convertTo2Decimal(Double.parseDouble(dealData.getJSONObject(k).getString("sellingprice").toString()));
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
                                                Double.parseDouble(sellpr),
                                                dealData.getJSONObject(k).getString("discount").toString(),
                                                Boolean.parseBoolean(dealData.getJSONObject(k).getString("is_super").toString()),
                                                Double.parseDouble(superdpri),
                                                null,
                                                null,
                                                0,
                                                dealData.getJSONObject(k).getString("categoryID").toString(),
                                                dealData.getJSONObject(k).getString("brandID").toString()
                                    );
                            StockDetail stockDetail = new StockDetail();
                            stockDetail.setColour(dealData.getJSONObject(k).getJSONObject("stockdetail").getString("colour"));
                            stockDetail.setPercentagesold(dealData.getJSONObject(k).getJSONObject("stockdetail").getString("percentagesold"));
                            stockDetail.setText(dealData.getJSONObject(k).getJSONObject("stockdetail").getString("text"));
                            deal.setStockDetail(stockDetail);
                            homeDeal.add(deal);
                        }
                        homeDeal.add(new HomeDeal());
                        adapterDeal = new HorizontalDealAdapter(fthis, homeDeal);
                        rv_deallist.setAdapter(adapterDeal);
                        adapterDeal.notifyDataSetChanged();
                    }else{
                        homeDealCount = 0;
                        hv_dealsbox.setVisibility(View.GONE);
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


    class GettingStores extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String newURL = URLManager.getHomeStoreListURL() + "&lat=" + user.getLat() + "&long=" + user.getLong();
            jsonStore = jsonParser.getJSONFromUrlAsGET(newURL);
            return null;
        }

        protected void onPostExecute(String responseData) {
            homeFooter.setVisibility(View.VISIBLE);
            pDialog.dismiss();
            try {
                if (jsonStore != null) {
                    homeStore.clear();
                    if (jsonStore.length() > 0) {
                        hv_storebox.setVisibility(View.VISIBLE);
                        Store objSt;
                        for (int k = 0; k < jsonStore.length(); k++) {
                            if(k<5) {
                                JSONObject jobj;
                                try {
                                    jobj = jsonStore.getJSONObject(k);
                                    objSt = new Store(jobj.getString("storeName"),
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
                                    homeStore.add(objSt);
                                } catch (JSONException ex) {

                                }
                            }
                        }
                        adapterStore.notifyDataSetChanged();
                        //pDialog.dismiss();
                    }else{
                        //pDialog.dismiss();
                        homeStore.clear();
                        hv_storebox.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoStore(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getHomeNoStore(fthis), Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex) {
                //pDialog.dismiss();
                ex.printStackTrace();
            }
            if(newAdsUrl != null && newAdsUrl.length() > 0 && !customAdsPref.getAdsId(fthis).toString().equals(newAdsId)) {
                showAds();
            }
        }
    }

    private void showAds(){
        customAdsPref.setAdsId(newAdsId);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.custom_popup, null);
        ImageView imgAds = (ImageView) dialoglayout.findViewById(R.id.imageViewAds);
        Picasso.with(fthis).load(newAdsUrl).into(imgAds);
        builder.setView(dialoglayout);
        final AlertDialog closedialog = builder.create();
        closedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                if(closedialog.isShowing()) {
                    closedialog.dismiss();
                }
                timer2.cancel();
            }
        }, 5000);
    }

    @Override
    protected void onStop() {
        homeSlider.stopAutoCycle();
        super.onStop();
    }
    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
        //scrollView.pageScroll(View.FOCUS_UP);

        int nCount = db.getNotificationUnRead();
        if(nCount < 1){
            tv_notiCount.setVisibility(View.GONE);
        }else{
            tv_notiCount.setText(nCount+"");
            tv_notiCount.setVisibility(View.VISIBLE);
        }

        userPref = new UserPref(this);
        user = userPref.getUser(this);

        if(user.getImg() != null && user.getImg().trim().length() > 0){
            if(readFileToInternalStorage() != null){
                user_profile_pic.setImageBitmap(readFileToInternalStorage());
            }else{
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }
        }else{
            if(user.getGender() != null && user.getGender().equals("Male")){
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }else if(user.getGender() != null && user.getGender().equals("Female")){
                user_profile_pic.setImageResource(R.drawable.user_profile_f);
            }else{
                user_profile_pic.setImageResource(R.drawable.user_profile_p);
            }
        }

        if(user.getLName() != null && user.getLName().trim().length() > 0){
            tv_username.setText(user.getFName() + " " + user.getLName());
        }else {
            tv_username.setText(user.getFName());
        }
        if(user.getCountry() != null && user.getCountry().length()>0) {
            tv_address1.setText(user.getAddress());
            tv_address.setText(user.getAddress());
            if(user.getAddress().length() < 1){
                tv_address1.setText(user.getCity());
                tv_address.setText(user.getCity());
            }
        }

        sidebarAdapter = new SidebarAdapter(this, menuOptions, menuIcons);
        listviewsidebar.setAdapter(sidebarAdapter);
        listviewsidebar.setOnItemClickListener(this);

    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
        inst = this;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_location) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edLocation:
                initiatePopupWindow();
                break;
            case R.id.btnAllStore:
                Intent intent1 = new Intent(HomeActivity.this, StoreListActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_shopNow:
                netConnection = new NetConnection();
                Map<String, String> networkDetails1 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails1.isEmpty()) {
                    shopNow();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.lout_home_shopnow:
                netConnection = new NetConnection();
                Map<String, String> networkDetails11 = netConnection.getConnectionDetails(HomeActivity.this);
                if(!networkDetails11.isEmpty()) {
                    shopNow();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_notify:
                //if(db.getNotificationUnRead() > 0) {
                  //  int fg = db.syncNotification();
                  //  tv_notiCount.setVisibility(View.GONE);
                //}
                Intent intentt = new Intent(HomeActivity.this, NotificationListActivity.class);
                startActivity(intentt);
                break;
            case R.id.img_menu:
                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_address1:
                initiatePopupWindow();
                break;
            case R.id.img_topaddress:
                initiatePopupWindow();
                break;
            case R.id.lout_cloc:
                initiatePopupWindow();
                break;
        }
    }


    public Bitmap readFileToInternalStorage(){
        Bitmap bitmap = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        File mypath = new File(directory, user.getImg());
        try {
            bitmap = BitmapFactory.decodeFile(mypath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }




    private void initiatePopupWindow() {
        startActivityForResult(new Intent(HomeActivity.this, PopLocationActivity.class), 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 199){
                switch (requestCode) {
                    case REQUEST_LOCATION:
                        switch (resultCode) {
                            case Activity.RESULT_CANCELED: {
                                //finish();
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
        if(requestCode == 201){
            try {
                if (data.getIntExtra("locstatus", 0) == 1) {
                    user = userPref.getUser(fthis);
                    tv_address1.setText(user.getAddress());
                    tv_address.setText(user.getAddress());
                    if(user.getAddress().length() < 1){
                        tv_address1.setText(user.getCity());
                        tv_address.setText(user.getCity());
                    }
                    //edLocation.setText(user.getAddress());
                    reloadData();
                } else if (data.getIntExtra("locstatus", 0) == 3){

                } else if (data.getIntExtra("locstatus", 0) == 77){

                }else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getLocUpdateFail(fthis), Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ErrorMessages.getLocUpdateFail(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getAddress(double lat, double lng) {
        new AttemptLocation().execute();
    }

    class AttemptLocation extends AsyncTask<String, String, String> {
        JSONObject jsonObj;
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(HomeActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            jsonObj = jsonParser.getJSONFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + user.getLat() + ","
                    + user.getLong() + "&sensor=true");
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(jsonObj != null){
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


                            String jFAdd = zero.getString("formatted_address");
                            String [] allAddress = jFAdd.split(",");
                            TAddress = allAddress[1]+", "+allAddress[2]+", "+allAddress[3];

                            if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                if (Type.equalsIgnoreCase("street_number")) {

                                }else if (Type.equalsIgnoreCase("route")) {

                                }else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                    //TknownName = long_name;
                                } else if (Type.equalsIgnoreCase("sublocality") || Type.equalsIgnoreCase("sublocality_level_1")) {
                                    //TAddress = long_name;
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

                        userPref.setAddress(TAddress);
                        userPref.setCity(TCity);
                        userPref.setState(TState);
                        userPref.setCountry(TCountry);
                        userPref.setUser(user);
                        user = userPref.getUser(fthis);
                        tv_address1.setText(user.getAddress());
                        tv_address.setText(user.getAddress());
                        if(user.getAddress().length() < 1){
                            tv_address1.setText(user.getCity());
                            tv_address.setText(user.getCity());
                        }
                        //edLocation.setText(user.getAddress());
                        pDialog.dismiss();
                        reloadData();
                    }else{

                    }
                }catch(JSONException jex){
                    jex.printStackTrace();
                }
            }else{

            }


        }
    }


    public static HomeActivity instance() {
        return inst;
    }

}
