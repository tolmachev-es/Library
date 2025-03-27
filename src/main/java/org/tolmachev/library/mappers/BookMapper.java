package org.tolmachev.library.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tolmachev.library.entity.BookEntity;
import org.tolmachev.library.entity.BookInSubscriptionEntity;
import org.tolmachev.library.model.Book;

@Mapper
public interface BookMapper {


    @Mapping(expression = "java(book.getBook().getAuthor())", target = "bookAuthor")
    @Mapping(expression = "java(book.getBook().getName())", target = "bookName")
    Book map(BookInSubscriptionEntity book);

    @Mapping(source = "author", target = "bookAuthor")
    @Mapping(source = "name", target = "bookName")
    Book map(BookEntity book);
}
