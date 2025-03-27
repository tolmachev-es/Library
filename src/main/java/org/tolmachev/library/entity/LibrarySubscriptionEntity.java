package org.tolmachev.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_active")
    private Boolean active;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookInSubscriptionEntity> books = new HashSet<>();

    public void addBook(BookEntity book) {
        BookInSubscriptionEntity bookInSubscriptionEntity = new BookInSubscriptionEntity();
        bookInSubscriptionEntity.setBook(book);
        bookInSubscriptionEntity.setSubscription(this);
        books.add(bookInSubscriptionEntity);
    }

    public void addAllBooks(Collection<BookEntity> book) {
        Set<BookInSubscriptionEntity> collect = book.stream()
                                                    .map(m -> new BookInSubscriptionEntity(this, m))
                                                    .collect(Collectors.toSet());
        books.addAll(collect);
    }

    public List<BookEntity> getAllBooks() {
        return books.stream()
                    .map(BookInSubscriptionEntity::getBook)
                    .toList();
    }
}
