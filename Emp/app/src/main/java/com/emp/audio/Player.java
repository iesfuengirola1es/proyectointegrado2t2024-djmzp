package com.emp.audio;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.emp.Emp;
import com.emp.models.Playlist;
import com.emp.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private Song currentSong;

    private MediaPlayer main;
    private Stack<Song> queue;
    private Stack<Song> history;
    private Playlist playlist;

    private boolean isPrepared;

    private ArrayList<PlayerCallback> clients;

    public Player(Playlist playList) {
        this.main = new MediaPlayer();
        this.main.setAudioAttributes(new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build());

        this.main.setOnCompletionListener(mp -> {
            Player.this.isPrepared = false;
            Player.this.next();
            Player.this.play();
        });

        this.main.setOnErrorListener((mp, what, extra) -> {
            this.main.release();

            this.main = new MediaPlayer();
            try {
                this.main.setDataSource(this.currentSong.url);
                this.main.prepare();
            } catch(IOException ignored) {}
            return false;
        });

        this.main.setOnPreparedListener(mp -> Player.this.isPrepared = true);

        this.currentSong = null;
        this.isPrepared = false;

        this.playlist = playList;

        this.clients = new ArrayList<>();
        this.queue = new Stack<>();
        this.history = new Stack<>();
    }

    public void enqueue(Song song) {
        this.queue.push(song);
    }

    public void prev() {
        final Song previousSong = this.getPreviousSong();
        if(previousSong == null || previousSong.url == null)
            return;

        try {
            this.main.stop();
            this.main.reset();
            this.main.setDataSource(previousSong.url);
            this.main.prepare();

            this.queue.push(this.currentSong);
            this.currentSong = previousSong;
            this.isPrepared = true;

            for(PlayerCallback client: this.clients) {
                client.onSongChanged(this.currentSong);
            }
        } catch(IOException | IllegalStateException ignored) {}
    }

    public void next() {
        final Song nextSong = this.getNextSong();
        if(nextSong == null || nextSong.url == null)
            return;

        try {
            this.main.stop();
            this.main.reset();
            this.main.setDataSource(nextSong.url);
            this.main.prepare();

            this.history.push(this.currentSong);
            this.currentSong = nextSong;
            this.isPrepared = true;

            for(PlayerCallback client: this.clients) {
                client.onSongChanged(this.currentSong);
            }
        } catch(IOException | IllegalStateException ignored) {}
    }

    private Song getPreviousSong() {
        if(this.history != null && !this.history.empty())
            return this.history.pop();

        return null;
    }

    private Song getNextSong() {
        if(!this.queue.isEmpty())
            return this.queue.pop();

        if(this.playlist != null && this.playlist.index >= 0)
            return this.playlist.songs.get(++this.playlist.index);

        return null;
    }

    public boolean isPlaying() {
        return this.main.isPlaying();
    }

    private void _pause() {
        this.main.pause();

        for(PlayerCallback cb: this.clients)
            cb.onPlaybackPlayPaused(false);
    }

    private void _play() {
        this.main.start();

        for(PlayerCallback cb: this.clients)
            cb.onPlaybackPlayPaused(true);
    }

    public void play() {
        if(this.isPrepared && !this.main.isPlaying())
            this._play();
    }

    public void pause() {
        if(this.isPrepared && this.main.isPlaying())
            this._pause();
    }

    public void playPause() {
        if(!this.isPrepared)
            return;

        if(this.main.isPlaying())
            this._pause();
        else
            this._play();
    }

    public void seek(int percent) {
        final long duration = this.main.getDuration();
        this.main.seekTo(duration * percent / 100, MediaPlayer.SEEK_CLOSEST);
    }

    public MediaPlayer getMediaPlayer() {
        return this.main;
    }

    public void register(PlayerCallback pc) {
        this.clients.add(pc);
    }

    public Song getCurrentSong() {
        return this.currentSong;
    }

    public int getTime() {
        if(!this.isPrepared)
            return 0;

        return this.main.getCurrentPosition();
    }

    public int getDuration() {
        if(!this.isPrepared)
            return 0;

        return this.main.getDuration();
    }

    public int getProgress() {
        if(!this.isPrepared)
            return 0;

        try {
            int duration = this.main.getDuration();
            if(duration <= 0)
                duration = 1;

            return (this.main.getCurrentPosition() * 100) / duration;
        } catch(IllegalStateException e) {
            return 0;
        }
    }
}
