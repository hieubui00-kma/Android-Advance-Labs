package com.hieubui.session;

import java.util.List;

public interface LabsRepository {

    long createAuthor(String name);

    List<Author> getAuthors();

    Author getAuthor(int authorId);

    int updateAuthor(Author author);

    int deleteAuthor(int authorId);

    long createBook(String title, String introduce, String summary, Author author);

    List<Book> getBooks(int authorId);

    Book getBook(int bookId);

    int updateBook(Book book);

    int deleteBook(int bookId);
}
