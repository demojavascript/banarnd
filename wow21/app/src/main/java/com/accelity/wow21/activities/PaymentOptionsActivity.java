package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.CartData;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private PaymentOptionsActivity fthis;
    private ArrayList<CartData> cartDeal;
    private Double amountQualifying = 0.0, amountMRP, amountwalletPaying, amountTotal = 0.0, amountSaved = 0.0, amountCoupon = 0.0, amountPayable = 0.0, amountWallet = 0.0, amountAfterWallet = 0.0;
    private TextView tv_qualifyingamount, tv_totalMRP, tv_cbamount, tv_couponCode, tv_couponCode_cb, tv_dealCount, tv_totalAmount, tv_totalSavings, tv_couponAmount, tv_payableAMount, tv_walletTotalAmount, tv_finalPayable, tv_finalPayable1;
    private EditText tv_walletPayAmount;
    private CheckBox cb_btn_wallet;
    private String str_coupncode, warehouseID, objVariantIDs, storeID, ErrorMsg, walletObject, couponType;
    private Boolean isCouponApplied = false;
    private NetConnection netConnection;
    private LinearLayout lout_couponBox, lout_couponBox_cb;
    private Button btn_PayNow;
    private JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog = null;
    private JSONObject jPayNow, walletDataObj;
    private Toast tincr = null;
    private RadioButton rb_cash, rb_online;
    private String AppOrderNo = "",  TXNID = "", BANKTXNID = "";
    private String pMode = "Cash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.PaymentOptions_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cartDeal = new ArrayList<CartData>();
        lout_couponBox_cb = (LinearLayout)findViewById(R.id.lout_couponBox_cb);
        lout_couponBox = (LinearLayout)findViewById(R.id.lout_couponBox);
        btn_PayNow = (Button)findViewById(R.id.btn_PayNow);
        btn_PayNow.setOnClickListener(this);

        rb_cash = (RadioButton)findViewById(R.id.rb_cash);
        rb_cash.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                rb_online.setChecked(false);
                rb_cash.setChecked(true);
                btn_PayNow.setText("Place Order");
               AppOrderNo = "";
               TXNID = "";
               BANKTXNID = "";
               pMode = "Cash";
           }
        });
        rb_online = (RadioButton)findViewById(R.id.rb_online);
        rb_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_cash.setChecked(false);
                rb_online.setChecked(true);
                btn_PayNow.setText("Pay Now");
                pMode = "Online";
            }
        });

        tv_qualifyingamount = (TextView)findViewById(R.id.tv_qualifyingamount);

        tv_totalMRP = (TextView)findViewById(R.id.tv_totalMRP);
        tv_dealCount = (TextView)findViewById(R.id.tv_dealCount);
        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        tv_totalSavings = (TextView)findViewById(R.id.tv_totalSavings);
        tv_payableAMount = (TextView)findViewById(R.id.tv_payableAMount);
        tv_couponCode_cb = (TextView)findViewById(R.id.tv_couponCode_cb) ;
        tv_couponCode = (TextView)findViewById(R.id.tv_couponCode) ;
        tv_walletTotalAmount = (TextView)findViewById(R.id.tv_walletTotalAmount);
        tv_walletPayAmount = (EditText)findViewById(R.id.tv_walletPayAmount);
        tv_walletPayAmount.setOnClickListener(this);
        tv_finalPayable = (TextView)findViewById(R.id.tv_finalPayable);
        tv_finalPayable1 = (TextView)findViewById(R.id.tv_finalPayable1);
        tv_couponAmount = (TextView)findViewById(R.id.tv_couponAmount);
        tv_cbamount = (TextView)findViewById(R.id.tv_cbamount);

        cb_btn_wallet = (CheckBox)findViewById(R.id.cb_btn_wallet);
        cb_btn_wallet.setOnClickListener(this);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
            amountMRP = intent.getDoubleExtra("MRPAmount", 0.0);
            tv_totalMRP.setText(amountMRP+"");
            amountTotal = Double.parseDouble(intent.getStringExtra("finalAmount"));
            amountSaved = Double.parseDouble(intent.getStringExtra("totalSavings"));

            warehouseID = intent.getStringExtra("warehouseID");
            storeID = intent.getStringExtra("storeID");
            cartDeal = (ArrayList<CartData>)intent.getSerializableExtra("cartData");
            tv_dealCount.setText(cartDeal.size()+" Items");

            isCouponApplied = intent.getBooleanExtra("isCoupon", false);
            amountPayable = amountTotal - amountCoupon;
            if(isCouponApplied){
                //lout_couponBox.setVisibility(View.VISIBLE);
                str_coupncode = intent.getStringExtra("couponCode");
                amountCoupon = Double.parseDouble(intent.getStringExtra("couponAmount"));
                couponType = intent.getStringExtra("couponType");
                amountSaved = amountSaved + amountCoupon;
                if(couponType.equals("cashback")){
                    tv_couponCode_cb.setText("Coupon Applied ("+str_coupncode+") Successfully.");
                    lout_couponBox_cb.setVisibility(View.VISIBLE);
                    tv_cbamount.setText(amountCoupon+"");
                    amountPayable = amountTotal;
                }else if(couponType.equals("instant")){
                    tv_couponCode.setText("Coupon Applied ("+str_coupncode+")");
                    lout_couponBox.setVisibility(View.VISIBLE);
                    amountPayable = amountTotal - amountCoupon;
                }
            }

            tv_couponAmount.setText(amountCoupon+"");
            tv_totalAmount.setText(amountTotal+"");
            tv_totalSavings.setText(amountSaved+"");
            tv_walletPayAmount.setText("0");
            tv_payableAMount.setText(amountPayable+"");
            tv_finalPayable.setText(amountPayable+"");
            tv_finalPayable1.setText(amountPayable+"");
            amountAfterWallet = amountPayable;
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(PaymentOptionsActivity.this);
            if (!networkDetails.isEmpty()) {
                new eWalletLoading().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }


    class eWalletLoading extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PaymentOptionsActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                walletDataObj = jsonParser.getUrlContents(URLManager.getUsereWalletUrl2() + "userid=" + user.getID());
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            cb_btn_wallet.setChecked(false);
            tv_walletPayAmount.setEnabled(false);
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if(walletDataObj != null){
                try{
                    amountQualifying = Double.parseDouble(walletDataObj.getString("min_eligibleamount"));
                    tv_qualifyingamount.setText(amountQualifying+"");
                    if(Double.parseDouble(walletDataObj.getString("amount")) > 0.0){
                        //if(1==1){

                        amountWallet = Double.parseDouble(walletDataObj.getString("amount"));
                        //amountWallet = 400.0;
                        tv_walletTotalAmount.setText(amountWallet+"");
                        //if(amountQualifying > Double.parseDouble(tv_payableAMount.getText().toString().trim())){
                        if(amountWallet > 0){
                            if(amountQualifying <= Double.parseDouble(tv_totalAmount.getText().toString())){
                                cb_btn_wallet.setChecked(true);
                                if(amountWallet > Double.parseDouble(tv_totalAmount.getText().toString())){
                                    amountAfterWallet = 0.0;
                                    tv_walletPayAmount.setText(amountPayable+"");
                                    tv_finalPayable.setText("0");
                                    tv_finalPayable1.setText("0");
                                }else{
                                    amountAfterWallet = amountPayable - amountWallet;
                                    tv_walletPayAmount.setText(amountWallet+"");
                                    tv_finalPayable.setText(amountAfterWallet+"");
                                    tv_finalPayable1.setText(amountAfterWallet+"");
                                }
                            }
                        }else{
                            cb_btn_wallet.setChecked(false);
                        }
                        //}

                    }else{

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            tv_walletPayAmount.setText("0");
        }
        else if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES){
            amountwalletPaying = Double.parseDouble(tv_walletPayAmount.getText().toString());
            if(amountwalletPaying > amountWallet){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_amountnot_g_wallet), Toast.LENGTH_LONG).show();
            }else if(amountwalletPaying > Double.parseDouble(tv_payableAMount.getText().toString().trim())){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_wallet_amnt_g), Toast.LENGTH_LONG).show();
            }else{

            }
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

    class finalPayment extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PaymentOptionsActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject finalObject = new JSONObject();
            JSONArray jsonItems = new JSONArray();
            try {
                for(int h=0; h<cartDeal.size();h++){
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("dealid", cartDeal.get(h).getDealID());
                    jsonItem.put("dealamount", cartDeal.get(h).getSellingPrice());
                    jsonItem.put("dealqty", cartDeal.get(h).getMineQty());
                    jsonItem.put("varientid", cartDeal.get(h).getVariantID());
                    jsonItems.put(jsonItem);
                }
                finalObject.put("appsource", "customerapp");
                finalObject.put("userid", user.getID());
                finalObject.put("storeid", storeID);
                finalObject.put("paymenttype", pMode);
                finalObject.put("coupon", "");
                if(amountwalletPaying > 0) {
                    finalObject.put("paybycashback", amountwalletPaying);
                }else{
                    finalObject.put("paybycashback", 0);
                }
                finalObject.put("couponfor", "");
                if(isCouponApplied){
                    finalObject.put("coupon", str_coupncode);
                    //finalObject.put("paybycashback", amountwalletPaying);
                    finalObject.put("couponfor", "apply");
                }

                finalObject.put("orderdetails", jsonItems);
                finalObject.put("AppOrderNo", AppOrderNo);
                finalObject.put("TXNID", TXNID);
                finalObject.put("BANKTXNID", BANKTXNID);


            }catch(Exception e){
                e.printStackTrace();
            }
            Log.d("finalObject", finalObject+"");
            jPayNow = jsonParser.makeHttpRequestJSON(URLManager.getPlaceOrderURL(),"POST", finalObject);
            Log.d("jPayNow", jPayNow+"");
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(jPayNow != null){
                try {
                    if (jPayNow.getString("Message").equals("success")) {
                        String fmantt = "";
                        String paymentType = "";
                        for(int k=0;k<jPayNow.getJSONObject("ResponseObject").getJSONArray("paymentdetails").length();k++){
                            JSONObject jobjDetail = jPayNow.getJSONObject("ResponseObject").getJSONArray("paymentdetails").getJSONObject(k);
                           if(Integer.parseInt(jobjDetail.getString("paymentmode")) == 0){
                               paymentType = "Cash";
                               fmantt = CustomUtil.convertTo2Decimal(Double.parseDouble(jobjDetail.getString("amount")));
                           }
                            if(Integer.parseInt(jobjDetail.getString("paymentmode")) == 3) {
                                paymentType = "Online";
                                fmantt = CustomUtil.convertTo2Decimal(Double.parseDouble(jobjDetail.getString("amount")));
                            }
                        }

                        Intent intent = new Intent(PaymentOptionsActivity.this, OrderSuccessActivity.class);
                        //intent.putExtra("orderAmount", jPayNow.getJSONObject("ResponseObject").getString("amount"));fmantt
                        intent.putExtra("paymentType", paymentType);
                        intent.putExtra("orderAmount", fmantt);
                        intent.putExtra("orderId", jPayNow.getJSONObject("ResponseObject").getString("orderno"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), jPayNow.getString("MessageText"), Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Some error occurred. Try Again.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_walletPayAmount:
                tv_walletPayAmount.setText("");
                break;
            case R.id.btn_PayNow:
                if(cb_btn_wallet.isChecked()){
                    amountwalletPaying = Double.parseDouble(tv_walletPayAmount.getText().toString());
                    if(amountwalletPaying > amountWallet){
                        tincr = Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_amount_cnt_g_wa), Toast.LENGTH_LONG);
                        tincr.show();
                    }else if(amountwalletPaying > Double.parseDouble(tv_payableAMount.getText().toString())){
                        tincr = Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_wallet_amnt_g), Toast.LENGTH_LONG);
                        tincr.show();
                    }else{
                        netConnection = new NetConnection();
                        Map<String, String> networkDetals = netConnection.getConnectionDetails(PaymentOptionsActivity.this);
                        if(!networkDetals.isEmpty()){
                            //new finalPayment().execute();
                            if(rb_cash.isChecked()) {
                                new finalPayment().execute();
                            }else if(rb_online.isChecked()){
                                onStartTransaction(v, amountPayable);
                            }else{
                                tincr = Toast.makeText(getApplicationContext(), "Select Any Payment Mode", Toast.LENGTH_LONG);
                                tincr.show();
                            }
                        }else{
                            tincr = Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG);
                            tincr.show();
                        }
                    }
                }else{
                    amountwalletPaying = 0.0;
                    netConnection = new NetConnection();
                    Map<String, String> networkDetals = netConnection.getConnectionDetails(PaymentOptionsActivity.this);
                    if(!networkDetals.isEmpty()){
                        if(rb_cash.isChecked()) {
                            new finalPayment().execute();
                        }else if(rb_online.isChecked()){
                            onStartTransaction(v, amountPayable);
                        }else{
                            tincr = Toast.makeText(getApplicationContext(), "Select Any Payment Mode", Toast.LENGTH_LONG);
                            tincr.show();
                        }
                    }else{
                        tincr = Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG);
                        tincr.show();
                    }
                }
                break;
            case R.id.cb_btn_wallet:
                if(tincr != null){
                    tincr.cancel();
                }
                if(amountWallet < 1){
                    tincr = Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_wallet_amnt_0), Toast.LENGTH_LONG);
                    tincr.show();
                    cb_btn_wallet.setChecked(false);
                }else if(amountQualifying > Double.parseDouble(tv_totalAmount.getText().toString())){
                    tincr = Toast.makeText(getApplicationContext(), getResources().getString(R.string.paymentoption_minto_avail_wallet) +" "+amountQualifying, Toast.LENGTH_LONG);
                    tincr.show();
                    cb_btn_wallet.setChecked(false);
                }else{
                    if(cb_btn_wallet.isChecked()) {
                        if(amountWallet >= Double.parseDouble(tv_totalAmount.getText().toString())){
                            amountwalletPaying = amountWallet - Double.parseDouble(tv_payableAMount.getText().toString());
                            tv_walletPayAmount.setText(""+Double.parseDouble(tv_payableAMount.getText().toString()));
                            tv_finalPayable.setText("0");
                            tv_finalPayable1.setText("0");
                        }else {
                            amountwalletPaying = amountWallet;
                            tv_walletPayAmount.setText(""+amountwalletPaying);
                            Double df = Double.parseDouble(tv_payableAMount.getText().toString()) - amountWallet;
                            tv_finalPayable.setText(df+"");
                            tv_finalPayable1.setText(df+"");
                        }
                    }else{
                        tv_walletPayAmount.setText("0");
                        amountwalletPaying = Double.parseDouble(tv_payableAMount.getText().toString());
                        tv_finalPayable.setText(""+amountwalletPaying);
                        tv_finalPayable1.setText(""+amountwalletPaying);
                    }
                }
                break;
        }
    }


    public void onStartTransaction(View view, Double amount) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", iniOrderId());
        paramMap.put("MID", getResources().getString(R.string.sample_merchant_id_staging));
        paramMap.put("CUST_ID", user.getID());
        paramMap.put("CHANNEL_ID", getResources().getString(R.string.sample_channel_id));
        paramMap.put("INDUSTRY_TYPE_ID", getResources().getString(R.string.sample_industry_type_id));
        paramMap.put("WEBSITE", getResources().getString(R.string.sample_website));
        paramMap.put("TXN_AMOUNT", amountPayable+"");
        paramMap.put("THEME", getResources().getString(R.string.sample_theme));
        //paramMap.put("EMAIL", user.getEmail());
        paramMap.put("MOBILE_NO", user.getMobile());
        Log.d("paramMap", ""+paramMap);
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "http://www.wow21deals.com/GenerateChecksum.aspx",
                "http://www.wow21deals.com/VerifyChecksum.aspx");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        AppOrderNo = inResponse.getString("ORDERID");
                        TXNID = inResponse.getString("TXNID");
                        BANKTXNID = inResponse.getString("BANKTXNID");
                        new finalPayment().execute();
                        Log.d("LOG", "Payment Transaction is successful " + inResponse.getString("TXNAMOUNT"));
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        //TXNAMOUNT
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        //new finalPayment().execute();
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed, Please Try again", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }

    private String iniOrderId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
