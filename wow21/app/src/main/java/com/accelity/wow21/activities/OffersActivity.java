package com.accelity.wow21.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.accelity.wow21.R;

public class OffersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView objcontentWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offers);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("Offers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objcontentWebview = (WebView)findViewById(R.id.contentWebview);
        objcontentWebview.getSettings().setJavaScriptEnabled(true);
        objcontentWebview.loadUrl("http://www.wow21deals.com/offers.html");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        finish();
    }
}
