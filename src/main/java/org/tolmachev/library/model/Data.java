package org.tolmachev.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String username;
    private String userFullName;
    private Boolean userActive;
    private String bookName;
    private String bookAuthor;
}
