package de.ck.jexxatutorial.bookstorej.domain.aggregate;

import java.util.Optional;

import de.ck.jexxatutorial.bookstorej.domain.domainevent.BookSoldOut;
import de.ck.jexxatutorial.bookstorej.domain.valueobject.ISBN13;
import io.jexxa.addend.applicationcore.Aggregate;
import io.jexxa.addend.applicationcore.AggregateFactory;
import io.jexxa.addend.applicationcore.AggregateID;


@Aggregate
public class Book
{
    private ISBN13 isbn13;
    private int amount;

    private Book(final ISBN13 isbn13, final int amount)
    {
        this.isbn13 = isbn13;
        this.amount = amount;
    }

    @AggregateFactory(Book.class)
    public static Book createBook(final ISBN13 isbn13, final int amount)
    {
        return new Book(isbn13, amount);
    }

    @AggregateID
    public ISBN13 getISBN13()
    {
        return isbn13;
    }

    public int getAmount()
    {
        return amount;
    }

    public Optional<BookSoldOut> sellBook()
    {
        amount--;
        BookSoldOut bookSoldOut = null;
        if (amount == 0)
        {
            bookSoldOut = new BookSoldOut();
        }
        return Optional.ofNullable(bookSoldOut);
    }


}
