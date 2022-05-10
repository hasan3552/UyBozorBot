package com.company.controllers;

import com.company.database.Database;
import com.company.database.DbConnection;
import com.company.enumm.Role;
import com.company.models.User;
import com.company.services.BotService;
import com.company.services.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static com.company.utils.ComponentContainer.BOT_TOKEN;
import static com.company.utils.ComponentContainer.BOT_USERNAME;

public class BotControl extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
//
            Message message = update.getMessage();

            Optional<User> optional = Database.customers.stream()
                    .filter(user -> user.getId().equals(message.getChatId())).findAny();

            if (optional.isPresent()) {
                User user = optional.get();

                if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN)) {

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
                DbConnection.addCustomer(user);

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

                if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN)) {

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
        }
    }

    public void sendMsg(SendLocation sendLocation) {

        try {
            execute(sendLocation);
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
