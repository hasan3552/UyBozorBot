package com.company.model;

import com.company.db.Database;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class InlineAdvertisement {

    private Integer id;
    private String url;
    private Integer advertisementId;
    private Boolean isBlocked = false;

    public InlineAdvertisement(String url, Integer advertisementId) {
        this.url = url;
        this.advertisementId = advertisementId;
        id = Database.inlineAdvertisements.size()+1;
    }
}
