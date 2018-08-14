package com.sedatdilmac.otonomi.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sedatdilmac.otonomi.AqRequest.AqStringRequest;
import com.sedatdilmac.otonomi.R;
import com.sedatdilmac.otonomi.Util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sedatdilmac.otonomi.Util.StaticFields.BASE_URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity ";

    private TextView textView, textView2;
    private int i = 1;

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

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        findViewById(R.id.button).setOnClickListener(this);

        aqRequest();

    }

    private void aqRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf(TAG, "onResponse : " + response);

                String aq = response + "\n\n üstteki read.php get in tamamı";
                textView.setText(aq);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf(TAG, "onErrorResponse : " + error);
            }
        };

        AqStringRequest aqStringRequest = new AqStringRequest(TAG, BASE_URL + "application_settings/read.php", null, listener, errorListener);
        MyApplication.get().getRequestQueue().add(aqStringRequest);
    }

    private void aqRequest2(int aq) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf(TAG, "onResponse : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String aq = "";
                    aq += jsonObject.getString("setting_id") + "\n";
                    aq += jsonObject.getString("setting_name") + "\n";
                    aq += jsonObject.getString("setting_value") + "\n";
                    aq += jsonObject.getString("setting_modified") + "\n";
                    textView2.setText(aq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf(TAG, "onErrorResponse : " + error);
            }
        };

        AqStringRequest aqStringRequest = new AqStringRequest(TAG, BASE_URL + "application_settings/read_one.php?setting_id=" + aq, null, listener, errorListener);
        MyApplication.get().getRequestQueue().add(aqStringRequest);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                aqRequest2(i++);
                break;
        }
    }
}
