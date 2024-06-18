package com.emp.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.media.session.MediaSessionCompat;

import com.emp.R;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
    private final IBinder binder = new LocalBinder();
    private Player player;

    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaController.TransportControls transportControls;

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent in) {
        return this.binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.player = new Player();
        final MediaPlayer mediaPlayer = player.getMediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        this.mediaSessionManager = (MediaSessionManager) this.getSystemService(Context.MEDIA_SESSION_SERVICE);
        this.mediaSession = new MediaSessionCompat(this.getApplicationContext(), this.getResources().getString(R.string.app_name));
        this.mediaSession.setActive(true);

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
            }

            @Override
            public void onPlay() {
                // PlayerService.this.player.start();
            }

            @Override
            public void onPause() {

                //PlayerService.this.player.pause();
            }

            @Override
            public void onSkipToNext() {
                // PlayerService.this.player.next();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
