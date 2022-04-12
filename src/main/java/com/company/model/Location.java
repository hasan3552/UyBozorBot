package com.company.model;

import com.company.db.Database;
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

    public Location(Double lang, Double late) {
        this.lang = lang;
        this.late = late;
        id= Database.locations.size()+1;
    }
}
