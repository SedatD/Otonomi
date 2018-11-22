package com.sedatdilmac.otonomi.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.sedatdilmac.otonomi.R;
import com.sedatdilmac.otonomi.Util.StaticFields;

import java.net.URLEncoder;

import static com.sedatdilmac.otonomi.Util.StaticFields.BASE_URL;

public class MainActivity extends AppCompatActivity implements OSSubscriptionObserver {

    private static final String TAG = "MainActivity";
    private boolean doubleBackToExitPressedOnce = false;
    private WebView webView;
    private String osi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aqRequest();

        /*FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("ankaotonomi-41f33")
                .setApiKey("AIzaSyDxG9jgxrfo7ADgbPsRmPBb_TttOtM60W0");
        FirebaseApp.initializeApp(this, builder.build());*/

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();

        OneSignal.addSubscriptionObserver(this);

        final ImageView imagwView = findViewById(R.id.imageView);

        setAnimation(imagwView, 1300);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                imagwView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    view.reload();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

        });

        osi = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        if (osi != null) {
            launchWebView();
        }

    }

    private void aqRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        String mUrl = StaticFields.BASE_URL + "onoff.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf(TAG, "onResponse : " + response);
                        if (Integer.parseInt(response.trim()) != 1) {
                            finish();
                            startActivity(new Intent(MainActivity.this, ErrorActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf(TAG, "onErrorResponse : " + error);
                        finish();
                        startActivity(new Intent(MainActivity.this, ErrorActivity.class));
                    }
                });
        queue.add(stringRequest);
    }

    private void setAnimation(View viewToAnimate, int time) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(time);
        viewToAnimate.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            //return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Çıkmak için tekrar basınız", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2400);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                Log.wtf(TAG, "onReceiveValue : " + aBoolean);
            }
        });

    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {

        Log.wtf(TAG, "onOSSubscriptionChanged : " + stateChanges);

        if (!stateChanges.getFrom().getSubscribed() && stateChanges.getTo().getSubscribed()) {
            //new AlertDialog.Builder(this).setMessage("You've successfully subscribed to push notifications!").show();

            //String osi = stateChanges.getTo().getUserId();
            osi = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

            launchWebView();
        }

    }

    private void launchWebView() {
        Log.wtf(TAG, "osi : " + osi);

        try {
            String postData = "onesignalid=" + URLEncoder.encode(osi, "UTF-8");
            webView.postUrl(BASE_URL + "mobile/", postData.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf(TAG, "catch : " + e.getMessage());
            webView.loadUrl(BASE_URL);
        }

    }

}

