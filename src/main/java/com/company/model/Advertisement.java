package com.company.model;

import com.company.db.Database;
import com.company.enums.AdStatus;
import com.company.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Advertisement {

    private Integer id;
    private String body;
    private String photo;
    private LocalDateTime when= LocalDateTime.now();
    private Double price;
    private AdStatus status = AdStatus.NEW;
    private Boolean isDeleted = false;
    private Boolean isBlocked = false;

    public Advertisement(String photo) {
        id = Database.advertisements.size()+1;
        this.photo = photo;
    }
}
