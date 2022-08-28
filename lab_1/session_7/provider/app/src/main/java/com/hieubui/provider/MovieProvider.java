package com.hieubui.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieProvider extends ContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".MovieProvider";

    public static final String CONTENT = "content://" + AUTHORITY + "/";

    public static final Uri CONTENT_URI = Uri.parse(CONTENT);

    private static final int MATCH_ALL = 1;

    private static final int MATCH_ID = 2;

    private LabsDatabase database;

    private UriMatcher matcher;

    @Override
    public boolean onCreate() {
        database = new LabsDatabaseImpl(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "movies", MATCH_ALL);
        matcher.addURI(AUTHORITY, "movies/#", MATCH_ID);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
        @NonNull Uri uri,
        @Nullable String[] projection,
        @Nullable String selection,
        @Nullable String[] selectionArgs,
        @Nullable String sortOrder
    ) {
        Cursor cursor = null;

        switch (matcher.match(uri)) {
            case MATCH_ALL:
                cursor = database.getMovies();
                break;

            case MATCH_ID:
                String movieId = uri.getLastPathSegment();

                cursor = database.getMovie(movieId);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (matcher.match(uri) == UriMatcher.NO_MATCH || values == null) {
            return null;
        }

        String name = values.getAsString(LabsDatabaseImpl.Movie.NAME);
        String description = values.getAsString(LabsDatabaseImpl.Movie.DESCRIPTION);

        database.createMovie(name, description);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int result = -1;

        switch (matcher.match(uri)) {
            case MATCH_ALL:
                result = database.deleteMovies();
                break;

            case MATCH_ID:
                String movieId = uri.getLastPathSegment();

                result = database.deleteMovie(movieId);
                break;
        }
        return result;
    }

    @Override
    public int update(
        @NonNull Uri uri,
        @Nullable ContentValues values,
        @Nullable String selection,
        @Nullable String[] selectionArgs
    ) {
        int result = -1;

        switch (matcher.match(uri)) {
            case MATCH_ALL:
                result = database.updateMovies(values, selection, selectionArgs);
                break;

            case MATCH_ID:
                String movieId = uri.getLastPathSegment();

                result = database.updateMovie(movieId, values);
                break;
        }
        return result;
    }
}
