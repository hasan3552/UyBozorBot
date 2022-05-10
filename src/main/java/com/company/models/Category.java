package com.company.models;

import com.company.database.Database;
import com.company.enumm.CategoryStatus;
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
    private CategoryStatus status = CategoryStatus.NEW;

    public Category(Integer categoryId) {
        this.categoryId = categoryId;
        isDeleted = true;
        id = Database.categories.size()+1;
    }
}
