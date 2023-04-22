package com.briontacticalsystems.tsubuild152;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    public static final int MSplash_time = 10000;
    String webUrl = "https://student.tsu.edu.ph";
    RelativeLayout relativeLayout;
    Button NointernetBtn;
    private ProgressBar progressBar;
    private WebView mywebView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action Bar Gradient
        ActionBar actionBar = getSupportActionBar();
        Drawable gradient = getResources().getDrawable(R.drawable.gradientbar);
        actionBar.setBackgroundDrawable(gradient);

        // TSU LOGO
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);


        // WebView Tweaks

        mywebView = (WebView) findViewById(R.id.webview);
        mywebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                internetCheck();
                super.onReceivedError(view, request, error);
            }
        });

        mywebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mywebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mywebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mywebView.getSettings().setAllowFileAccess(true);
        mywebView.getSettings().setAllowContentAccess(true);
        mywebView.getSettings().setUseWideViewPort(true);
        mywebView.getSettings().setOffscreenPreRaster(true);
        mywebView.getSettings().setNeedInitialFocus(true);
        mywebView.getSettings().setDatabaseEnabled(true);
        mywebView.getSettings().setDomStorageEnabled(true);
        mywebView.getSettings().setBuiltInZoomControls(true);
        mywebView.getSettings().setDisplayZoomControls(false);
        mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mywebView.getSettings().setSafeBrowsingEnabled(true);
        }
        mywebView.getSettings().setEnableSmoothTransition(true);
        mywebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mywebView.getSettings().setSupportZoom(true);
        mywebView.loadUrl(webUrl);
        mywebView.getSettings().setLoadWithOverviewMode(true);
        mywebView.getSettings().setGeolocationEnabled(false);
        mywebView.getSettings().setAllowContentAccess(true);
        mywebView.getSettings().setMixedContentMode(100);

        // Progress Bar

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mywebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {

                progressBar.setProgress(progress);

                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }

            }


        });

        // Pull Down Refresh WebView Page
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                mywebView.reload();
            }, 1500);
        });
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );

        // No internet Warning Message and Refresh

        NointernetBtn = (Button) findViewById(R.id.Refresh);
        relativeLayout = (RelativeLayout) findViewById(R.id.noCon);
        internetCheck();

        NointernetBtn.setOnClickListener(view -> internetCheck());

        //External Storage permission for saving file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "Permission denied to WRITE_EXTERNAL_STORAGE - Requesting access");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }
        // Download Lines here
        mywebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.portaloptions, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.PortalHome) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://student.tsu.edu.ph/");
        }
        if (id == R.id.Office365) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://portal.office.com/");
        }
        if (id == R.id.mail) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://outlook.office.com/");

        }
        if (id == R.id.TeamsApp) {
            // Open the corresponding app
            mywebView.loadUrl("https://teams.microsoft.com/");
        }
        if (id == R.id.OneDrive) {
            // Open the corresponding app
            mywebView.loadUrl("https://tsueduph-my.sharepoint.com/");
        }
        if (id == R.id.CPass) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://account.activedirectory.windowsazure.com/ChangePassword.aspx");
        }
        if (id == R.id.CPRecovery) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://account.activedirectory.windowsazure.com/passwordreset/Register.aspx");
        }
        if (id == R.id.Logout) {
            // Open the URL in a web browser
            mywebView.loadUrl("https://student.tsu.edu.ph/AzureAD/Account/SignOut");


            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                mywebView.clearHistory();
                mywebView.clearCache(true);
                mywebView.clearFormData();
                mywebView.clearSslPreferences();
            }, MSplash_time);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mywebView.canGoBack()) {

            mywebView.goBack();

        } else {

            super.onBackPressed();

        }
    }

    public void internetCheck() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobiledata = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobiledata.isConnected()) {
            mywebView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            mywebView.reload();

        } else if (wifi.isConnected()) {
            mywebView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            mywebView.reload();
        } else {
            mywebView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

    }
}