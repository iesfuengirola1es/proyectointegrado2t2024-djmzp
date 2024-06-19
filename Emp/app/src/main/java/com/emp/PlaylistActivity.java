package com.emp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;

import com.emp.audio.Player;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView playlists;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playlist);

        this.playlists = this.findViewById(R.id.playlists);

        this.bottomNavigationView = this.findViewById(R.id.navbar);
        this.bottomNavigationView.setSelectedItemId(R.id.action_library);
        this.bottomNavigationView.setOnItemSelectedListener(item -> {
            final int id = item.getItemId();

            Intent in;
            if(id == R.id.action_library) {
                return true;
            } else if(id == R.id.action_settings) {
                in = new Intent(PlaylistActivity.this, SettingsActivity.class);
            } else {
                in = new Intent(PlaylistActivity.this, MainActivity.class);
            }

            PlaylistActivity.this.startActivity(in);
            return true;
        });
    }
}