package org.tolmachev.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "subscription_book")
public class BookInSubscriptionEntity {
    @EmbeddedId
    private BookSubscriptionId bookSubscriptionId;

    @ManyToOne
    @MapsId("subscriptionId")
    @JoinColumn(name = "subscription_id", insertable = false, referencedColumnName = "id")
    private LibrarySubscriptionEntity subscription;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable = false, referencedColumnName = "book_id")
    private BookEntity book;

    @Column(name = "start_date")
    private LocalDate startDate;

    public void setBook(BookEntity book) {
        this.book = book;
        if (bookSubscriptionId == null) {
            bookSubscriptionId = new BookSubscriptionId();
        }
        bookSubscriptionId.setBookId(book != null ? book.getId() : null);
    }

    public void setSubscription(LibrarySubscriptionEntity subscription) {
        this.subscription = subscription;
        if (bookSubscriptionId == null) {
            bookSubscriptionId = new BookSubscriptionId();
        }
        bookSubscriptionId.setSubscriptionId(subscription != null ? subscription.getId() : null);
    }
}
