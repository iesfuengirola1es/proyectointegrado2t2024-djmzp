package com.emp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.emp.adapters.AlbumListAdapter;
import com.emp.adapters.SongListAdapter;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Song;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name;
    private RecyclerView albumList;
    private RecyclerView songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_artist);

        final Intent in = this.getIntent();

        this.image = this.findViewById(R.id.image);
        this.name = this.findViewById(R.id.name);
        this.albumList = this.findViewById(R.id.album_list);
        this.songList = this.findViewById(R.id.song_list);

        final long artistId = in.getLongExtra("artistId", -1);
        final Artist artist = Emp.getArtists().stream().filter(a -> a.msId == artistId).findFirst().orElse(null);

        final List<Album> albums = Emp.getAlbums().stream().filter(a -> a.artistId == artistId).collect(Collectors.toList());
        final List<Song> songs = Emp.getSongs().stream().filter(s -> s.artistId == artistId).collect(Collectors.toList());

        final LinearLayoutManager albumsManager = new LinearLayoutManager(this);
        albumsManager.setOrientation(RecyclerView.HORIZONTAL);
        this.albumList.setLayoutManager(albumsManager);
        this.albumList.setAdapter(new AlbumListAdapter(this, albums));

        final LinearLayoutManager songsManager = new LinearLayoutManager(this);
        songsManager.setOrientation(RecyclerView.VERTICAL);
        this.songList.setLayoutManager(songsManager);
        this.songList.setAdapter(new SongListAdapter(songs));

        this.image.setImageResource(R.drawable.ic_person);
        this.name.setText(artist.name == null ? Artist.UNKNOWN : artist.name);
    }
}