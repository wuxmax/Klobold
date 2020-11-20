package com.example.klobold;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.sql.Timestamp;


public class MainActivity extends YouTubeFailureRecoveryActivity {

    YouTubePlayer youTubePlayer;
    private boolean initialized = false;

    ModeManager modeManager;
    LightSensorManager lightSensorManager;

    private Timestamp lastLooTime = null;
    private static long looPauseLimit = 1000 * 60 * 5;

    String currentPlaylist = Config.RR_PLAYLIST_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubeView.initialize(Config.API_KEY, this);

        modeManager = new ModeManager(this);
        lightSensorManager = new LightSensorManager(this);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player_view);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.youTubePlayer = player;
        player.setPlayerStateChangeListener(this);

        if (!wasRestored) {
            loadNewVideo();
            player.setFullscreen(true);
        }

        initialized = true;
////        modeManager.adaptMode(lightSensorManager.isAboveThreshold());
//        System.out.println("before set mode");
//        modeManager.setMode(AppMode.LightMode);
//        System.out.println("after set mode");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lightSensorManager.registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensorManager.unregisterListener();
    }

//    @Override
//    public void onLoaded(java.lang.String s) {
//        modeManager.onNewVideoLoaded();
//    }


    @Override
    public void onError(com.google.android.youtube.player.YouTubePlayer.ErrorReason errorReason) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadNewVideo();
    }

    void loadNewVideo() {
        youTubePlayer.loadPlaylist(currentPlaylist, Util.getRandomVideoIndex(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    boolean isInitialized() {
        return initialized;
    }

    static long getLooPauseLimit() {
        return looPauseLimit;
    }

    Timestamp getLastLooTime() {
        return lastLooTime;
    }

    void setLastLooTime(Timestamp lastLooTime) {
        this.lastLooTime = lastLooTime;
    }

}