package com.hieubui.session;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LabsDatabaseImpl extends SQLiteOpenHelper implements LabsDatabase {
    private static final String NAME = "lab_1_session_5_database";

    private static final int VERSION = 1;

    private final SQLiteDatabase database;

    public LabsDatabaseImpl(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        database.execSQL(Author.SCHEMA_CREATE);
        database.execSQL(Book.SCHEMA_CREATE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Book.SCHEMA_DROP);
        database.execSQL(Author.SCHEMA_DROP);
        onCreate(database);
    }

    @Override
    public long createAuthor(String name) {
        ContentValues values = new ContentValues();

        values.put(Author.NAME, name);
        return database.insertOrThrow(Author.TABLE, null, values);
    }

    @Override
    public Cursor getAuthors() {
        return database.query(
            true,
            Author.TABLE,
            new String[]{Author._ID, Author.NAME},
            null, null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Cursor getAuthor(int authorId) {
        return database.query(
            Author.TABLE,
            new String[]{Author._ID, Author.NAME},
            Author._ID + "=?", new String[]{authorId + ""},
            null,
            null,
            null
        );
    }

    @Override
    public int updateAuthor(int authorId, ContentValues values) {
        return database.update(Author.TABLE, values, Author._ID + "=?", new String[]{authorId + ""});
    }

    @Override
    public int deleteAuthor(int authorId) {
        return database.delete(Author.TABLE, Author._ID + "=?", new String[]{authorId + ""});
    }

    @Override
    public long createBook(String title, String introduce, String summary, int authorId) {
        ContentValues values = new ContentValues();

        values.put(Book.TITLE, title);
        values.put(Book.INTRODUCE, introduce);
        values.put(Book.SUMMARY, summary);
        values.put(Book.AUTHOR_ID, authorId);
        return database.insertOrThrow(Book.TABLE, null, values);
    }

    @Override
    public Cursor getBooks(int authorId) {
        return database.query(
            true,
            Book.TABLE,
            new String[]{Book._ID, Book.TITLE},
            Book.AUTHOR_ID + "=?", new String[]{authorId + ""},
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Cursor getBook(int bookId) {
        return database.query(
            Book.TABLE,
            new String[]{Book._ID, Book.TITLE, Book.INTRODUCE, Book.SUMMARY},
            Book._ID + "=?", new String[]{bookId + ""},
            null,
            null,
            null
        );
    }

    @Override
    public int updateBook(int bookId, ContentValues values) {
        return database.update(Book.TABLE, values, Book._ID + "=?", new String[]{bookId + ""});
    }

    @Override
    public int deleteBooks(int authorId) {
        return database.delete(Book.TABLE, Book.AUTHOR_ID + "=?", new String[]{authorId + ""});
    }

    @Override
    public int deleteBook(int bookId) {
        return database.delete(Book.TABLE, Book._ID + "=?", new String[]{bookId + ""});
    }

    public static class Author {
        public static final String TABLE = "authors";

        public static final String SCHEMA_CREATE = "create table " + Author.TABLE + "( "
            + Author._ID + " integer primary key autoincrement, "
            + Author.NAME + " text not null"
            + ");";

        public static final String SCHEMA_DROP = "DROP TABLE IF EXISTS " + Author.TABLE;

        public static final String _ID = "_id";
        public static final String NAME = "name";
    }

    public static class Book {
        public static final String TABLE = "books";

        public static final String SCHEMA_CREATE = "create table " + Book.TABLE + "( "
            + Book._ID + " integer primary key autoincrement, "
            + Book.TITLE + " text not null, "
            + Book.INTRODUCE + " text not null, "
            + Book.SUMMARY + " text, "
            + Book.AUTHOR_ID + " integer not null"
            + ");";

        public static final String SCHEMA_DROP = "DROP TABLE IF EXISTS " + Book.TABLE;

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String INTRODUCE = "introduce";
        public static final String SUMMARY = "summary";
        public static final String AUTHOR_ID = "author_id";
    }
}
