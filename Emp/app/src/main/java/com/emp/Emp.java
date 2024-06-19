package com.emp;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import com.emp.audio.Player;
import com.emp.db.Connector;
import com.emp.db.LocalDatabaseHelper;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Playlist;
import com.emp.models.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Emp extends Application {
    public static final String TAG = "EMP";

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static SQLiteDatabase db;
    private static Player player;

    private static Context context;

    private static ArrayList<Artist> artists;
    private static ArrayList<Album> albums;
    private static ArrayList<Song> songs;
    private static ArrayList<Playlist> playlists;

    @Override
    public void onCreate() {
        super.onCreate();
        Emp.context = this.getApplicationContext();
    }

    public static void init() {
        Emp.db = new LocalDatabaseHelper(Emp.context).getWritableDatabase();

        Emp.artists = Connector.getArtists();
        Emp.albums = Connector.getAlbums(-1);
        Emp.songs = Connector.getSongs(-1, -1);
        Emp.playlists = Connector.getPlaylists();

        // TODO remove this, use the service only
        final Playlist main = Emp.playlists.get(0);
        Collections.shuffle(main.songs, new Random());
        Emp.player = new Player(main);
    }

    public static Player getPlayer() {
        return Emp.player;
    }

    public static SQLiteDatabase getDb() {
        return Emp.db;
    }

    public static ArrayList<Artist> getArtists() {
        return Emp.artists;
    }

    public static ArrayList<Album> getAlbums() {
        return Emp.albums;
    }

    public static ArrayList<Song> getSongs() {
        return Emp.songs;
    }

    public static void d(String msg) { Log.d(Emp.TAG, msg); }

    public static void e(String msg) {
        Log.e(Emp.TAG, msg);
    }
}
