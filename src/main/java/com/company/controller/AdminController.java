package com.company.controller;

import com.company.enums.Language;
import com.company.model.User;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
public class AdminController extends Thread {

    private Message message;
    private User user;
    private Language language;

    public AdminController(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {



    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

    }
}
