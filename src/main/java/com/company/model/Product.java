package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Product {

    private Long id;
    private Long userId;
    private String text;
    private Integer locationId;
    private String contactProduct;
    private Integer categoryId;
    private LocalDateTime when;

    private Boolean isDeleted;
    private Boolean isSending;


}
