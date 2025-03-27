package org.tolmachev.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookSubscriptionId implements Serializable {
    @Column(name = "subscription_id")
    private Integer subscriptionId;
    @Column(name = "book_id")
    private Integer bookId;

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
}

