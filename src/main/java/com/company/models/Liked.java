package com.company.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Liked {

    private Long id;
    private Long userId;
    private Long productId;
    private Boolean isDeleted;

}
