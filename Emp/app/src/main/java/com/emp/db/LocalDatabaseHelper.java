package com.emp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Playlist;
import com.emp.models.Song;

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "EMP";
    private static final int DB_VERSION = 1;

    public LocalDatabaseHelper(Context ctx) {
        super(ctx, LocalDatabaseHelper.DB_NAME, null, LocalDatabaseHelper.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Playlist.SQL_CREATE_TABLE);
        db.execSQL(Artist.SQL_CREATE_TABLE);
        db.execSQL(Album.SQL_CREATE_TABLE);
        db.execSQL(Song.SQL_CREATE_TABLE);
        db.execSQL(Playlist.PlaylistSong.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
