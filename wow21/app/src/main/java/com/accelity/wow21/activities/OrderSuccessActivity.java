package com.accelity.wow21.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;

public class OrderSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private OrderSuccessActivity fthis;
    private TextView tv_orderamountt, tv_orderidd, tv_myorderss, tv_orderText, tv_orderText2;
    private Button btn_continueshopp;
    private String orderId, orderAmount, paymentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);


        tv_myorderss = (TextView)findViewById(R.id.tv_myorderss);
        tv_myorderss.setOnClickListener(this);
        tv_orderidd = (TextView)findViewById(R.id.tv_orderidd);
        tv_orderText = (TextView)findViewById(R.id.tv_orderText);
        tv_orderText2 = (TextView)findViewById(R.id.tv_orderText2);
        tv_orderamountt = (TextView)findViewById(R.id.tv_orderamountt);
        btn_continueshopp = (Button)findViewById(R.id.btn_continueshopp);
        btn_continueshopp.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()){
            paymentType = intent.getStringExtra("paymentType");
            orderAmount = intent.getStringExtra("orderAmount");
            orderId = intent.getStringExtra("orderId");
            tv_orderamountt.setText(orderAmount);
            tv_orderidd.setText(orderId);
            toolbar.setTitle(orderId);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            if(paymentType.equals("Online")){
                tv_orderText.setText("You have paid ");
                tv_orderamountt.setText(orderAmount +" Online");
            }

        }

    }
    public void onBackPressed() {
        Intent i = new Intent(OrderSuccessActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_myorderss:
                goToOrders();
                break;
            case R.id.btn_continueshopp:
                goToDeals();
                break;
        }
    }
    public void goToOrders(){
        Intent intent = new Intent(OrderSuccessActivity.this, OrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isSuccess", true);
        startActivity(intent);
    }
    public void goToDeals(){
        Intent intent = new Intent(OrderSuccessActivity.this, DealListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isSuccess", true);
        startActivity(intent);
    }
}
