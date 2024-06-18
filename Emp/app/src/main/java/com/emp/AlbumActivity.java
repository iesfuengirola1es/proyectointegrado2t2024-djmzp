package com.emp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.emp.adapters.SongListAlbumAdapter;
import com.emp.db.Connector;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumActivity extends AppCompatActivity {

    private ImageView cover;
    private TextView title;
    private TextView artist;
    private RecyclerView songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_album);

        final Intent in = this.getIntent();

        this.cover = this.findViewById(R.id.cover);
        this.title = this.findViewById(R.id.title);
        this.artist = this.findViewById(R.id.artist);
        this.songList = this.findViewById(R.id.song_list);

        final long albumId = in.getLongExtra("albumId", -1);
        final Album album = Emp.getAlbums().stream().filter(a -> a.msId == albumId).findFirst().orElse(null);

        final List<Song> songs = Emp.getSongs().stream().filter(s -> s.albumId == albumId).collect(Collectors.toList());

        final SongListAlbumAdapter adapter = new SongListAlbumAdapter(this, songs);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        this.cover.setImageURI(Uri.parse(album.cover));
        this.title.setText(album.name == null ? Album.UNKNOWN : album.name);
        this.artist.setText(album.artist == null ? Artist.UNKNOWN : album.artist);

        this.songList.setAdapter(adapter);
        this.songList.setLayoutManager(layoutManager);
    }
}
