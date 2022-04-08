package com.company.service;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.Category;
import com.company.model.User;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Getter
@Setter
public class UserService extends Thread {

    private Message message;
    private User user;
    private Language language;

    public UserService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {


    }

    public void register() {

        if (message.hasContact() && user.getStatus().equals(Status.GIVE_CONTACT)) {

            user.setStatus(Status.GIVE_LANGUAGE);
            user.setPhoneNumber(message.getContact().getPhoneNumber());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("Change language");

            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(Status.GIVE_FULL_NAME) && message.hasText()) {

            user.setStatus(Status.MENU);
            user.setRole(Role.CUSTOMER);
            user.setFullName(message.getText());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "<b>Assalomu aleykum " + user.getFullName() + "</b>\nOnline uy bozor botiga xush kelipsiz\uD83D\uDE0A" :
                    "<b> Здравствуйте " + user.getFullName() + "</b>\nДобро пожаловать в бот для домашнего онлайн-рынка\uD83D\uDE0A");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
            sendMessage.setReplyMarkup(menu);
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

    }

    public void showCategoryToUser(Integer categoryId) {

        List<Category> categories = Database.categories.stream()
                .filter(category -> category.getCategoryId() == categoryId)
                .toList();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Qanday xonadon qidiryapsiz\uD83E\uDD14" :
                "Какую квартиру вы ищете\uD83E\uDD14");

        InlineKeyboardMarkup categoryMenuForUser = KeyboardUtil.getCategoryMenuForUser(categories, language);
        sendMessage.setReplyMarkup(categoryMenuForUser);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void setting() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Sozlamalar bo'limi." : "Раздел настроек.");

        ReplyKeyboardMarkup settingMenu = KeyboardUtil.getSettingMenu(language);
        sendMessage.setReplyMarkup(settingMenu);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }
}
