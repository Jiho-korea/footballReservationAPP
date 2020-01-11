package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class Splashscreen extends Activity {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();

    }
    private void StartAnimations(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();

        LinearLayout linear = (LinearLayout)findViewById(R.id.lin_lay);
        linear.clearAnimation();
        linear.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();

        ImageView iv = (ImageView)findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1600) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    Splashscreen.this.finish();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }

            }
        };


        splashTread.start();
    }
}
