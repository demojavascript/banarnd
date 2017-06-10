package com.accelity.wow21.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.Order;
import com.accelity.wow21.models.OrderDetail;
import com.accelity.wow21.models.PaymentDetails;
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

public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private OrderDetailActivity fthis;
    private Order morder, objOrder;

    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONArray orderListObject;
    private JSONObject objRating;
    private NetConnection netConnection;
    private ScrollView scrollBox;
    private TableLayout objDetails;
    private View view_orderstatus;
    private RatingBar pop_ratingbar;
    private TextView tv_pmode, tv_storename, tv_orderno, tv_orderamount, tv_wdate, tv_dealcount, tv_totalMRP, tv_totalWOWAmount, tv_totalSavings, tv_payableAMount;
    private TextView tv_couponCode, tv_couponAmount, tv_couponCode_cb, tv_cbamount, tv_walletpayment;
    private int orderRating = 0;
    private float ratingg = 0;
    private String  ratingMsg = "", fAmoumntt = "", paymentType = "";
    private LinearLayout lout_couponBox, lout_couponBox_cb, lout_wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objDetails = (TableLayout) findViewById(R.id.objDetails);
        scrollBox = (ScrollView)findViewById(R.id.scrollBox);

        lout_couponBox = (LinearLayout)findViewById(R.id.lout_couponBox);
        lout_couponBox_cb = (LinearLayout)findViewById(R.id.lout_couponBox_cb);
        lout_wallet = (LinearLayout)findViewById(R.id.lout_wallet);

        view_orderstatus = (View)findViewById(R.id.view_orderstatus);
        pop_ratingbar = (RatingBar) findViewById(R.id.pop_ratingbar);
        tv_storename = (TextView) findViewById(R.id.tv_storename);
        tv_orderno = (TextView) findViewById(R.id.tv_orderno);
        tv_orderamount = (TextView) findViewById(R.id.tv_orderamount);
        tv_wdate = (TextView) findViewById(R.id.tv_wdate);
        tv_dealcount = (TextView) findViewById(R.id.tv_dealcount);
        tv_totalMRP = (TextView)findViewById(R.id.tv_totalMRP);
        tv_totalWOWAmount = (TextView)findViewById(R.id.tv_totalWOWAmount);
        tv_totalSavings = (TextView)findViewById(R.id.tv_totalSavings);
        tv_payableAMount = (TextView)findViewById(R.id.tv_payableAMount);
        tv_walletpayment = (TextView)findViewById(R.id.tv_walletpayment);

        tv_pmode = (TextView)findViewById(R.id.tv_pmode);
        tv_couponCode = (TextView)findViewById(R.id.tv_couponCode);
        tv_couponAmount = (TextView)findViewById(R.id.tv_couponAmount);
        tv_couponCode_cb = (TextView)findViewById(R.id.tv_couponCode_cb);
        tv_cbamount = (TextView)findViewById(R.id.tv_cbamount);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
            objOrder = (Order)intent.getSerializableExtra("order");
            toolbar.setTitle(objOrder.getOrderno());

            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(OrderDetailActivity.this);
            if (!networkDetails.isEmpty()) {
                new GettingOrderDetail().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showRatingBox(){
        final Dialog dialog = new Dialog(fthis);
        ratingg = 5;
        dialog.setContentView(R.layout.rate_order);
        dialog.setTitle("Rate Order");
        final TextView tv_warn = (TextView) dialog.findViewById(R.id.tv_warn) ;
        RatingBar rbBar = (RatingBar)dialog.findViewById(R.id.rb_orderRating) ;
        final EditText feedbackMsg = (EditText) dialog.findViewById(R.id.feedbackMsg);
        feedbackMsg.setText("");
        rbBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingg = rating;
                if(rating < 3){
                    tv_warn.setVisibility(View.VISIBLE);
                    feedbackMsg.setVisibility(View.VISIBLE);
                    feedbackMsg.requestFocus();
                    feedbackMsg.setCursorVisible(true);
                    InputMethodManager input = (InputMethodManager) fthis.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    input.showSoftInput(feedbackMsg, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    InputMethodManager inputManager1 = (InputMethodManager) getSystemService(fthis.INPUT_METHOD_SERVICE);
                    inputManager1.hideSoftInputFromWindow(feedbackMsg.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    tv_warn.setVisibility(View.GONE);
                    feedbackMsg.setCursorVisible(false);
                }
            }
        });

        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(fthis.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(feedbackMsg.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if(ratingg <3){
                    ratingMsg = feedbackMsg.getText().toString();
                }else{
                    ratingMsg = "";
                }
                if(ratingg > 0){
                    dialog.dismiss();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(OrderDetailActivity.this);
                    if (!networkDetails.isEmpty()) {
                        new PostingRating().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(fthis.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(feedbackMsg.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setDisplayData(Order currentOrder){
        scrollBox.setVisibility(View.VISIBLE);
        Double totalMRP = 0.0;
        pop_ratingbar.setRating(currentOrder.getOrderrating());
        tv_storename.setText(currentOrder.getStorname());
        //tv_payableAMount.setText(currentOrder.getTotalamount()+"");
        tv_totalSavings.setText(currentOrder.getTotalsavings()+"");
        tv_totalWOWAmount.setText(currentOrder.getTotalamount()+"");
        tv_orderno.setText(currentOrder.getOrderno()+"");
        tv_orderamount.setText(currentOrder.getTotalamount()+"");
        tv_wdate.setText(currentOrder.getDate()+"");
        tv_dealcount.setText(currentOrder.getTotaldeal()+"");

        if(currentOrder.getOrderstatus() == 0){
            view_orderstatus.setBackgroundColor(getResources().getColor(R.color.colorYellowBadge));
        }else if(currentOrder.getOrderstatus() == 1){
            view_orderstatus.setBackgroundColor(getResources().getColor(R.color.colorGreenBarDark));
        }else if(currentOrder.getOrderstatus() == 2){
            view_orderstatus.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }


        for (int i = 0; i < currentOrder.getOrderDetails().size(); i++) {
            totalMRP = totalMRP + currentOrder.getOrderDetails().get(i).getDealMrp() * currentOrder.getOrderDetails().get(i).getDealQty();
            OrderDetail objOrder = currentOrder.getOrderDetails().get(i);

            TableRow tbrow = new TableRow(this);
            tbrow.setOrientation(TableRow.HORIZONTAL);
            tbrow.setPadding(6, 6, 6, 6);

            TextView t1v = new TextView(this);
            t1v.setText(objOrder.getDealname());
            t1v.setTextAppearance(this, R.style.tvTableColumn);
            t1v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);

            LinearLayout linouttv2 = (LinearLayout)getLayoutInflater().inflate(R.layout.table_column_view, null);
            TextView t2v = (TextView) linouttv2.findViewById(R.id.tv_textview);
            t2v.setText(objOrder.getDealQty()+"");
            //t2v.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            t2v.setGravity(Gravity.CENTER| Gravity.CENTER_VERTICAL);
            t2v.setWidth(30);
            linouttv2.setGravity(Gravity.CENTER| Gravity.CENTER_VERTICAL);
            tbrow.addView(linouttv2);

            LinearLayout linouttv3 = (LinearLayout)getLayoutInflater().inflate(R.layout.table_column_view, null);
            TextView t3v = (TextView) linouttv3.findViewById(R.id.tv_textview);
            TextView t33v = (TextView) linouttv3.findViewById(R.id.tv_rsdrawable);
            t33v.setVisibility(View.VISIBLE);
            //t3v.setWidth(80);
            t33v.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            t3v.setText(objOrder.getDealMrp()+"");
            t3v.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            linouttv3.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            //linouttv3.setBackgroundColor(getResources().getColor(R.color.colorBgGreenFooter));
            tbrow.addView(linouttv3);

            LinearLayout linouttv4 = (LinearLayout)getLayoutInflater().inflate(R.layout.table_column_view, null);
            TextView t4v = (TextView) linouttv4.findViewById(R.id.tv_textview);
            TextView t44v = (TextView) linouttv4.findViewById(R.id.tv_rsdrawable);
            t44v.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            t44v.setVisibility(View.VISIBLE);
            t4v.setText(objOrder.getDealamount()+"");
            t4v.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            //LinearLayout.LayoutParams parms4 = new LinearLayout.LayoutParams(90, 24);
            //t4v.setWidth(90);
            linouttv4.setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            //linouttv4.setBackgroundColor(getResources().getColor(R.color.colorRed));
            tbrow.addView(linouttv4);
            objDetails.addView(tbrow);
        }
        if(orderRating == 0 && currentOrder.getOrderstatus() == 1 ) {
            showRatingBox();
        }
        tv_totalMRP.setText(totalMRP + "");

        for(int h=0;h<currentOrder.getPaymentDetails().size();h++){
            PaymentDetails objDetail = currentOrder.getPaymentDetails().get(h);
            if(objDetail.getPaymentmode().equals("2")){
                tv_walletpayment.setText(objDetail.getAmount()+"");
                lout_wallet.setVisibility(View.VISIBLE);
            }else if(objDetail.getPaymentmode().equals("0") ){
                fAmoumntt = objDetail.getAmount()+"";
            }else if( objDetail.getPaymentmode().equals("3")){
                fAmoumntt = objDetail.getAmount()+"";
                tv_pmode.setText("Online");
            }else if(objDetail.getPaymentmode().equals("1")){
                if(objDetail.getReturntype().equals("cashback")){
                    tv_cbamount.setText(objDetail.getAmount()+"");
                    tv_couponCode_cb.setText("Coupon Applied ("+objDetail.getMode()+")");
                    lout_couponBox_cb.setVisibility(View.VISIBLE);
                }else if(objDetail.getReturntype().equals("instant")){
                    tv_couponAmount.setText(objDetail.getAmount()+"");
                    tv_couponCode.setText("Coupon Applied("+objDetail.getMode()+")");
                    lout_couponBox.setVisibility(View.VISIBLE);
                }

            }
        }
        tv_payableAMount.setText(fAmoumntt+"");
        //fAmoumntt

    }

    class GettingOrderDetail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                pDialog = CProgress.ctor(OrderDetailActivity.this);
                pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                orderListObject = jsonParser.getJSONFromUrlAsGET(URLManager.getUserOrderListURL() + "&userid=" + user.getID()+"&orderid="+objOrder.getOrderid());
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(orderListObject != null){
                if(orderListObject.length() > 0){
                    try {
                        for(int i=0;i<orderListObject.length();i++){
                            JSONObject objOrder = orderListObject.getJSONObject(i);

                            morder = new Order();
                            orderRating = Integer.parseInt(objOrder.getString("rating"));
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
                            String tmntsav = CustomUtil.convertTo2Decimal(Double.parseDouble(objOrder.getString("totalsavings")));
                            morder.setTotalsavings(Double.parseDouble(tmntsav));
                            morder.setuserid(objOrder.getString("userid"));
                            morder.setOrderrating(Integer.parseInt(objOrder.getString("rating")));

                            ArrayList<OrderDetail> objorderData = new ArrayList<OrderDetail>();
                            for(int k=0;k<objOrder.getJSONArray("orderdetails").length();k++){
                                JSONObject jobjDetail = objOrder.getJSONArray("orderdetails").getJSONObject(k);
                                OrderDetail objDetail = new OrderDetail();
                                String tmntdealam = CustomUtil.convertTo2Decimal(Double.parseDouble(jobjDetail.getString("dealamount")));
                                objDetail.setDealamount(Double.parseDouble(tmntdealam));
                                objDetail.setDealid(jobjDetail.getString("dealid"));
                                String tmntdeamrp = CustomUtil.convertTo2Decimal(Double.parseDouble(jobjDetail.getString("mrp")));
                                objDetail.setDealMrp(Double.parseDouble(tmntdeamrp));
                                objDetail.setDealname(jobjDetail.getString("title"));
                                objDetail.setDealQty(Integer.parseInt(jobjDetail.getString("dealqty")));
                                objDetail.setOrderid(jobjDetail.getString("orderid"));
                                objorderData.add(objDetail);
                            }
                            morder.setOrderDetails(objorderData);
                            ArrayList<PaymentDetails> objpaymentData = new ArrayList<PaymentDetails>();
                            for(int k=0;k<objOrder.getJSONArray("paymentdetails").length();k++){
                                JSONObject jobjDetail = objOrder.getJSONArray("paymentdetails").getJSONObject(k);
                                PaymentDetails objPayDetail = new PaymentDetails();
                                objPayDetail.setOrderid(jobjDetail.getString("orderid"));
                                String tmntdeamrpaa = CustomUtil.convertTo2Decimal(Double.parseDouble(jobjDetail.getString("amount")));
                                objPayDetail.setAmount(Double.parseDouble(tmntdeamrpaa));
                                objPayDetail.setId(jobjDetail.getString("id"));
                                objPayDetail.setMode(jobjDetail.getString("mode"));

                                objPayDetail.setModetranid(jobjDetail.getString("modetranid"));
                                objPayDetail.setWdate(CustomUtil.getFullDate(jobjDetail.getString("createddate")));
                                objPayDetail.setPaymentmode(jobjDetail.getString("paymentmode"));
                                objPayDetail.setReturntype(jobjDetail.getString("returntype"));
                                objpaymentData.add(objPayDetail);
                            }
                            morder.setPaymentDetails(objpaymentData);
                        }
                        setDisplayData(morder);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No Order Found.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    class PostingRating extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(OrderDetailActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                JSONObject objData = new JSONObject();
                objData.put("orderid", morder.getOrderid());
                objData.put("rating", ratingg);
                objData.put("review", ratingMsg);
                objRating = jsonParser.makeHttpRequestJSON(URLManager.getOrderRatingURL(), "POST", objData);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(objRating != null){
                try{
                    if(objRating.getString("Message").equals("success")){
                        pop_ratingbar.setRating(ratingg);
                        view_orderstatus.setBackgroundColor(getResources().getColor(R.color.colorGreenBarDark));
                    }else{
                        Toast.makeText(getApplicationContext(), objRating.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
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
