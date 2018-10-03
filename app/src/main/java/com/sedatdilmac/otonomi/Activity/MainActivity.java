package com.sedatdilmac.otonomi.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.onesignal.OneSignal;
import com.sedatdilmac.otonomi.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.sedatdilmac.otonomi.Util.StaticFields.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity ";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        try {
            String url = BASE_URL;
            String postData = "onesignalid=" + URLEncoder.encode(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId(), "UTF-8");
            webView.postUrl(url, postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String url = BASE_URL;
            webView.loadUrl(url);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

