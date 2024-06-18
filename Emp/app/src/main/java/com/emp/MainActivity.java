package com.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emp.adapters.MainListAdapter;
import com.emp.audio.Player;
import com.emp.audio.PlayerCallback;
import com.emp.db.Connector;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Song;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
/*
import org.schabi.newpipe.extractor.Extractor;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Request;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.extractor.stream.VideoStream;
*/

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlayerCallback {
    private static final int STORAGE_REQUEST_CODE = 1;

    private EditText searchBar;
    private RecyclerView mainList;

    private CardView floatingControls;
    private ImageButton play;
    private TextView title;
    private TextView artist;
    private LinearLayout clickableArea;

    private BottomNavigationView bottomNavigationView;

    private Player player;

    private int getChannelMask(int channels) {
        if(channels == 1)
            return AudioFormat.CHANNEL_OUT_MONO;

        return AudioFormat.CHANNEL_OUT_STEREO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.searchBar = this.findViewById(R.id.search_bar);
        this.mainList = this.findViewById(R.id.main_list);

        this.floatingControls = this.findViewById(R.id.floating_controls);
        this.play = this.floatingControls.findViewById(R.id.play);
        this.clickableArea = this.floatingControls.findViewById(R.id.clickable_area);
        this.title = this.floatingControls.findViewById(R.id.title);
        this.artist = this.floatingControls.findViewById(R.id.artist);

        this.player = Emp.getPlayer();

        this.play.setOnClickListener(v -> this.player.playPause());
        this.clickableArea.setOnClickListener(v -> {
            final Intent in = new Intent(MainActivity.this, PlayingActivity.class);
            MainActivity.this.startActivity(in);
        });
        this.onPlaybackPlayPaused(this.player.isPlaying());
        this.onSongChanged(this.player.getCurrentSong());
        this.player.register(this);

        final ArrayList<Artist> artists = Emp.getArtists();
        final ArrayList<Album> albums = Emp.getAlbums();
        final ArrayList<Song> songs = Emp.getSongs();

        final MainListAdapter adapter = new MainListAdapter(this, artists, albums, songs);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        this.mainList.setAdapter(adapter);
        this.mainList.setLayoutManager(layoutManager);

        this.mainList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isUp = true;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy < -16 && !this.isUp) {
                    searchBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up));
                    this.isUp = true;
                } else if(dy > 0 && this.isUp) {
                    searchBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down));
                    this.isUp = false;
                }
            }
        });

        this.bottomNavigationView = this.findViewById(R.id.navbar);
        this.bottomNavigationView.setSelectedItemId(R.id.action_library);
        this.bottomNavigationView.setOnItemSelectedListener(item -> {
            final int id = item.getItemId();

            Intent in;
            if(id == R.id.action_playlist) {
                in = new Intent(MainActivity.this, PlaylistActivity.class);
            } else if(id == R.id.action_settings) {
                in = new Intent(MainActivity.this, SettingsActivity.class);
            } else {
                return true;
            }

            MainActivity.this.startActivity(in);

            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MainActivity.STORAGE_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
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

        this.title.setText(song.title);
    }
}