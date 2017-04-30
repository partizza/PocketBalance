package ua.agwebs.root.events;


import ua.agwebs.root.entity.BalanceBook;

public class BookCreatedEvent {

    private BalanceBook book;

    public BookCreatedEvent(BalanceBook book){
        this.book = book;
    }

    public BalanceBook getBook() {
        return book;
    }

    @Override
    public String toString() {
        return "BookCreatedEvent{" +
                "book=" + book +
                '}';
    }
}
