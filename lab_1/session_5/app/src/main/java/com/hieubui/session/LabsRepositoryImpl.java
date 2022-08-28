package com.hieubui.session;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LabsRepositoryImpl implements LabsRepository {
    private final LabsDatabase database;

    public LabsRepositoryImpl(LabsDatabase database) {
        this.database = database;
    }

    @Override
    public long createAuthor(String name) {
        return database.createAuthor(name);
    }

    @Override
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        Cursor cursor = database.getAuthors();

        if (cursor == null) {
            return authors;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return authors;
        }

        Author author;

        do {
            author = new Author();

            author.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            author.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            authors.add(author);
        } while (cursor.moveToNext());
        cursor.close();
        return authors;
    }

    @Override
    public Author getAuthor(int authorId) {
        Cursor cursor = database.getAuthor(authorId);

        if (cursor == null) {
            return null;
        }

        Author author = null;

        if (cursor.moveToFirst()) {
            author = new Author();

            author.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            author.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        cursor.close();
        return author;
    }

    @Override
    public int updateAuthor(@NonNull Author author) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", author.getName());
        return database.updateAuthor(author.getID(), contentValues);
    }

    @Override
    public int deleteAuthor(int authorId) {
        database.deleteBooks(authorId);
        return database.deleteAuthor(authorId);
    }

    @Override
    public long createBook(String title, String introduce, String summary, @NonNull Author author) {
        return database.createBook(title, introduce, summary, author.getID());
    }

    @Override
    public List<Book> getBooks(int authorId) {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.getBooks(authorId);

        if (cursor == null) {
            return books;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return books;
        }

        Book book;

        do {
            book = new Book();

            book.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            books.add(book);
        } while (cursor.moveToNext());
        cursor.close();
        return books;
    }

    @Override
    public Book getBook(int bookId) {
        Cursor cursor = database.getBook(bookId);

        if (cursor == null) {
            return null;
        }

        Book book = null;

        if (cursor.moveToFirst()) {
            book = new Book();

            book.setID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            book.setIntroduce(cursor.getString(cursor.getColumnIndexOrThrow("introduce")));
            book.setSummary(cursor.getString(cursor.getColumnIndexOrThrow("summary")));
        }
        cursor.close();
        return book;
    }

    @Override
    public int updateBook(Book book) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", book.getTitle());
        contentValues.put("introduce", book.getIntroduce());
        contentValues.put("summary", book.getSummary());
        return database.updateBook(book.getID(), contentValues);
    }

    @Override
    public int deleteBook(int bookId) {
        return database.deleteBook(bookId);
    }
}
