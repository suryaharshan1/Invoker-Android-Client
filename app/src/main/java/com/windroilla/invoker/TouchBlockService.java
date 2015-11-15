package com.windroilla.invoker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchBlockService extends Service implements View.OnTouchListener {
    private final float BLOCK_DISTANCE = 3000f;
    private TouchBlockView mVideoProtectorView = null;
    private float mX;
    private float mY;
    private float mLastX;
    private float mLastY;

    public TouchBlockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVideoProtectorView = new TouchBlockView(this);
        mVideoProtectorView.setOnTouchListener(this);
        Log.i("TouchBlocker", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TouchBlocker", "onStartCommand");
        mVideoProtectorView.show();
        return START_STICKY;
    }

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
                mVideoProtectorView.dismiss();
                stopSelf();
                Log.i("TouchBlocker", "Stop self");
            }
        } else {
            mX = 0;
            mY = 0;
            mVideoProtectorView.hideStatus();
        }

        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    class TouchBlockView {
        private Context mContext;
        private View mBlockerView;

        public TouchBlockView(Context context) {
            mContext = context;
            mBlockerView = View.inflate(mContext, R.layout.screen_touch_blocker, null);
        }

        public void setOnTouchListener(View.OnTouchListener l) {
            mBlockerView.setOnTouchListener(l);
        }

        public void show() {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.TRANSLUCENT);
            params.setTitle("Satellite Info");
            hideStatus();

            wm.addView(mBlockerView, params);
        }

        public void hideStatus() {
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            mBlockerView.setSystemUiVisibility(uiOptions);
        }

        public void dismiss() {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mBlockerView);
        }
    }
}
