package com.windroilla.invoker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class TouchBlockActivity extends AppCompatActivity {

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_block);
        ll = (LinearLayout) findViewById(R.id.touch_block_layout);
        ll.setOnTouchListener(new View.OnTouchListener() {
            private final float BLOCK_DISTANCE = 3000f;
            private float mX;
            private float mY;
            private float mLastX;
            private float mLastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int act = event.getAction();
                if (act == MotionEvent.ACTION_DOWN) {
                    mX = 0;
                    mY = 0;
                    mLastX = event.getX();
                    mLastY = event.getY();
                } else if (act == MotionEvent.ACTION_MOVE) {
                    float x = event.getX();
                    float y = event.getY();
                    mX += Math.abs(mLastX - x);
                    mX += Math.abs(mLastY - y);
                    mLastX = x;
                    mLastY = y;

                    //if (mX > BLOCK_DISTANCE || mY > BLOCK_DISTANCE)
                    //	Log.i("TouchBlocker", "Ok to leave");
                } else if (act == MotionEvent.ACTION_UP) {
                    if (mX > BLOCK_DISTANCE || mY > BLOCK_DISTANCE) {
                        //mVideoProtectorView.dismiss();
                        //stopSelf();
                        Log.i("TouchBlocker", "Stop self");
                    }
                } else {
                    mX = 0;
                    mY = 0;
                    //mVideoProtectorView.hideStatus();
                }

                return true;
            }
        });
    }

}
