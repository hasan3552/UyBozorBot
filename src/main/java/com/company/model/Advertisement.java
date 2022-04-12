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
    private String inlineName;
    private String inlineUrl;
    private LocalDateTime when = LocalDateTime.now();
    private AdStatus status = AdStatus.NEW;
    private Boolean isDeleted = false;
    private Boolean isSending = false;

    public Advertisement() {
        id = Database.advertisements.size() + 1;

    }
}
