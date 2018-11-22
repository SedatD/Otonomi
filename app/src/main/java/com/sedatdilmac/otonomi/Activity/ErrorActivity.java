package com.sedatdilmac.otonomi.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sedatdilmac.otonomi.R;

public class ErrorActivity extends AppCompatActivity {

    private static final String TAG = "ErrorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
