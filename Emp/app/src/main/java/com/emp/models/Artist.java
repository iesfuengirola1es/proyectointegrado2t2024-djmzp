package com.emp.models;

public class Artist implements Model {
    public static final String TABLE_NAME = "artists";

    public static final String COLUMN_ID = "rowid";
    public static final String COLUMN_MS_ID = "ms_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";

    public static final String UNKNOWN = "Unknown";

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + Artist.TABLE_NAME + "(" +
            Artist.COLUMN_ID + " INTEGER PRIMARY KEY," +
            Artist.COLUMN_MS_ID + " INTEGER NOT NULL," +
            Artist.COLUMN_NAME + " TEXT," +
            Artist.COLUMN_IMAGE + " TEXT" +
        ")";

    public long id;
    public long msId;
    public String name;
    public String image;
}
