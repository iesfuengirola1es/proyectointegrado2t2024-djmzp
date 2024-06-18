package com.emp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.R;
import com.emp.models.Song;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private final ArrayList<Song> songs;
    private View.OnClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cover;
        private final TextView title;

        public ViewHolder(View view) {
            super(view);

            this.cover = view.findViewById(R.id.cover);
            this.title = view.findViewById(R.id.title);
        }

        public ImageView getCover() {
            return this.cover;
        }
        public TextView getTitle() {
            return this.title;
        }
    }

    public SongListAdapter(ArrayList<Song> songs, View.OnClickListener listener) {
        this.songs = songs;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.song_list_item, viewGroup, false);

        view.setOnClickListener(this.onClickListener);

        return new SongListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Song song = this.songs.get(position);

        if(song.cover != null)
            holder.getCover().setImageURI(Uri.parse(song.cover));

        holder.getTitle().setText(song.title);
    }

    @Override
    public int getItemCount() {
        return this.songs.size();
    }


}
