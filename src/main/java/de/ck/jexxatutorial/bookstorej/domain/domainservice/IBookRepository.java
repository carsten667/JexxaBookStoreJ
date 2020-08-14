package de.ck.jexxatutorial.bookstorej.domain.domainservice;

import java.util.List;
import java.util.Optional;

import de.ck.jexxatutorial.bookstorej.domain.aggregate.Book;
import de.ck.jexxatutorial.bookstorej.domain.valueobject.ISBN13;

public interface IBookRepository
{
    void add(Book book);

    Book get(ISBN13 isbn13);

    boolean isRegistered(ISBN13 isbn13);

    Optional<Book> search(ISBN13 isbn13);

    void update(Book book);

    List<Book> getAll();
}
