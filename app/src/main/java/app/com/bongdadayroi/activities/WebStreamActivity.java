package app.com.bongdadayroi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import app.com.bongdadayroi.R;

public class WebStreamActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_stream);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myWebView = (WebView) findViewById(R.id.webview);
        if (myWebView != null) {
            Log.i("link-stream", "link-stream" + getIntent().getStringExtra("video"));
            myWebView.loadUrl(getIntent().getStringExtra("video"));
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new WebViewClient());
        }
        myWebView.post(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
