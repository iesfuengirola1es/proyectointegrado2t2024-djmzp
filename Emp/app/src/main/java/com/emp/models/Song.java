package com.emp.models;

public class Song implements Model {
    public static final String TABLE_NAME = "songs";

    public static final String COLUMN_ID = "rowid";
    public static final String COLUMN_MS_ID = "ms_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_ARTIST_ID = "artist_id";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_ALBUM_ID = "album_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_MIME = "mime";
    public static final String COLUMN_COVER = "cover";

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + Song.TABLE_NAME + "(" +
            Song.COLUMN_ID + " INTEGER PRIMARY KEY," +
            Song.COLUMN_MS_ID + " INTEGER UNIQUE NOT NULL," +
            Song.COLUMN_TITLE + " TEXT," +
            Song.COLUMN_ARTIST + " TEXT," +
            Song.COLUMN_ARTIST_ID + " INTEGER REFERENCES " + Artist.TABLE_NAME + "(" + Artist.COLUMN_MS_ID + ")," +
            Song.COLUMN_ALBUM + " TEXT," +
            Song.COLUMN_ALBUM_ID + " INTEGER REFERENCES " + Album.TABLE_NAME + "(" + Album.COLUMN_MS_ID + ")," +
            Song.COLUMN_NUMBER + " INTEGER," +
            Song.COLUMN_URL + " TEXT," +
            Song.COLUMN_MIME + " TEXT," +
            Song.COLUMN_COVER + " TEXT" +
        ")";

    public long id;
    public long msId;
    public String title;
    public String artist;
    public long artistId;
    public String album;
    public long albumId;
    public int number;
    public String url;
    public String mime;
    public String cover;
}
