package com.wruzjan.ihg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wruzjan.ihg.utils.SignatureView;

public class CaptureSignatureActivity extends Activity {

    private View mView;
    private SignatureView sView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_signature);

        mView = findViewById(R.id.captureSignatureView);
        sView = (SignatureView) mView;
    }

    public void clear(View view) {
        sView.clear();
    }

    public void save(View view) {
        sView.capture();
        finish();
    }

}