package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Location {

    private Integer id;
    private Double lang;
    private Double late;
    private Boolean isDeleted = false;

}
