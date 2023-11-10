package io.fluent.data.model.event;


import io.fluent.data.model.entity.BookReview;

public class CheckReviewEvent {
    
    private final BookReview bookReview;

    public CheckReviewEvent(BookReview bookReview) {
        this.bookReview = bookReview;
    }

    public BookReview getBookReview() {
        return bookReview;
    }
        
}
