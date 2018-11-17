package com.sedatdilmac.otonomi.Activity;

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
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.onesignal.OneSignal;
import com.sedatdilmac.otonomi.R;

import java.net.URLEncoder;

import static com.sedatdilmac.otonomi.Util.StaticFields.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity ";
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("ankaotonomi-41f33")
                .setApiKey("AIzaSyDxG9jgxrfo7ADgbPsRmPBb_TttOtM60W0");
        FirebaseApp.initializeApp(this, builder.build());

        OneSignal.startInit(this)
                //.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();

        final ImageView imagwView = findViewById(R.id.imageView);

        setAnimation(imagwView, 1000);

        final WebView webView = findViewById(R.id.webView);
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
            }
        });

        /*Thread mSplashThread;
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1750);
                    }
                } catch (InterruptedException ex) {
                    Log.wtf(TAG, "thread catche dustu");
                } finally {
                    webView.setVisibility(View.VISIBLE);
                    imagwView.setVisibility(View.GONE);
                }
            }
        };
        mSplashThread.start();*/

        String osi = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        Log.wtf(TAG, "osi : " + osi);

        try {
            String postData = "onesignalid=" + URLEncoder.encode(osi, "UTF-8");
            webView.postUrl(BASE_URL + "mobile/", postData.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf(TAG, "alt catch : " + e.getMessage() + " / osi : " + osi);
            webView.loadUrl(BASE_URL);
        }

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

    /*@Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {

        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            new AlertDialog.Builder(this)
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show();
            // get player ID
            stateChanges.getTo().getUserId();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }*/

}

