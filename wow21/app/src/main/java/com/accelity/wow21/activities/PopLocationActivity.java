package com.accelity.wow21.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Place;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Rahul on 20-08-2016.
 */
public class PopLocationActivity extends Activity implements  AdapterView.OnItemClickListener,  View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView tv_Address, btnSkip;
    private Button btnRefreshLocation;
    private LinearLayout ll_skipbox;
    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 199;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonProfile;
    private Double Latitude,  Longitude;
    private ProgressDialog pDialog = null;
    private PopLocationActivity fthis;
    private ArrayList<String> placeIds;
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_DETAIL = "/details";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBb1ti5oCMGqG-4_TjZdropWXZsJmDzh8s";
    private NetConnection netConnection;
    //private static final String API_KEY = "AIzaSyCMgTEWgrOaK3YOniK59egp0wvpvs4dXQA";
    //AIzaSyCMgTEWgrOaK3YOniK59egp0wvpvs4dXQA
    private AutoCompleteTextView autoCompView;
    private String Latt, Lngg;
    private User user;
    private UserPref userPref;
    private String Address = "", City = "", State = "", Country = "", knownName = "";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_location_home);
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        fthis = this;
        btnRefreshLocation = (Button)findViewById(R.id.btnRefreshLocation);
        btnRefreshLocation.setOnClickListener(this);
        btnSkip = (TextView)findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(this);

        ll_skipbox = (LinearLayout) findViewById(R.id.ll_skipbox);

        if (user.getLat() != null && !user.getLat().equals("0")) {
            ll_skipbox.setOnClickListener(this);
        }else{
            ll_skipbox.setVisibility(View.GONE);
        }

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        placeIds = new ArrayList<String>();
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.support_simple_spinner_dropdown_item));
        autoCompView.setOnItemClickListener(this);

        tv_Address = (TextView)findViewById(R.id.tv_Address);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSkip){
            if (user.getLat() != null && !user.getLat().equals("0") && user.getCity().trim().length() > 2) {
                Intent intent = new Intent();
                intent.putExtra("locstatus", 3);
                setResult(201, intent);
                finish();
            }
        }
        if(v.getId() == R.id.btnRefreshLocation){
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(PopLocationActivity.this);
            if(!networkDetails.isEmpty()) {
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

                    }else{
                        refreshlocation();
                    }
                }else{
                    refreshlocation();
                }
            }else{
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(PopLocationActivity.this);
                    if(!networkDetails.isEmpty()) {
                        refreshlocation();
                    }else{
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshlocation(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(PopLocationActivity.this).build();
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
                        Log.d("In Success", "11");
                        processLocation();
                        //new AttemptLocation().execute();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d("In RESOLUTION_REQUIRED", "11");
                        try {
                            status.startResolutionForResult(PopLocationActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SERVICE_VERSION_UPDATE_REQUIRED:
                        Log.d("In SERVICE_VERSI", "11");
                       // updatePlayService();

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("In CHANGE_UNAVAILABLE", "11");
                        //  updatePlayService();
                        break;
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                       // finish();
                        break;
                    }
                    case Activity.RESULT_OK: {
                        processLocation();
                        //new AttemptLocation().execute();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    private void processLocation(){
        LocationManager locmanager = (LocationManager) PopLocationActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsStatus = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            pDialog = CProgress.ctor(PopLocationActivity.this);
            pDialog.show();
            Log.d("In above", "location change");
            locationManager = (LocationManager) PopLocationActivity.this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    Log.d("In a", "change...");
                    pDialog.dismiss();
                    new AttemptLocation().execute();
                    locationManager.removeUpdates(locationListener);
                    /*try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Country = addresses.get(0).getCountryName();
                        State = addresses.get(0).getAdminArea();
                        City = addresses.get(0).getLocality();
                        Address = addresses.get(0).getAddressLine(0);

                        String finalStr = Address + "," + City + ", " + State + ", " + Country;
                        autoCompView.setText(finalStr);
                        userPref.setAddress(Address);
                        userPref.setCity(City);
                        userPref.setState(State);
                        userPref.setCountry(Country);
                        userPref.setLat(Latitude+"");
                        userPref.setLong(Longitude+"");
                        pDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("locstatus", 1);
                        setResult(201, intent);
                        finish();

                    } catch (IOException ex) {
                        pDialog.dismiss();
                        new AttemptLocation().execute();
                        ex.printStackTrace();
                    }*/
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //Log.d("provider", status+"  "+provider);
                }

                public void onProviderEnabled(String provider) {
                    //Log.d("provider", "2"+"  "+provider);
                }

                public void onProviderDisabled(String provider) {
                    //Log.d("provider", "1"+"  "+provider);
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


    class AttemptLocation extends AsyncTask<String, String, String> {
        JSONObject jsonObj;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PopLocationActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            Log.d("jsonObj", "start");
            jsonObj = jsonParser.getJSONFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Latitude + ","
                    + Longitude + "&sensor=true");
            return null;
        }
        protected void onPostExecute(String file_url) {
            String country = "";
            if(jsonObj != null){
                try {
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero.getJSONArray("address_components");

                        String jFAdd = zero.getString("formatted_address");
                        String [] allAddress = jFAdd.split(",");
                        Address = allAddress[1]+", "+allAddress[2]+", "+allAddress[3];


                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (Type.equalsIgnoreCase("street_number")) {

                            }else if (Type.equalsIgnoreCase("route")) {

                            }else if (Type.equalsIgnoreCase("political") ) {

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

                        pDialog.dismiss();
                        userPref.setAddress(Address);
                        userPref.setCity(City);
                        userPref.setState(State);
                        userPref.setCountry(Country);
                        userPref.setLat(Latitude+"");
                        userPref.setLong(Longitude+"");
                        Intent intent = new Intent();
                        intent.putExtra("locstatus", 1);
                        setResult(201, intent);
                        finish();
                    }else{
                        pDialog.dismiss();
                    }
                }catch(JSONException jex){
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getUnexpectedError(fthis), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("locstatus", 0);
                    setResult(201, intent);
                    finish();
                    jex.printStackTrace();
                }
            }else{
                pDialog.dismiss();
            }

        }
    }


    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Details();
        //placeIds.clear();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Details(placeIds.get(position));
        }
        //Toast.makeText(this, placeIds.get(position)+"", Toast.LENGTH_SHORT).show();
    }

    public void Details(String placeId){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ArrayList resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAIL + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + placeId);
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //Log.d("st1", "Error processing Places API URL", e);
            //return resultList;
        } catch (IOException e) {
            //Log.d("st2", "Error connecting to Places API", e);
            //return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject results = jsonObj.getJSONObject("result");
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            Latt = location.get("lat").toString();
            Lngg = location.get("lng").toString();
            tv_Address.setText("Latitude:- "+Latt+", Longitude:- "+Lngg);


            JSONArray address_components = results.getJSONArray("address_components");

            String jFAdd = results.getString("formatted_address");
            String [] allAddress = jFAdd.split(",");
            if(allAddress.length == 1){
                Address = allAddress[0];
            }else if(allAddress.length == 2){
                Address = allAddress[0]+", "+allAddress[1];
            }else if(allAddress.length >= 3){
                Address = allAddress[0]+", "+allAddress[1]+", "+allAddress[2];
            }




            for (int i = 0; i < address_components.length(); i++) {
                JSONObject zero2 = address_components.getJSONObject(i);
                String long_name = zero2.getString("long_name");
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);

                if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                    if (Type.equalsIgnoreCase("street_number")) {

                    }else if (Type.equalsIgnoreCase("route")) {

                    }else if (Type.equalsIgnoreCase("sublocality_level_2")) {

                    } else if (Type.equalsIgnoreCase("sublocality") || Type.equalsIgnoreCase("sublocality_level_1")) {
                        City = long_name;
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
            }

            userPref.setAddress(Address);
            userPref.setCity(City);
            userPref.setState(State);
            userPref.setCountry(Country);
            userPref.setLat(Latt+"");
            userPref.setLong(Lngg+"");
            Intent intent = new Intent();
            intent.putExtra("locstatus", 1);
            setResult(201, intent);
            finish();

        } catch (JSONException e) {
            Intent intent = new Intent();
            intent.putExtra("locstatus", 0);
            setResult(201, intent);
            finish();
            e.printStackTrace();
            //Log.e(LOG_TAG, "Cannot process", e);
        }
        //return null;
    }

    public static ArrayList<Place> autocomplete(String input) {
        ArrayList<Place> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            resultList = new ArrayList<Place>();
            //resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                //System.out.println("============================================================");
                resultList.add(new Place(predsJsonArray.getJSONObject(i).getString("place_id"), predsJsonArray.getJSONObject(i).getString("description")));
                //resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                //Log.d("place_id", predsJsonArray.getJSONObject(i).getString("place_id")+"");
            }
        } catch (JSONException e) {
            //Log.e(LOG_TAG, "Cannot process", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList mresultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return mresultList.size();
        }

        @Override
        public String getItem(int index) {
            return mresultList.get(index)+"";
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        placeIds.clear();
                        ArrayList<Place> resultList = autocomplete(constraint.toString());
                        mresultList = new ArrayList(resultList.size());

                        for(int j = 0; j<resultList.size(); j++){
                            Place place = resultList.get(j);
                            mresultList.add(place.getDesc());
                            placeIds.add(place.getId());
                        }
                        filterResults.values = mresultList;
                        filterResults.count = mresultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
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
        if (user.getLat() != null && !user.getLat().equals("0") && user.getCity().trim().length() > 2) {
            Intent intent = new Intent();
            intent.putExtra("locstatus", 77);
            setResult(201, intent);
            finish();
        }
    }
}
