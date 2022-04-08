package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Photo {

    private Long id;
    private String fileId;
    private Long productId;
    private Boolean isDeleted;

}
