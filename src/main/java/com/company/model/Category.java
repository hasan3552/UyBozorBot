package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Category {

    private Integer id;
    private String nameUz;
    private String nameRu;
    private Integer categoryId;
    private Boolean isDeleted;

}
