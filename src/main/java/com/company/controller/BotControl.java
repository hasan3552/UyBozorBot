package com.company.controller;

import com.company.db.Database;
import com.company.db.DbConnection;
import com.company.enums.Role;
import com.company.model.User;
import com.company.service.BotService;
import com.company.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public class BotControl extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "bot_uybozorbot";
    }

    @Override
    public String getBotToken() {
        return "5228556568:AAHfxqvgWFAgvoWziMghG6W3KqsztACZZRQ";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();
            Optional<User> optional = Database.customers.stream()
                    .filter(user -> user.getId().equals(message.getChatId())).findAny();

            if (optional.isPresent()) {
                User user = optional.get();

                if (user.getRole().equals(Role.ADMIN)) {

                    AdminController adminController = new AdminController(message, user);
                    adminController.start();

                } else if (user.getRole().equals(Role.CUSTOMER)) {

                    UserController userController = new UserController(message, user);
                    userController.start();

                } else if (user.getRole().equals(Role.REGISTER)) {

                    UserService userService = new UserService(message, user);
                    userService.register();

                }
            } else {
                User user = new User(message.getChatId(), message.getFrom().getUserName());

                Database.customers.add(user);
               // DbConnection.addCustomer(user);

                BotService botService = new BotService(message, user);
                botService.start();

            }

        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();

            Optional<User> optional = Database.customers.stream()
                    .filter(user -> user.getId().equals(callbackQuery.getFrom().getId()))
                    .findAny();
            if (optional.isPresent()) {

                User user = optional.get();

                if (user.getRole().equals(Role.ADMIN)) {

                    AdminController adminController = new AdminController(message, user);
                    adminController.workCallbackQuery(callbackQuery);

                } else if (user.getRole().equals(Role.CUSTOMER)) {

                    UserController userController = new UserController(message, user);
                    userController.workCallbackQuery(callbackQuery);


                } else if (user.getRole().equals(Role.REGISTER)) {

                    BotService botService = new BotService(message, user);
                    botService.workCallbackQuery(callbackQuery);
                }

            }

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            sendMsg(deleteMessage);

        }

    }


    public void sendMsg(EditMessageText editMessageText) {

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SendPhoto sendPhoto) {

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SendDocument sendDocument) {

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
        public void sendMsg(SendContact sendContact) {

        try {
            execute(sendContact);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(SendMessage sendMessage) {

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(DeleteMessage deleteMessage) {

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
