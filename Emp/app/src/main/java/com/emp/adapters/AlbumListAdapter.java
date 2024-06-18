package com.emp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emp.AlbumActivity;
import com.emp.R;
import com.emp.models.Album;

import java.util.ArrayList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Album> albums;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Album album;

        private final ImageView cover;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            this.cover = view.findViewById(R.id.cover);
            this.name = view.findViewById(R.id.name);
        }

        public Album getAlbum() {
            return album;
        }

        public ImageView getCover() { return this.cover; }
        public TextView getName() { return this.name; }

        public void setAlbum(Album album) {
            this.album = album;

            this.name.setText(this.album.name);

            if(this.album.cover != null)
                this.cover.setImageURI(Uri.parse(this.album.cover));
            else
                this.cover.setImageResource(R.drawable.ic_album_cover);
        }
    }

    public AlbumListAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_item, parent, false);

        return new AlbumListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Album album = this.albums.get(position);
        holder.setAlbum(album);
        holder.itemView.setOnClickListener(v -> {
            final Intent in = new Intent(this.context, AlbumActivity.class);
            in.putExtra("albumId", album.msId);
            this.context.startActivity(in);
        });
    }

    @Override
    public int getItemCount() {
        return this.albums.size();
    }
}
