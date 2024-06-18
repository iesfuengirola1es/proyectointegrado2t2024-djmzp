package com.emp.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.emp.Emp;
import com.emp.models.Album;
import com.emp.models.Artist;
import com.emp.models.Playlist;
import com.emp.models.Song;

import java.io.IOException;
import java.util.ArrayList;

public class Connector {
    public static final int ALL = -1;

    private static final String[] ARTISTS_PROJECTION = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    };

    private static final String[] ALBUMS_PROJECTIONS = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            // MediaStore.Audio.Albums.ARTIST_ID,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
    };

    private static final String[] SONGS_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE
    };

    public static void refreshMediaStoreData(Context ctx) throws IOException {
        ctx.deleteDatabase(LocalDatabaseHelper.DB_NAME);
        Connector.syncMediaStoreData(ctx);
    }

    public static void syncMediaStoreData(Context ctx) throws IOException {
        final ContentResolver cr = ctx.getContentResolver();

        Connector.syncMediaStoreArtists(cr);
        Connector.syncMediaStoreAlbums(cr);
        Connector.syncMediaStoreSongs(cr);
    }

    public static void syncMediaStoreArtists(ContentResolver cr) throws IOException {
        final Cursor cursor = cr.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            Connector.ARTISTS_PROJECTION,
            null, null, null
        );

        if(cursor == null)
            throw new IOException("Database cursor is null for artists");

        final SQLiteDatabase localDB = Emp.getDb();

        final ContentValues values = new ContentValues();
        while(cursor.moveToNext()) {
            final int msIdIdx = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            final int nameIdx = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

            values.put(Artist.COLUMN_MS_ID, cursor.getLong(msIdIdx));
            values.put(Artist.COLUMN_NAME, nameIdx < 0 ? Artist.UNKNOWN : cursor.getString(nameIdx));

            localDB.insert(Artist.TABLE_NAME, null, values);
            values.clear();
        }

        Emp.d("Retrieved " + cursor.getCount() + " artists from the MediaStore");

        cursor.close();
    }

    public static void syncMediaStoreAlbums(ContentResolver cr) throws IOException {
        final Cursor cursor = cr.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            Connector.ALBUMS_PROJECTIONS,
            null, null, null
        );

        if(cursor == null)
            throw new IOException("Database cursor is null for albums");

        final SQLiteDatabase localDB = Emp.getDb();

        final ContentValues values = new ContentValues();
        while(cursor.moveToNext()) {
            final int msIdIdx = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            final int nameIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            final int artistIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            // final int artistIdIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST_ID);
            final int firstYearIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR);
            final int lastYearIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR);
            final int coverIdx = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            String year = null;
            if(firstYearIdx >= 0 && lastYearIdx >= 0)
                year = cursor.getString(firstYearIdx) + " - " + cursor.getString(lastYearIdx);
            else if(firstYearIdx >= 0)
                year = cursor.getString(firstYearIdx);
            else if(lastYearIdx >= 0)
                year = cursor.getString(lastYearIdx);

            values.put(Album.COLUMN_MS_ID, cursor.getLong(msIdIdx));
            values.put(Album.COLUMN_NAME, nameIdx < 0 ? Album.UNKNOWN : cursor.getString(nameIdx));
            values.put(Album.COLUMN_ARTIST, artistIdx < 0 ? Artist.UNKNOWN : cursor.getString(artistIdx));
            values.put(Album.COLUMN_ARTIST_ID, -1);
            values.put(Album.COLUMN_YEAR, year);
            values.put(Album.COLUMN_COVER, coverIdx < 0 ? null : cursor.getString(coverIdx));

            if(localDB.insert(Album.TABLE_NAME, null, values) < 0)
                Emp.e("Error inserting " + cursor.getString(nameIdx));

            values.clear();
        }

        Emp.d("Retrieved " + cursor.getCount() + " albums from the MediaStore");

        cursor.close();
    }

    public static void syncMediaStoreSongs(ContentResolver cr) throws IOException {
        final Cursor cursor = cr.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            Connector.SONGS_PROJECTION,
            null, null, null
        );

        if(cursor == null)
            throw new IOException("Database cursor is null for media");

        final SQLiteDatabase localDB = Emp.getDb();

        final ContentValues values = new ContentValues();
        while(cursor.moveToNext()) {
            final int msIdIdx = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            final int titleIdx = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            final int artistIdx = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            final int artistIdIdx = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            final int albumIdx = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            final int albumIdIdx = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            final int numberIdx = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
            final int dataIdx = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            final int mimeIdx = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);

            values.put(Song.COLUMN_MS_ID, cursor.getLong(msIdIdx));
            values.put(Song.COLUMN_TITLE, titleIdx < 0 ? cursor.getString(dataIdx) : cursor.getString(titleIdx));
            values.put(Song.COLUMN_ARTIST, artistIdx < 0 ? Artist.UNKNOWN : cursor.getString(artistIdx));
            values.put(Song.COLUMN_ARTIST_ID, cursor.getLong(artistIdIdx));
            values.put(Song.COLUMN_ALBUM, albumIdx < 0 ? Album.UNKNOWN : cursor.getString(albumIdx));
            values.put(Song.COLUMN_ALBUM_ID, albumIdIdx < 0 ? -1 : cursor.getLong(albumIdIdx));
            values.put(Song.COLUMN_NUMBER, numberIdx < 0 ? -1 : cursor.getInt(numberIdx));
            values.put(Song.COLUMN_URL, "file://" + cursor.getString(dataIdx));
            values.put(Song.COLUMN_MIME, mimeIdx < 0 ? "Unknown" : cursor.getString(mimeIdx));
            values.put(Song.COLUMN_COVER, (String) null);

            localDB.insert(Song.TABLE_NAME, null, values);
            values.clear();
        }

        Emp.d("Retrieved " + cursor.getCount() + " songs from the MediaStore");

        cursor.close();
    }

    public static ArrayList<Artist> getArtists() {
        final SQLiteDatabase localDB = Emp.getDb();

        final Cursor cursor = localDB.query(
            Artist.TABLE_NAME, null, null, null, null, null, null
        );

        final ArrayList<Artist> artists = new ArrayList<>();

        while(cursor.moveToNext()) {
            final Artist a = new Artist();

            // final int idIdx = cursor.getColumnIndex(Artist.COLUMN_ID);
            final int msIdIdx = cursor.getColumnIndex(Artist.COLUMN_MS_ID);
            final int nameIdx = cursor.getColumnIndex(Artist.COLUMN_NAME);
            final int imageIdx = cursor.getColumnIndex(Artist.COLUMN_IMAGE);

            // a.id = cursor.getLong(idIdx);
            a.msId = cursor.getLong(msIdIdx);
            a.name = cursor.getString(nameIdx);
            a.image = cursor.getString(imageIdx);

            artists.add(a);
        }

        Emp.d("Retrieved " + artists.size() + " songs from the local database");

        cursor.close();

        return artists;
    }

    public static ArrayList<Album> getAlbums(int artist) {
        final SQLiteDatabase localDB = Emp.getDb();

        String selection = null;
        String[] selectionArgs = null;

        if (artist >= 0) {
            selection = Album.COLUMN_ARTIST + " = ?";
            selectionArgs = new String[1];
            selectionArgs[0] = Integer.toString(artist);
        }

        final Cursor cursor = localDB.query(
            Album.TABLE_NAME, null, selection, selectionArgs, null, null, null
        );

        final ArrayList<Album> albums = new ArrayList<>();

        while(cursor.moveToNext()) {
            final Album a = new Album();

            // final int idIdx = cursor.getColumnIndex(Album.COLUMN_ID);
            final int msIdIdx = cursor.getColumnIndex(Album.COLUMN_MS_ID);
            final int nameIdx = cursor.getColumnIndex(Album.COLUMN_NAME);
            final int artistIdx = cursor.getColumnIndex(Album.COLUMN_ARTIST);
            final int artistIdIdx = cursor.getColumnIndex(Album.COLUMN_ARTIST_ID);
            final int yearIdx = cursor.getColumnIndex(Album.COLUMN_YEAR);
            final int durationIdx = cursor.getColumnIndex(Album.COLUMN_DURATION);
            final int coverIdx = cursor.getColumnIndex(Album.COLUMN_COVER);

            // a.id = cursor.getLong(idIdx);
            a.msId = cursor.getLong(msIdIdx);
            a.name = cursor.getString(nameIdx);
            a.artist = cursor.getString(artistIdx);
            a.artistId = cursor.getLong(artistIdIdx);
            a.year = cursor.getString(yearIdx);
            a.duration = cursor.getInt(durationIdx);
            a.cover = cursor.getString(coverIdx);

            albums.add(a);
        }

        Emp.d("Retrieved " + albums.size() + " songs from the local database");

        cursor.close();

        return albums;
    }


    public static ArrayList<Song> getSongs(int album, int artist) {
        final SQLiteDatabase localDB = Emp.getDb();

        String selection = null;
        String[] selectionArgs = null;
        if(album >= 0) {
            selection = Song.COLUMN_ALBUM + " = ?";
            selectionArgs = new String[1];
            selectionArgs[0] = Integer.toString(album);
        } else if(artist >= 0) {
            selection = Song.COLUMN_ARTIST + " = ?";
            selectionArgs = new String[1];
            selectionArgs[0] = Integer.toString(artist);
        }

        final Cursor cursor = localDB.query(
            Song.TABLE_NAME, null, selection, selectionArgs, null, null, null
        );

        final ArrayList<Song> songs = new ArrayList<>();

        while(cursor.moveToNext()) {
            final Song s = new Song();

            // final int idIdx = cursor.getColumnIndex(Song.COLUMN_ID);
            final int msIdIdx = cursor.getColumnIndex(Song.COLUMN_MS_ID);
            final int titleIdx = cursor.getColumnIndex(Song.COLUMN_TITLE);
            final int artistIdx = cursor.getColumnIndex(Song.COLUMN_ARTIST);
            final int artistIdIdx = cursor.getColumnIndex(Song.COLUMN_ARTIST_ID);
            final int albumIdx = cursor.getColumnIndex(Song.COLUMN_ALBUM);
            final int albumIdIdx = cursor.getColumnIndex(Song.COLUMN_ALBUM_ID);
            final int numberIdx = cursor.getColumnIndex(Song.COLUMN_NUMBER);
            final int urlIdx = cursor.getColumnIndex(Song.COLUMN_URL);
            final int mimeIdx = cursor.getColumnIndex(Song.COLUMN_MIME);
            final int coverIdx = cursor.getColumnIndex(Song.COLUMN_COVER);

            // s.id = cursor.getLong(idIdx);
            s.msId = cursor.getLong(msIdIdx);
            s.title = cursor.getString(titleIdx);
            s.artist = cursor.getString(artistIdx);
            s.artistId = cursor.getLong(artistIdIdx);
            s.album = cursor.getString(albumIdx);
            s.albumId = cursor.getLong(albumIdIdx);
            s.number = cursor.getInt(numberIdx);
            s.url = cursor.getString(urlIdx);
            s.mime = cursor.getString(mimeIdx);
            s.cover = Emp.getAlbums().stream().filter(a -> a.msId == s.albumId).findFirst().map(a -> a.cover).orElse(null);

            songs.add(s);
        }

        Emp.d("Retrieved " + songs.size() + " songs from the local database");

        cursor.close();

        return songs;
    }

    public static ArrayList<Playlist> getPlaylists() {
        return null;
    }

    /*
    public static ArrayList<Playlist> getPlaylists() {
        final SQLiteDatabase localDB = Emp.getDb();

        final Cursor cursor = localDB.rawQuery("SELECT * FROM playlists p JOIN playlists_songs ps ON p.id = ps.playlistId JOIN songs s ON s.msId = ps.songId ORDER BY ps.playlist_id", null);
        final ArrayList<Playlist> playlists = new ArrayList<>();

        Playlist playlist;
        ArrayList<Song> songs;

        long previousId = -1;
        long currentId = -1;

        while(cursor.moveToNext()) {
            // Playlist columns
            final int idIdx = cursor.getColumnIndex(Playlist.COLUMN_ID);
            final int nameIdx = cursor.getColumnIndex(Playlist.COLUMN_NAME);
            final int createdIdx = cursor.getColumnIndex(Playlist.COLUMN_CREATED);
            final int indexIdx = cursor.getColumnIndex(Playlist.COLUMN_INDEX);

            // Playlists_songs columns
            final int playlistIdIdx = cursor.getColumnIndex(Playlist.PlaylistSong.COLUMN_PLAYLIST_ID);
            final int songIdIdx = cursor.getColumnIndex(Playlist.PlaylistSong.COLUMN_SONG_ID);
            final int playcountIdx = cursor.getColumnIndex(Playlist.PlaylistSong.COLUMN_PLAYCOUNT);

            // Song columns
            final int msIdIdx = cursor.getColumnIndex(Song.COLUMN_MS_ID);
            final int titleIdx = cursor.getColumnIndex(Song.COLUMN_TITLE);
            final int artistIdx = cursor.getColumnIndex(Song.COLUMN_ARTIST);
            final int artistIdIdx = cursor.getColumnIndex(Song.COLUMN_ARTIST_ID);
            final int albumIdx = cursor.getColumnIndex(Song.COLUMN_ALBUM);
            final int albumIdIdx = cursor.getColumnIndex(Song.COLUMN_ALBUM_ID);
            final int numberIdx = cursor.getColumnIndex(Song.COLUMN_NUMBER);
            final int urlIdx = cursor.getColumnIndex(Song.COLUMN_URL);
            final int mimeIdx = cursor.getColumnIndex(Song.COLUMN_MIME);
            final int coverIdx = cursor.getColumnIndex(Song.COLUMN_COVER);

            currentId = cursor.getLong(playlistIdIdx);

            if(currentId != previousId && previousId != -1) {
                playlist = new Playlist();
                playlist.index = cursor.getLong()
            }

            previousId = currentId;
        }
    }*/

}
