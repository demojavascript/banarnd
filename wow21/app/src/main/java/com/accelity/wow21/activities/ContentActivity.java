package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;

public class ContentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int pageid;
    private String pageTitle;
    private String pageDesc;
    private WebView objcontentWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("Content");
        setSupportActionBar(toolbar);

        objcontentWebview = (WebView)findViewById(R.id.contentWebview);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()) {
            pageid = bundle.getInt("contentpage");
            loadPage(pageid);
        }
    }

    public void loadPage(int pageid){
        objcontentWebview.getSettings().setJavaScriptEnabled(true);
        switch(pageid){
            case 1:
                pageTitle = "About Us";
                objcontentWebview.loadUrl("http://www.wow21deals.com/aboutus.html");
                break;
            case 2:
                pageTitle = "FAQ";
                objcontentWebview.loadUrl("http://www.wow21deals.com/FAQs.html");
                break;
            case 3:
                pageTitle = "Privacy Policy";
                objcontentWebview.loadUrl("http://www.wow21deals.com/PrivacyPolicy.html");
                break;
            case 4:
                pageTitle = "Terms and Conditions";
                objcontentWebview.loadUrl("http://www.wow21deals.com/TermsofUse.html");
                break;
            default:
                break;
        }
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(pageTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
