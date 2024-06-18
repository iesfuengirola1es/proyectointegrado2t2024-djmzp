package com.emp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.Emp;
import com.emp.R;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Song;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private static final int ARTISTS_TITLE_INDEX = 0;
    private static final int ARTISTS_INDEX = 1;
    private static final int ALBUMS_TITLE_INDEX = 2;
    private static final int ALBUMS_INDEX = 3;
    private static final int SONGS_TITLE_INDEX = 4;

    private final Context context;

    private RecyclerView artistList;
    private RecyclerView albumList;
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView title;
        private TextView artist;

        public ViewHolder(View view) {
            super(view);

            this.cover = view.findViewById(R.id.cover);
            this.title = view.findViewById(R.id.title);
            this.artist = view.findViewById(R.id.artist);
        }

        public ImageView getCover() { return this.cover; }
        public TextView getTitle() { return this.title; }
        public TextView getArtist() { return this.artist; }
    }

    public MainListAdapter(Context context, ArrayList<Artist> artists, ArrayList<Album> albums, ArrayList<Song> songs) {
        this.context = context;
        this.artists = artists;
        this.albums = albums;
        this.songs = songs;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context ctx = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(ctx);
/*
        if(viewType > MainListAdapter.SONGS_TITLE_INDEX) {
            final View view = inflater.inflate(R.layout.song_list_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return new MainListAdapter.ViewHolder(view);
        }
  */
        View view;
        switch(viewType) {
            case MainListAdapter.ARTISTS_TITLE_INDEX:
                view = inflater.inflate(R.layout.title_item, parent, false);
                ((TextView) view).setText(R.string.artists);
                break;

            case MainListAdapter.ARTISTS_INDEX:
                if(this.artists.isEmpty()) {
                    view = inflater.inflate(R.layout.text_item, parent, false);
                    ((TextView) view).setText(R.string.no_artists);
                    break;
                }

                view = new RecyclerView(ctx);
                final LinearLayoutManager artistsManager = new LinearLayoutManager(ctx);
                artistsManager.setOrientation(RecyclerView.HORIZONTAL);
                ((RecyclerView) view).setLayoutManager(artistsManager);
                ((RecyclerView) view).setAdapter(new AlbumListAdapter(this.context, this.albums));

                this.artistList = (RecyclerView) view;
                break;

            case MainListAdapter.ALBUMS_TITLE_INDEX:
                view = inflater.inflate(R.layout.title_item, parent, false);
                ((TextView) view).setText(R.string.albums);
                break;

            case MainListAdapter.ALBUMS_INDEX:
                if(this.albums.isEmpty()) {
                    view = inflater.inflate(R.layout.text_item, parent, false);
                    ((TextView) view).setText(R.string.no_albums);
                    break;
                }

                view = new RecyclerView(ctx);
                final LinearLayoutManager albumsManager = new LinearLayoutManager(ctx);
                albumsManager.setOrientation(RecyclerView.HORIZONTAL);
                ((RecyclerView) view).setLayoutManager(albumsManager);
                ((RecyclerView) view).setAdapter(new AlbumListAdapter(this.context, this.albums));

                this.albumList = (RecyclerView) view;
                break;

            case MainListAdapter.SONGS_TITLE_INDEX:
                view = inflater.inflate(R.layout.title_item, parent, false);
                ((TextView) view).setText(R.string.songs);
                break;

            default:
                view = inflater.inflate(R.layout.song_list_item, parent, false);
        }

        return new MainListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position <= MainListAdapter.SONGS_TITLE_INDEX)
            return;

        final Song song = this.songs.get(position);
        if(song.cover != null)
            holder.getCover().setImageURI(Uri.parse(song.cover));

        holder.getTitle().setText(song.title);
        holder.getArtist().setText(song.artist);

        holder.itemView.setOnClickListener(v -> {
            Emp.getPlayer().enqueue(song);
            Emp.getPlayer().next();
            Emp.getPlayer().play();
        });
    }

    @Override
    public int getItemCount() {
        return this.songs.size();
    }
}
