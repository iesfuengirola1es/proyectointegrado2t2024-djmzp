package com.emp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;

import com.emp.audio.Player;

import java.io.IOException;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playlist);

        this.playlists = this.findViewById(R.id.playlists);
    }
}