package com.hieubui.consumer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LabsRepositoryImpl implements LabsRepository {
    private final String MOVIE_CONTENT = "content://com.hieubui.provider.MovieProvider/movies";

    private final WeakReference<ContentResolver> contentResolver;

    public LabsRepositoryImpl(WeakReference<ContentResolver> contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public void createMovie(Movie movie) {
        Uri uri = Uri.parse(MOVIE_CONTENT);
        ContentValues values = new ContentValues();

        values.put("name", movie.getName());
        values.put("description", movie.getDescription());
        contentResolver.get().insert(uri, values);
    }

    @Override
    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        Uri uri = Uri.parse(MOVIE_CONTENT);
        Cursor cursor = contentResolver.get().query(uri, null, null, null, null);

        if (cursor == null) {
            return movies;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return movies;
        }

        Movie movie;

        do {
            movie = new Movie();

            movie.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            movie.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            movies.add(movie);
        } while (cursor.moveToNext());
        cursor.close();
        return movies;
    }

    @Override
    public Movie getMovie(String movieId) {
        Uri uri = Uri.parse(MOVIE_CONTENT + "/" + movieId);
        Cursor cursor = contentResolver.get().query(uri, null, null, null, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        Movie movie = new Movie();

        movie.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        movie.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        cursor.close();
        return movie;
    }

    @Override
    public boolean updateMovie(Movie movie) {
        Uri uri = Uri.parse(MOVIE_CONTENT + "/" + movie.getID());
        ContentValues values = new ContentValues();

        values.put("name", movie.getName());
        values.put("description", movie.getDescription());
        contentResolver.get().update(uri, values, null, null);
        return false;
    }

    @Override
    public boolean deleteMovie(String movieId) {
        Uri uri = Uri.parse(MOVIE_CONTENT + "/" + movieId);

        return contentResolver.get().delete(uri, null, null) > 0;
    }

    @Override
    public void deleteMovies() {
        Uri uri = Uri.parse(MOVIE_CONTENT);

        contentResolver.get().delete(uri, null, null);
    }
}
