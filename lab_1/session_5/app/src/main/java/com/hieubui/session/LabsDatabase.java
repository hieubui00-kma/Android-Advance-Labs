package com.hieubui.session;

import android.content.ContentValues;
import android.database.Cursor;

public interface LabsDatabase {

    long createAuthor(String name);

    Cursor getAuthors();

    Cursor getAuthor(int authorId);

    int updateAuthor(int authorId, ContentValues values);

    int deleteAuthor(int authorId);

    long createBook(String title, String introduce, String summary, int authorId);

    Cursor getBooks(int authorId);

    Cursor getBook(int bookId);

    int updateBook(int bookId, ContentValues values);

    int deleteBooks(int authorId);

    int deleteBook(int bookId);
}
