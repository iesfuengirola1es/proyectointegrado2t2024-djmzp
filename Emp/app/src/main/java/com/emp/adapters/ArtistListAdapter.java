package com.emp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.ArtistActivity;
import com.emp.R;
import com.emp.models.Artist;

import java.util.ArrayList;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Artist> artists;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Artist artist;

        private final ImageView cover;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            this.cover = view.findViewById(R.id.cover);
            this.name = view.findViewById(R.id.name);
        }

        public Artist getArtist() {
            return artist;
        }

        public ImageView getCover() { return this.cover; }
        public TextView getName() { return this.name; }

        public void setArtist(Artist artist) {
            this.artist = artist;

            this.name.setText(this.artist.name);
            this.cover.setImageResource(R.drawable.ic_person);
        }
    }

    public ArtistListAdapter(Context context, ArrayList<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_item, parent, false);

        return new ArtistListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Artist artist = this.artists.get(position);
        holder.setArtist(artist);
        holder.itemView.setOnClickListener(v -> {
            final Intent in = new Intent(this.context, ArtistActivity.class);
            in.putExtra("artistId", artist.msId);
            this.context.startActivity(in);
        });
    }

    @Override
    public int getItemCount() {
        return this.artists.size();
    }
}