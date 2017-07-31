package com.example.dhruvmj.samplerssfeedreader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @author Dhruv Jariwala
 */

public class WebBrowserActivity extends AppCompatActivity {
    private static final String WEB_VIEW_TITLE = "title";
    private static final String WEB_VIEW_LINK = "link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        WebView webView = (WebView) findViewById(R.id.web_view);
        Bundle bundle = getIntent().getExtras();
        webView.setWebViewClient(new WebViewClient());

        setTitle(bundle.getString(WEB_VIEW_TITLE));
        webView.loadUrl(bundle.getString(WEB_VIEW_LINK));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static void startWebBrowserActivity(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebBrowserActivity.class);
        intent.putExtra(WEB_VIEW_LINK, url);
        intent.putExtra(WEB_VIEW_TITLE, title);
        activity.startActivity(intent);
    }
}
