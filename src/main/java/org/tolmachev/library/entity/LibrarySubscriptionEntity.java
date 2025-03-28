package org.tolmachev.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscription")
public class LibrarySubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_active")
    private Boolean active;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<BookInSubscriptionEntity> books = new HashSet<>();

    public synchronized void addBook(BookEntity book) {
        BookInSubscriptionEntity bookInSubscriptionEntity = new BookInSubscriptionEntity(this, book);
        bookInSubscriptionEntity.getBook().addSubscription(bookInSubscriptionEntity);
        books.add(bookInSubscriptionEntity);
    }
}
