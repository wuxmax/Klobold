package com.example.klobold;


import android.view.WindowManager;
import java.sql.Timestamp;



class ModeManager {

    private MainActivity activity;
    private AppMode currentMode;

    ModeManager(MainActivity activity) {
        this.activity = activity;
        this.currentMode = null;
    }

    AppMode getCurrentMode() {
        return currentMode;
    }

    void adaptMode(boolean aboveLightThreshold) {
        if (activity.isInitialized()) {
            if (aboveLightThreshold && currentMode != AppMode.LightMode) {
                setMode(AppMode.LightMode);
            } else if (!aboveLightThreshold && currentMode != AppMode.DarkMode) {
                setMode(AppMode.DarkMode);
            }
        }
    }

    void setMode(AppMode mode) {
        switch (mode) {
            case LightMode:
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (activity.getLastLooTime() == null) {
                    activity.setLastLooTime(now);
                }
                long looPause = now.getTime() - activity.getLastLooTime().getTime();

                setScreenBrightness(activity, mode);

                if (looPause >= MainActivity.getLooPauseLimit()) {
                    // load new video and wait for callback of newVideoLoaded()
                    activity.loadNewVideo();
//                    activity.youTubePlayer.play();
                } else {
                    activity.youTubePlayer.play();
                }

                currentMode = AppMode.LightMode;
                break;
            case DarkMode:
                activity.setLastLooTime(new Timestamp(System.currentTimeMillis()));
                activity.youTubePlayer.pause();
                setScreenBrightness(activity, mode);
                currentMode = AppMode.DarkMode;
                break;
            default:
                break;
        }
    }

//    void onNewVideoLoaded() {
//        if (currentMode == AppMode.LightMode) {
//
//        }
//    }


    private void setScreenBrightness(MainActivity activity, AppMode mode) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        if (mode == AppMode.LightMode) {
            params.screenBrightness = -1; // brightest
        }
        if (mode == AppMode.DarkMode) {
            params.screenBrightness = 0; // darkest
        }
        activity.getWindow().setAttributes(params);
    }

}
