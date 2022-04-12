package com.company.service;

import com.company.enums.Language;
import com.company.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AdvertisementService extends Thread{

    private Message message;
    private User user;
    private Language language;

    public AdvertisementService(Message message, User user) {
        this.message = message;
        this.user = user;
        language=  user.getLanguage();
    }

    @Override
    public void run() {



    }
}
