package com.emp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.Emp;
import com.emp.R;
import com.emp.models.Song;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private final List<Song> songs;

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

    public SongListAdapter(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.song_list_item, viewGroup, false);

        return new SongListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
