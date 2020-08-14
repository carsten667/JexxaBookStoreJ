package de.ck.jexxatutorial.bookstorej.domain.domainservice;

import de.ck.jexxatutorial.bookstorej.domain.domainevent.BookSoldOut;

public interface IDomainEventPublisher
{
    void publishBookSoldOut(final BookSoldOut bookSoldOut);
}
