package com.accelity.wow21.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.adapters.CheckoutAdapter;
import com.accelity.wow21.adapters.CouponListAdapter;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.CartData;
import com.accelity.wow21.models.Coupon;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustMessages;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private User user;
    private Button btn_applycouponBox, btn_freebiesBox, btn_SelectCoupon, btn_ApplyCoupon, btn_DoneCouponSelection, btnCheckout2, btnCheckout;
    private EditText ed_finalCouponCode;
    private UserPref userPref;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonCouponInfo, outerObject;
    private JSONArray jsonCoupons, jsonObjectStock;
    private CheckoutActivity fthis;
    private RelativeLayout rout_step3;
    private LinearLayout lout_step1, lout_step2, lout_step4, lout_step5, lout_cashback;
    private TextView tv_tQty, tv_totalAmount, tv_tSavings, tv_couponCode, tv_couponAmount, tv_payableAmount, btn_cancelCoupon, tv_couponAmount2;
    private RecyclerView rvDealLIst;
    private RecyclerView.Adapter adapterDealLIst;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CartData> homeDeal, cartDeal;
    private ProgressDialog pDialog = null;
    private Double totalAmountSP = 0.0, dealAmount = 0.0, dealSavings = 0.0;
    private Integer ttalDealqty = 0;
    private NetConnection netConnection;
    private ListView lv_couponlist;
    private CouponListAdapter couponListAdapter;
    private String[] couponList = {"qw","qw", "qw", "qw", "qw"};
    private String str_coupncode, warehouseID, objVariantIDs, objStoreID, str_couponAmount;
    private Double finaldiscount, totalAmount, MRPamount = 0.0;
    private ArrayList<Coupon> CouponList;
    private Boolean isCouponApplied = false;
    private String couponType, couponKey = "";
    private static CheckoutActivity inst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Checkout_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_SelectCoupon = (Button) findViewById(R.id.btn_SelectCoupon);
        btn_SelectCoupon.setOnClickListener(this);

        ed_finalCouponCode = (EditText)findViewById(R.id.ed_finalCouponCode);

        btn_applycouponBox = (Button) findViewById(R.id.btn_applycouponBox);
        btn_applycouponBox.setOnClickListener(this);
        btn_ApplyCoupon = (Button) findViewById(R.id.btn_ApplyCoupon);
        btn_ApplyCoupon.setOnClickListener(this);
        btn_freebiesBox = (Button)findViewById(R.id.btn_freebiesBox);
        btn_freebiesBox.setOnClickListener(this);
        btn_cancelCoupon = (TextView) findViewById(R.id.btn_couponCancel);
        btn_cancelCoupon.setOnClickListener(this);
        btn_DoneCouponSelection = (Button)findViewById(R.id.btn_DoneCouponSelection);
        btn_DoneCouponSelection.setOnClickListener(this);
        btnCheckout = (Button)findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);
        btnCheckout2 = (Button)findViewById(R.id.btnCheckout2);
        btnCheckout2.setOnClickListener(this);

        lout_step2 = (LinearLayout)findViewById(R.id.lout_step2);
        lout_step1 = (LinearLayout)findViewById(R.id.lout_step1);
        lout_step4 = (LinearLayout)findViewById(R.id.lout_step4);
        lout_step5 = (LinearLayout)findViewById(R.id.lout_step5);
        rout_step3 = (RelativeLayout) findViewById(R.id.rout_step3);
        lout_cashback = (LinearLayout) findViewById(R.id.lout_cashback);


        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        tv_tSavings = (TextView)findViewById(R.id.tv_tSavings);
        tv_tQty = (TextView)findViewById(R.id.tv_tQty);
        tv_couponAmount2 = (TextView)findViewById(R.id.tv_couponAmount2);
        tv_couponAmount = (TextView)findViewById(R.id.tv_couponAmount);
        tv_couponCode = (TextView)findViewById(R.id.tv_couponCode);
        tv_payableAmount = (TextView)findViewById(R.id.tv_payableAmount);


        CouponList = new ArrayList<Coupon>();
        lv_couponlist = (ListView)findViewById(R.id.couponList);
        couponListAdapter = new CouponListAdapter(this, R.layout.row_coupon_list, CouponList, lv_couponlist);
        lv_couponlist.setAdapter(couponListAdapter);

        homeDeal = new ArrayList<CartData>();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
            warehouseID = intent.getStringExtra("warehouseID");
            objStoreID = intent.getStringExtra("storeID");

            homeDeal = (ArrayList<CartData>)intent.getSerializableExtra("cartData");
            rvDealLIst = (RecyclerView)findViewById(R.id.recycleview_checkoutdeallist);
            layoutManager = new LinearLayoutManager(this);
            rvDealLIst.setLayoutManager(layoutManager);
            rvDealLIst.setHasFixedSize(true);
            adapterDealLIst = new CheckoutAdapter(homeDeal, this, rvDealLIst, tv_totalAmount, tv_tQty, tv_tSavings, R.layout.row_checkoutdeallist);
            rvDealLIst.setAdapter(adapterDealLIst);
            rvDealLIst.setNestedScrollingEnabled(false);

            for (Integer mm = 0; mm <homeDeal.size();mm++){
                dealAmount = dealAmount + homeDeal.get(mm).getSellingPrice() * homeDeal.get(mm).getMineQty();
                dealSavings = dealSavings + homeDeal.get(mm).getMineQty() * ( homeDeal.get(mm).getMrp() - homeDeal.get(mm).getSellingPrice() );
                ttalDealqty = ttalDealqty + homeDeal.get(mm).getMineQty();
            }
            tv_totalAmount.setText(dealAmount+"");
            String ghtt = String.format("%.2f", Double.parseDouble(dealSavings.toString()));
            tv_tSavings.setText(ghtt);
            tv_tQty.setText(ttalDealqty+"");
        }

    }

    @Override
    public void onBackPressed() {
        if(rout_step3.getVisibility() == View.VISIBLE){
            rout_step3.setVisibility(View.GONE);
            lout_step2.setVisibility(View.VISIBLE);
        }else if(lout_step2.getVisibility() == View.VISIBLE) {
            lout_step2.setVisibility(View.GONE);
            lout_step1.setVisibility(View.VISIBLE);
        }else if(lout_step5.getVisibility() == View.VISIBLE) {
            //lout_step2.setVisibility(View.GONE);
            //lout_step1.setVisibility(View.VISIBLE);
            super.onBackPressed();
            finish();
        }else {
            super.onBackPressed();
            finish();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCheckout:
                CheckoutProcess();
                break;
            case R.id.btnCheckout2:
                CheckoutProcess();
                break;
            case R.id.btn_DoneCouponSelection:
                int itemcount = 0;
                if(couponKey.length() < 1){
                    Toast.makeText(getApplicationContext(), CustMessages.getCouponCodeMissText(fthis), Toast.LENGTH_LONG).show();
                }else{
                    lout_step2.setVisibility(View.GONE);
                    rout_step3.setVisibility(View.GONE);
                    applyingCoupon();
                    ed_finalCouponCode.setText(couponKey);
                    //lout_step5.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_couponCancel:
                isCouponApplied = false;
                str_coupncode = "";
                couponKey = "";
                lout_step5.setVisibility(View.GONE);
                lout_step1.setVisibility(View.VISIBLE);
                ed_finalCouponCode.setText("");
                lout_step4.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ApplyCoupon:
                if(ed_finalCouponCode.getText().toString().trim().length()<1){
                    Toast.makeText(getApplicationContext(), "Please enter the coupon code", Toast.LENGTH_SHORT).show();;
                }else{

                }
                InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                couponKey = ed_finalCouponCode.getText().toString();
                applyingCoupon();
                break;
            case R.id.btn_applycouponBox:
                lout_step1.setVisibility(View.GONE);
                lout_step2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_freebiesBox:

                break;
            case R.id.btn_SelectCoupon:
                //
                couponKey = "";
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed_finalCouponCode.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                totalAmount = Double.parseDouble(tv_totalAmount.getText().toString());
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(ed_finalCouponCode, InputMethodManager.SHOW_IMPLICIT);
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(CheckoutActivity.this);
                if (!networkDetails.isEmpty()) {
                    new gettingCouponList().execute();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void CheckoutProcess(){
        objVariantIDs = "";
        cartDeal = new ArrayList<CartData>();
        MRPamount = 0.0;
        for(Integer k=0;k<homeDeal.size();k++){
            TextView tvQty = (TextView)rvDealLIst.getChildAt(k).findViewById(R.id.tv_dealQty);
            if (Integer.parseInt(tvQty.getText().toString()) > 0){
                MRPamount = MRPamount + homeDeal.get(k).getMrp() * Integer.parseInt(tvQty.getText().toString());
                CartData objCartData = new CartData(homeDeal.get(k).getVariantID(), homeDeal.get(k).getVariantTitle(), homeDeal.get(k).getDealID(),
                        homeDeal.get(k).getDealName(), homeDeal.get(k).getDealDesc(), homeDeal.get(k).getDealIcon(),
                        homeDeal.get(k).getMaxPerCustomer(), homeDeal.get(k).getStockAvailable(), homeDeal.get(k).getDealTotalQty(),
                        homeDeal.get(k).getMrp(), homeDeal.get(k).getSellingPrice(), homeDeal.get(k).getDealDiscount(),
                        homeDeal.get(k).getIsSuper(), homeDeal.get(k).getSuperDealMinAmount(),   null,   null, homeDeal.get(k).getDealQty(), Integer.parseInt(tvQty.getText().toString().trim()), homeDeal.get(k).getCatID(), homeDeal.get(k).getBrandID());
                cartDeal.add(objCartData);
                if(objVariantIDs == "" || objVariantIDs == null){
                    objVariantIDs = homeDeal.get(k).getVariantID();
                }else{
                    objVariantIDs = objVariantIDs + "," + homeDeal.get(k).getVariantID();
                }
            }
        }
        if(cartDeal.size() < 1) {
            Toast.makeText(getApplicationContext(), ErrorMessages.getSelectAnyDeal(fthis), Toast.LENGTH_LONG).show();
        }else {
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(CheckoutActivity.this);
            if(!networkDetails.isEmpty()) {
                new gettingStockInfo().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ed_finalCouponCode.setText("");
    }

    class gettingStockInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(CheckoutActivity.this);
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
                    if( cartDeal.get(h).getMineQty() > Integer.parseInt(objData.get("stock").toString()) ){
                        hh++;
                        ErrorMsg = ErrorMsg + cartDeal.get(h).getDealName() + getResources().getString(R.string.wStock_txt1) + objData.get("stock").toString() + getResources().getString(R.string.wStock_txt2);
                    }
                }
                if(hh < 1){
                    lout_step1.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(CheckoutActivity.this, PaymentOptionsActivity.class);
                    intent.putExtra("warehouseID", warehouseID);
                    intent.putExtra("storeID", objStoreID);
                    intent.putExtra("cartData", cartDeal);
                    intent.putExtra("totalSavings", tv_tSavings.getText().toString());
                    intent.putExtra("isCoupon", isCouponApplied);
                    intent.putExtra("couponType", couponType);
                    intent.putExtra("couponCode", couponKey);
                    intent.putExtra("couponAmount",str_couponAmount);
                    //String finalmnn = CustomUtil.convertTo2Decimal(Double.parseDouble());
                    intent.putExtra("finalAmount", tv_totalAmount.getText().toString());
                    intent.putExtra("MRPAmount", Double.parseDouble(CustomUtil.convertTo2Decimal(MRPamount)));
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

    public void applyingCoupon(){
        int itemCount = 0;
        outerObject = new JSONObject();
        JSONArray allItems = new JSONArray();
        for(Integer k=0;k<homeDeal.size();k++){
            TextView tvQty = (TextView)rvDealLIst.getChildAt(k).findViewById(R.id.tv_dealQty);
            int qty = Integer.parseInt(tvQty.getText().toString());
            if(qty > 0) {
                itemCount++;
                JSONObject jsonObject = new JSONObject();
                try {
                    CartData deal = homeDeal.get(k);
                    jsonObject.put("cp", deal.getSellingPrice());
                    jsonObject.put("qty", Integer.parseInt(tvQty.getText().toString()));
                    jsonObject.put("itemid", deal.getVariantID());
                    jsonObject.put("title", deal.getVariantTitle());
                    jsonObject.put("isfreebie", false);
                    jsonObject.put("catid", deal.getCatID());
                    jsonObject.put("brandid", deal.getBrandID());
                    allItems.put(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try{
            outerObject.put("calltype", "check");
            outerObject.put("coupon", couponKey);
            outerObject.put("appname", "wow");
            outerObject.put("orderno", "");
            outerObject.put("ordersequanceno", 0);
            outerObject.put("warehouseid", warehouseID);
            outerObject.put("userid", user.getID());
            outerObject.put("mobno", user.getMobile());
            outerObject.put("email", user.getEmail());
            outerObject.put("item", allItems);
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("outerObject", outerObject+"");
        if(itemCount < 1) {
            Toast.makeText(getApplicationContext(), ErrorMessages.getSelectAnyDeal(fthis), Toast.LENGTH_LONG).show();
        }else{
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(CheckoutActivity.this);
            if (!networkDetails.isEmpty()) {
                new applyingCoupon().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    class gettingCouponList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(CheckoutActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            jsonCoupons = jsonParser.getJSONFromUrlAsGET(URLManager.getUserPramotionURL()+"userid="+user.getID()+"&orderamount="+totalAmount);
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(jsonCoupons != null){
                if(jsonCoupons.length() > 0) {
                    CouponList.clear();
                    try {
                        for (int h = 0; h < jsonCoupons.length(); h++) {
                            Coupon objCoupon = new Coupon(jsonCoupons.getJSONObject(h).getString("text"), jsonCoupons.getJSONObject(h).getString("description"), jsonCoupons.getJSONObject(h).getString("maxdiscount"), jsonCoupons.getJSONObject(h).getBoolean("canapplicable"), jsonCoupons.getJSONObject(h).getString("minorderamount"));
                            CouponList.add(objCoupon);
                        }
                        rout_step3.setVisibility(View.VISIBLE);
                        couponListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    rout_step3.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), CustMessages.getNoCouponFoundText(fthis), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    class applyingCoupon extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(CheckoutActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            jsonCouponInfo = jsonParser.makeHttpRequestJSON(URLManager.getDiscountURL(), "POST", outerObject);
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(jsonCouponInfo != null){
                JSONObject resultObject, jsonMsg;
                try {
                    resultObject = jsonCouponInfo.getJSONObject("result");
                    if(resultObject.getString("message").equals("success")){
                        String dfg = resultObject.getJSONObject("rule").getString("returntype");

                        finaldiscount = Double.parseDouble(jsonCouponInfo.getString("orderdiscount").toString());
                        str_couponAmount = jsonCouponInfo.getString("orderdiscount").toString();
                        isCouponApplied = true;
                        lout_step2.setVisibility(View.GONE);
                        lout_step4.setVisibility(View.VISIBLE);
                        //lout_step5.setVisibility(View.VISIBLE);


                        tv_couponCode.setText(couponKey);
                        tv_couponAmount.setText(finaldiscount+"");
                        if(dfg.equals("cashback")){
                            tv_couponAmount2.setText(str_couponAmount);
                            couponType = "cashback";
                            lout_cashback.setVisibility(View.VISIBLE);
                        }else{
                            couponType = "instant";
                            lout_cashback.setVisibility(View.GONE);
                        }
                        Double payableAmount = Double.parseDouble(jsonCouponInfo.getString("netpayable").toString());
                        tv_payableAmount.setText(payableAmount+"");

                        showCouponPopup(couponKey, str_couponAmount, payableAmount+"", couponType);

                    }else{
                        lout_step4.setVisibility(View.VISIBLE);
                        lout_step5.setVisibility(View.GONE);
                        lout_step2.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), resultObject.getString("message")+"", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{

            }
        }
    }

    public void showCouponPopup(String couponcode, String coupomamount, String payamount, String cType){
        final Dialog dialog = new Dialog(fthis);
        dialog.setContentView(R.layout.coupon_popup);


        View v_spacee = (View) dialog.findViewById(R.id.v_spacee);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            v_spacee.setVisibility(View.VISIBLE);
        }
        LinearLayout lout_cashback = (LinearLayout) dialog.findViewById(R.id.lout_cashback);
        LinearLayout lout_coupon = (LinearLayout) dialog.findViewById(R.id.lout_coupon);

        if(cType.equals("cashback")){
            lout_cashback.setVisibility(View.VISIBLE);
        }else{
            lout_coupon.setVisibility(View.VISIBLE);
        }
        TextView tv_couponAmount2 = (TextView) dialog.findViewById(R.id.tv_couponAmount2);
        TextView tv_couponamount = (TextView) dialog.findViewById(R.id.tv_couponamount);
        TextView tv_netpayableamount = (TextView) dialog.findViewById(R.id.tv_netpayableamount);
        TextView tvCouponCodee = (TextView) dialog.findViewById(R.id.tvCouponCodee);

        tv_couponAmount2.setText(coupomamount);
        tv_couponamount.setText(coupomamount);
        tv_netpayableamount.setText(payamount);
        tvCouponCodee.setText(couponcode);

        Button btnNo = (Button) dialog.findViewById(R.id.btn_couponCancel);
        Button btnYes = (Button) dialog.findViewById(R.id.btnGoCheckout);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutProcess();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCouponApplied = false;
                str_coupncode = "";
                lout_step5.setVisibility(View.GONE);
                lout_step1.setVisibility(View.VISIBLE);
                ed_finalCouponCode.setText("");
                lout_step4.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        
        if(Integer.parseInt(coupomamount) < 1){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.discount_Amount_Zero), Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }
    public static CheckoutActivity instance() {
        return inst;
    }
    public void updateKey(final String dKey) {
        couponKey = dKey;
        ed_finalCouponCode.setText(couponKey);
    }
}
