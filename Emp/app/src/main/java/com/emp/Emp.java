package com.emp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.emp.audio.Player;
import com.emp.db.Connector;
import com.emp.db.LocalDatabaseHelper;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Song;

import java.util.ArrayList;

public class Emp extends Application {
    public static final String TAG = "EMP";

    private static SQLiteDatabase db;
    private static Player player;

    private static ArrayList<Artist> artists;
    private static ArrayList<Album> albums;
    private static ArrayList<Song> songs;

    @Override
    public void onCreate() {
        super.onCreate();
        Emp.db = new LocalDatabaseHelper(this.getApplicationContext()).getWritableDatabase();

        // TODO remove this, use the service only
        Emp.player = new Player();

        Emp.artists = Connector.getArtists();
        Emp.albums = Connector.getAlbums(-1);
        Emp.songs = Connector.getSongs(-1, -1);
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
