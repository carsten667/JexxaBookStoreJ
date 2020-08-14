package de.ck.jexxatutorial.bookstorej.applicationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import de.ck.jexxatutorial.bookstorej.domain.aggregate.Book;
import de.ck.jexxatutorial.bookstorej.domain.businessexception.BookNotInStockException;
import de.ck.jexxatutorial.bookstorej.domain.domainservice.ReferenceLibrary;
import de.ck.jexxatutorial.bookstorej.domain.valueobject.ISBN13;
import io.jexxa.core.JexxaMain;
import io.jexxa.infrastructure.drivenadapterstrategy.persistence.RepositoryManager;
import io.jexxa.infrastructure.drivenadapterstrategy.persistence.imdb.IMDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookStoreJServiceTest
{
    private static final String DRIVEN_ADAPTER_PERSISTENCE = "de.ck.jexxatutorial.bookstorej.infrastructure.drivenadapter.persistence";
    private static final String DRIVEN_ADAPTER_MESSAGING = "de.ck.jexxatutorial.bookstorej.infrastructure.drivenadapter.console";

    private JexxaMain jexxaMain;


    @BeforeEach
    void initTest()
    {
        // Here you can define the desired DB strategy without adjusting your tests
        // Within your tests you can completely focus on the domain logic which allows
        // you to run the tests as unit tests within daily development or as integration
        // tests on a build server
        RepositoryManager.getInstance().setDefaultStrategy(IMDBRepository.class);
        jexxaMain = new JexxaMain(BookStoreJServiceTest.class.getSimpleName());
        jexxaMain.addToInfrastructure(DRIVEN_ADAPTER_PERSISTENCE)
                .addToInfrastructure(DRIVEN_ADAPTER_MESSAGING);

        //Clean up the repository
        RepositoryManager.getInstance()
                .getStrategy(Book.class, Book::getISBN13, jexxaMain.getProperties())
                .removeAll();

        //Get the latest books when starting the application
        jexxaMain.bootstrap(ReferenceLibrary.class).with(ReferenceLibrary::addLatestBooks);

    }

    @Test
    public void testGetAll()
    {
        //Arrange : Get the inbound port that we would like to test
        var bookStore = jexxaMain.getInstanceOfPort(BookStoreJService.class);

        //Act
        final List<Book> books = bookStore.getBooks();

        //Assert: After adding books via our service, our bookstore must know theses books
        assertFalse(books.isEmpty());
    }

    @Test
    public void testInStock() throws BookNotInStockException
    {
        //Arrange : Get the inbound port that we would like to test
        var bookStore = jexxaMain.getInstanceOfPort(BookStoreJService.class);
        final ISBN13 isbn13 = new ISBN13("978-1-60309-265-4");

        boolean ergebnis = bookStore.inStock(isbn13);

        assertTrue(ergebnis);
    }

    @Test
    public void testAddToStock()
    {
        //Arrange : Get the inbound port that we would like to test
        var bookStore = jexxaMain.getInstanceOfPort(BookStoreJService.class);
        final ISBN13 isbn13 = new ISBN13("978-1-891830-99-3");

        bookStore.addToStock(Book.createBook(isbn13, 2));

        assertEquals(2, bookStore.get(isbn13).getAmount());
    }

    @Test
    public void testSellBook()
    {
        //Arrange : Get the inbound port that we would like to test
        var bookStore = jexxaMain.getInstanceOfPort(BookStoreJService.class);
        final ISBN13 isbn13 = new ISBN13("978-1-60309-265-4");
        bookStore.sellBook(isbn13);

        assertEquals(3, bookStore.get(isbn13).getAmount());

    }
}