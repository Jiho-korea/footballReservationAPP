package com.example.footballreservationapp;

import android.renderscript.ScriptIntrinsicLUT;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PrivacyTermPopupActivity extends AppCompatActivity {
    ScrollView privacyScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_term_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.7), (int)(height * 0.7));

        privacyScrollView = (ScrollView)findViewById(R.id.privacyScrollView);

        ConstraintLayout.LayoutParams sp = (ConstraintLayout.LayoutParams)privacyScrollView.getLayoutParams();

        sp.height = (int)(height * 0.6);

        privacyScrollView.setLayoutParams(sp);
    }
}
