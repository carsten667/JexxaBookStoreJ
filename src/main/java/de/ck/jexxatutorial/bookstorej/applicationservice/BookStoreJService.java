package de.ck.jexxatutorial.bookstorej.applicationservice;

import java.util.List;
import java.util.Optional;

import de.ck.jexxatutorial.bookstorej.domain.aggregate.Book;
import de.ck.jexxatutorial.bookstorej.domain.businessexception.BookNotInStockException;
import de.ck.jexxatutorial.bookstorej.domain.domainevent.BookSoldOut;
import de.ck.jexxatutorial.bookstorej.domain.domainservice.IBookRepository;
import de.ck.jexxatutorial.bookstorej.domain.domainservice.IDomainEventPublisher;
import de.ck.jexxatutorial.bookstorej.domain.valueobject.ISBN13;
import io.jexxa.addend.applicationcore.ApplicationService;

@ApplicationService
public class BookStoreJService
{
    private IBookRepository bookRepository = null;
    private IDomainEventPublisher domainEventPublisher = null;

    public BookStoreJService(final IBookRepository bookRepository, final IDomainEventPublisher domainEventPublisher)
    {
        this.bookRepository = bookRepository;
        this.domainEventPublisher = domainEventPublisher;
    }


    public List<Book> getBooks()
    {
        return bookRepository.getAll();
    }

    public boolean inStock(final ISBN13 isbn13) throws BookNotInStockException
    {
        if (!bookRepository.isRegistered(isbn13))
        {
            throw new BookNotInStockException("Book with isbn " + isbn13 + " is not in stock");
        }
        return true;
    }

    public void addToStock(final Book book)
    {

        bookRepository.add(book);
    }

    public void sellBook(final ISBN13 isbn13)
    {
        Book book = get(isbn13);
        final Optional<BookSoldOut> bookSoldOut = book.sellBook();

        if (bookSoldOut.isPresent())
        {
            domainEventPublisher.publishBookSoldOut(new BookSoldOut());
        }
    }

    public Book get(final ISBN13 isbn13)
    {
        return bookRepository.get(isbn13);
    }

}
