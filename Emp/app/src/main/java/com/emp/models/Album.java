package com.emp.models;

public class Album implements Model {
    public static final String TABLE_NAME = "albums";

    public static final String COLUMN_ID = "rowid";
    public static final String COLUMN_MS_ID = "ms_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_ARTIST_ID = "artist_id";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_COVER = "cover";

    public static final String UNKNOWN = "Unknown";

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + Album.TABLE_NAME + "(" +
            Album.COLUMN_ID + " INTEGER PRIMARY KEY," +
            Album.COLUMN_MS_ID + " INTEGER UNIQUE NOT NULL," +
            Album.COLUMN_NAME + " TEXT," +
            Album.COLUMN_ARTIST + " TEXT," +
            Album.COLUMN_ARTIST_ID + " INTEGER REFERENCES " + Artist.TABLE_NAME + "(" + Artist.COLUMN_MS_ID + ")," +
            Album.COLUMN_YEAR + " TEXT," +
            Album.COLUMN_DURATION + " INTEGER," +
            Album.COLUMN_COVER + " TEXT" +
        ")";

    public long id;
    public long msId;
    public String name;
    public String artist;
    public long artistId;
    public String year;
    public int duration;
    public String cover;
}
