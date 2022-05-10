package com.company.models;

import com.company.database.Database;
import com.company.enumm.AdStatus;
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
