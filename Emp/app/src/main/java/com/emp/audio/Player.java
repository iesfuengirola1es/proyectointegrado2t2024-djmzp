package com.emp.audio;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.emp.models.Playlist;
import com.emp.models.Song;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Player {
    private Song currentSong;

    private MediaPlayer main;
    private Queue<Song> queue;
    private Playlist history;
    private Playlist playlist;

    private boolean isPrepared;

    private ArrayList<PlayerCallback> clients;

    public Player() {
        this.main = new MediaPlayer();
        final AudioAttributes attr = new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build();
        this.main.setAudioAttributes(attr);
        this.main.setOnCompletionListener(mp -> {
            Player.this.isPrepared = false;
            Player.this.next();
        });

        this.currentSong = null;
        this.isPrepared = false;

        this.clients = new ArrayList<>();
        this.queue = new ArrayDeque<>();
    }

    public void enqueue(Song song) {
        this.queue.offer(song);
    }

    public void prev() {

    }

    public void next() {
        this.pause();
        this.main.reset();

        final Song nextSong = this.getNextSong();
        if(nextSong == null || nextSong.url == null)
            return;

        try {
            this.main.setDataSource(nextSong.url);
            this.main.prepare();

            this.currentSong = nextSong;
            this.isPrepared = true;
            for(PlayerCallback client: this.clients) {
                client.onSongChanged(this.currentSong);
            }
        } catch(IOException | IllegalStateException ignored) {}
    }

    private Song getPreviousSong() {
        // if(this.history != null && this.history.index > )
        return null;
    }

    private Song getNextSong() {
        if(!this.queue.isEmpty())
            return this.queue.remove();

        if(this.playlist != null && this.playlist.index > 0)
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
        if(!this.main.isPlaying())
            this._play();
    }

    public void pause() {
        if(this.main.isPlaying())
            this._pause();
    }

    public void playPause() {
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
