package com.hieubui.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LabsDatabaseImpl extends SQLiteOpenHelper implements LabsDatabase {
    private static final String NAME = "lab_1_session_7_database";

    private static final int VERSION = 1;

    private final SQLiteDatabase database;

    public LabsDatabaseImpl(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        String schema = "create table " + Movie.TABLE + "( "
            + Movie._ID + " integer primary key autoincrement, "
            + Movie.NAME + " text not null, "
            + Movie.DESCRIPTION + " text not null"
            + ");";

        database.execSQL(schema);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase database, int oldVersion, int newVersion) {
        String schema = "DROP TABLE IF EXISTS " + Movie.TABLE;

        database.execSQL(schema);
        onCreate(database);
    }

    @Override
    public long createMovie(String name, String description) {
        ContentValues values = new ContentValues();

        values.put(Movie.NAME, name);
        values.put(Movie.DESCRIPTION, description);
        return database.insertOrThrow(Movie.TABLE, null, values);
    }

    @Override
    public Cursor getMovies() {
        return database.query(
            true,
            Movie.TABLE,
            new String[]{Movie._ID, Movie.NAME, Movie.DESCRIPTION},
            null, null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Cursor getMovie(String movieId) {
        return database.query(
            Movie.TABLE,
            new String[]{Movie._ID, Movie.NAME, Movie.DESCRIPTION},
            Movie._ID + "=?", new String[]{movieId},
            null,
            null,
            null
        );
    }

    @Override
    public int updateMovie(String movieId, ContentValues values) {
        return updateMovies(values, Movie._ID + "=?", new String[]{movieId});
    }

    @Override
    public int updateMovies(ContentValues values, String selection, String[] selectionArgs) {
        return database.update(Movie.TABLE, values, selection, selectionArgs);
    }

    @Override
    public int deleteMovie(String movieId) {
        return database.delete(Movie.TABLE, Movie._ID + "=?", new String[]{movieId});
    }

    @Override
    public int deleteMovies() {
        return database.delete(Movie.TABLE, null, null);
    }

    public static class Movie {
        public static final String TABLE = "movies";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }
}
