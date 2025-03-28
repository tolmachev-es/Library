package org.tolmachev.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private Set<BookInSubscriptionEntity> subscription = new HashSet<>();

    public void addSubscription(BookInSubscriptionEntity bookInSubscriptionEntity) {
        this.subscription.add(bookInSubscriptionEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(author, that.author) && Objects.equals(publishDate,
                that.publishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, publishDate);
    }
}
