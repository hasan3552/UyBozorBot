package com.company.model;

import com.company.db.Database;
import com.company.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    private Long id;
    private Long userId;
    private Integer categoryId;

    private String text;
    private Integer locationId;
    private String contactProduct;
    private LocalDateTime when;
    private String fileId;

    private Boolean isDeleted = false;
    private Boolean isSending = false;
    private ProductStatus status = ProductStatus.NEW;

    public Product(Long userId, Integer categoryId) {

        this.userId = userId;
        this.categoryId = categoryId;
        //System.out.println("Database.products.size() = " + Database.products.size());
        id =(long) Database.products.size()+1;
        when = LocalDateTime.now();

    }

//    public Product(Long id, Long userId, Integer categoryId, String text, Integer locationId,
//                   String contactProduct, LocalDateTime when, String fileId, Boolean isDeleted, Boolean isSending) {
//        this.id = id;
//        this.userId = userId;
//        this.categoryId = categoryId;
//        this.text = text;
//        this.locationId = locationId;
//        this.contactProduct = contactProduct;
//        this.when = when;
//        this.fileId = fileId;
//        this.isDeleted = isDeleted;
//        this.isSending = isSending;
//    }
}
