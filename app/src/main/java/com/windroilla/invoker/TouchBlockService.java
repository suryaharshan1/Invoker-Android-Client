package com.windroilla.invoker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchBlockService extends Service implements View.OnTouchListener, View.OnKeyListener {
    private final float BLOCK_DISTANCE = 3000f;
    private TouchBlockView mVideoProtectorView = null;
    private float mX;
    private float mY;
    private float mLastX;
    private float mLastY;
    private AudioManager audiomanage;
    private int audio;
    private int volume;
    private int[] stream;

    public TouchBlockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVideoProtectorView = new TouchBlockView(this);
        mVideoProtectorView.setOnTouchListener(this);
        mVideoProtectorView.setOnKeyListener(this);
        audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Log.i("TouchBlocker", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TouchBlocker", "onStartCommand");
        mVideoProtectorView.show();
        audio = audiomanage.getRingerMode();

        /*
        stream[0] = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
        stream[1] = audiomanage.getStreamVolume(AudioManager.STREAM_RING);
        stream[2] = audiomanage.getStreamVolume(AudioManager.STREAM_ALARM);
        stream[4] = audiomanage.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audiomanage.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audiomanage.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audiomanage.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        */

        audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mVideoProtectorView != null) {
            mVideoProtectorView.dismiss();
            Log.i("TouchBlocker", "onCreate");
        }
        if(audiomanage != null){
            /*
            audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, 0, stream[0]);
            audiomanage.setStreamVolume(AudioManager.STREAM_RING, 0, stream[1]);
            audiomanage.setStreamVolume(AudioManager.STREAM_ALARM, 0, stream[2]);
            audiomanage.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, stream[4]);
            */
            audiomanage.setRingerMode(audio);
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return true;
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

        public void setOnKeyListener(View.OnKeyListener k) {
            mBlockerView.setOnKeyListener(k);
        }

        public void show() {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (int) (50 * getResources()
                            .getDisplayMetrics().scaledDensity),
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.OPAQUE);
            params.alpha = 0.5F;
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            params.gravity = Gravity.TOP;
            params.setTitle("Satellite Info");
            hideStatus();
            wm.addView(mBlockerView, params);
        }

        public void hideStatus() {
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            mBlockerView.setSystemUiVisibility(uiOptions);
        }

        public void dismiss() {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mBlockerView);
        }
    }
}
