package org.tolmachev.library.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {
    @NotBlank(message = "Поле username не может быть пустым")
    private String username;
    @NotBlank(message = "Поле userFullName не может быть пустым")
    private String userFullName;
    @NotNull(message = "Поле userActive не может быть пустым")
    private Boolean userActive;
    @NotBlank(message = "Поле bookName не может быть пустым")
    private String bookName;
    @NotBlank(message = "Поле bookAuthor не может быть пустым")
    private String bookAuthor;
}
