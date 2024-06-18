package com.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emp.audio.Player;
import com.emp.audio.PlayerCallback;
import com.emp.models.Artist;
import com.emp.models.Song;
import com.emp.utils.Time;

import java.util.Timer;
import java.util.TimerTask;

public class PlayingActivity extends AppCompatActivity implements PlayerCallback {

    private Player player;

    private ImageView cover;
    private TextView title;
    private TextView artist;

    private ImageButton prev;
    private ImageButton play;
    private ImageButton next;

    private SeekBar seekBar;
    private TextView time;
    private TextView duration;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playing);

        this.cover = this.findViewById(R.id.cover);
        this.title = this.findViewById(R.id.title);
        this.artist = this.findViewById(R.id.artist);

        this.prev = this.findViewById(R.id.prev);
        this.play = this.findViewById(R.id.play);
        this.next = this.findViewById(R.id.next);

        this.seekBar = this.findViewById(R.id.seek_bar);
        this.time = this.findViewById(R.id.time);
        this.duration = this.findViewById(R.id.duration);

        this.player = Emp.getPlayer();

        this.prev.setOnClickListener(v -> PlayingActivity.this.player.prev());
        this.next.setOnClickListener(v -> PlayingActivity.this.player.next());
        this.play.setOnClickListener(v -> PlayingActivity.this.player.playPause());

        this.onPlaybackPlayPaused(this.player.isPlaying());
        this.onSongChanged(this.player.getCurrentSong());
        this.player.register(this);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PlayingActivity.this.seekBar.setProgress(PlayingActivity.this.player.getProgress());
                PlayingActivity.this.time.setText(Time.formatMilliseconds(PlayingActivity.this.player.getTime()));
                PlayingActivity.this.duration.setText(Time.formatMilliseconds(PlayingActivity.this.player.getDuration()));
            }
        }, 0, 200);

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser)
                    return;
                PlayingActivity.this.player.seek(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        /*
        this.gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return Math.abs(e2.getY() - e1.getY()) > 100;
            }
        });
         */
    }

    @Override
    public void onPlaybackPlayPaused(boolean playing) {
        this.play.invalidate();
        this.play.setImageResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void onSongChanged(Song song) {
        this.title.setText(R.string.not_playing);
        this.artist.setText(Artist.UNKNOWN);

        if(song == null)
            return;

        if(song.artist != null)
            this.artist.setText(song.artist);

        if(song.cover != null)
            this.cover.setImageURI(Uri.parse(song.cover));

        this.title.setText(song.title);
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return this.gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }*/
}