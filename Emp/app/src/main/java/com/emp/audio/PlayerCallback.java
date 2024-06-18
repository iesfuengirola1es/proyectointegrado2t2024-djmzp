package com.emp.audio;

import com.emp.models.Song;

public interface PlayerCallback {
    void onPlaybackPlayPaused(boolean playing);
    void onSongChanged(Song song);
}
