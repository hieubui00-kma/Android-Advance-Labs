package com.hieubui.provider;

import android.content.ContentValues;
import android.database.Cursor;

public interface LabsDatabase {

    long createMovie(String name, String description);

    Cursor getMovies();

    Cursor getMovie(String movieId);

    int updateMovie(String movieId, ContentValues values);

    int updateMovies(ContentValues values, String selection, String[] selectionArgs);

    int deleteMovie(String movieId);

    int deleteMovies();
}
