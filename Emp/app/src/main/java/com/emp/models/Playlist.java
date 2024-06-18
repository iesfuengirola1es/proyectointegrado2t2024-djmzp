package com.emp.models;

import java.util.ArrayList;

public class Playlist {
    public static final String TABLE_NAME = "playlists";

    public static final String COLUMN_ID = "rowid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_INDEX = "idx";

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + Playlist.TABLE_NAME + "(" +
            Playlist.COLUMN_ID + " INTEGER PRIMARY KEY," +
            Playlist.COLUMN_NAME + " TEXT," +
            Playlist.COLUMN_CREATED + " INTEGER," +
            Playlist.COLUMN_INDEX + " INTEGER" +
        ")";

    public long id;
    public String name;
    public long created; // unix timestamp
    public int index; // index of current song

    public ArrayList<Song> songs;

    public static class PlaylistSong {
        public static final String TABLE_NAME = "playlists_songs";

        public static final String COLUMN_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_SONG_ID = "song_id";
        public static final String COLUMN_PLAYCOUNT = "playcount";

        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PlaylistSong.TABLE_NAME + "(" +
                PlaylistSong.COLUMN_PLAYLIST_ID + " INTEGER REFERENCES " + Playlist.TABLE_NAME + "(" + Playlist.COLUMN_ID + ")," +
                PlaylistSong.COLUMN_SONG_ID + " INTEGER REFERENCES " + Song.TABLE_NAME + "(" + Song.COLUMN_MS_ID + ")," +
                PlaylistSong.COLUMN_PLAYCOUNT + " INTEGER" +
            ")";

        public long playlistId; // internal id
        public long songId; // mediastore id
        public int playcount; // times listened to
    }
}
