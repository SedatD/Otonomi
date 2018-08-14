package com.sedatdilmac.otonomi.Util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SD
 * on 25.07.2018.
 */

public class MyApplication extends Application {

    private static MyApplication _instance;
    private RequestQueue _requestQueue;
    private SharedPreferences _preferences;

    public static MyApplication get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _requestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return _requestQueue;
    }

    public SharedPreferences getPreferences() {
        return _preferences;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        return _preferences.edit();
    }

}
