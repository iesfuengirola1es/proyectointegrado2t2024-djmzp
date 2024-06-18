package com.emp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.R;
import com.emp.models.Song;

import java.util.List;

public class SongListAlbumAdapter extends RecyclerView.Adapter<SongListAlbumAdapter.ViewHolder> {
    private final List<Song> songs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView track;
        private final TextView title;

        public ViewHolder(View view) {
            super(view);

            this.track = view.findViewById(R.id.track);
            this.title = view.findViewById(R.id.title);
        }

        public TextView getTrack() {
            return this.track;
        }
        public TextView getTitle() {
            return this.title;
        }
    }

    public SongListAlbumAdapter(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongListAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.song_list_item_album, viewGroup, false);

        return new SongListAlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAlbumAdapter.ViewHolder holder, int position) {
        final Song song = this.songs.get(position);

        holder.getTrack().setText(song.number < 0 ? "#" : Integer.toString(song.number));
        holder.getTitle().setText(song.title);
    }

    @Override
    public int getItemCount() {
        return this.songs.size();
    }
}
