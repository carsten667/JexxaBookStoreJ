package de.ck.jexxatutorial.bookstorej.domain.businessexception;

public class BookNotInStockException extends Exception
{
    public BookNotInStockException(final String message)
    {
        super(message);
    }
}
