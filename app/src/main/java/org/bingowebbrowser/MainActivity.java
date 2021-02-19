package org.bingowebbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    WebView web;
    private ProgressBar progressBar;
    EditText etxtUrl;
    ImageButton imgBHome,imgBReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        web=findViewById(R.id.web);
        progressBar = findViewById(R.id.progressBar);
        etxtUrl = findViewById(R.id.etxtUrl);
        imgBHome = findViewById(R.id.imgBHome);
        imgBReload = findViewById(R.id.imgBReload);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        web.setWebViewClient(new WebViewClientDemo());
        web.setWebChromeClient(new WebChromeClientDemo());
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);//for enable js in web page
        web.getSettings().setBuiltInZoomControls(true); //for zoom


        imgBReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etxtUrl.getText().toString();
                if (text.contains(".")){
                    if (text.contains("http://")){
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl(String.valueOf(text));
                    }else if (text.contains("https://")){
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl(String.valueOf(text));
                    }else{
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl("http://"+text);
                    }
                }else {
                    web.setWebViewClient(new WebViewClientDemo());
                    web.loadUrl("https://google.com/search?q="+text);
                }
            }
        });

        imgBHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.loadUrl("https://google.com/");
            }
        });

        etxtUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etxtUrl.getText().toString();
                if (text.contains(".")){
                    if (text.contains("http://")){
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl(String.valueOf(s));
                    }else if (text.contains("https://")){
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl(String.valueOf(s));
                    }else{
                        web.setWebViewClient(new WebViewClientDemo());
                        web.loadUrl("http://"+s);
                    }
                }else {
                    web.setWebViewClient(new WebViewClientDemo());
                    web.loadUrl("https://google.com/search?q=" + s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (!isNetworkAvailable()){
            etxtUrl.setText("");
            web.loadUrl("file:///android_asset/offline.html");
        }else{
            web.loadUrl("https://google.com/");
        }

    }





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }
    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}