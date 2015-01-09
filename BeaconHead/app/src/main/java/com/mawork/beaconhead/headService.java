package com.mawork.beaconhead;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class headService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    public String url;

    @Override
    public IBinder onBind(Intent intent) {

        url = intent.getExtras().getString("URL");

        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();



        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.android_head);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(headService.this, WebActivity.class);
                intent.putExtra("URL",url);
                startActivity(intent);

                return false;
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(chatHead != null) windowManager.removeView(chatHead);
    }

}
