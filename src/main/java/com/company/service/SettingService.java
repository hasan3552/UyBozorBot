package com.company.service;

import com.company.Main;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.regex.Pattern;

public class SettingService extends Thread {

    public Message message;
    public User user;
    public Language language;

    public SettingService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        if (message.getText().equals(DemoUtil.BACK_REPLY_UZ) || message.getText().equals(DemoUtil.BACK_REPLY_RU)) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Asosiy menyu" : "Главное меню");

            if (user.getRole().equals(Role.CUSTOMER)) {
                user.setStatus(Status.MENU);

                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
                sendMessage.setReplyMarkup(menu);

            } else {
                user.setStatus(Status.ADMIN_MENU);

                ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
                sendMessage.setReplyMarkup(adminMenu);

            }
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.getText().equals(DemoUtil.SETTING_LANGUAGE_UZ) ||
                message.getText().equals(DemoUtil.SETTING_LANGUAGE_RU)) {

            user.setStatus(Status.SET_LANGUAGE);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Tilni tanlang" : "Выберите язык");

            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.getText().equals(DemoUtil.SETTING_FULLNAME_UZ) ||
                message.getText().equals(DemoUtil.SETTING_FULLNAME_RU)) {

            user.setStatus(Status.SET_NEW_FULLNAME);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Ism familiyangizni kiriting." : "Введите свое имя и фамилию.");
            sendMessage.setReplyMarkup(null);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.getText().equals(DemoUtil.SETTING_CALL_NUMBER_UZ) ||
                message.getText().equals(DemoUtil.SETTING_CALL_NUMBER_RU)) {
            user.setStatus(Status.SET_NEW_CONTACT);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Yangi raqamingizni jo'nating." : "Отправьте свой новый номер.");

            ReplyKeyboardMarkup contact = KeyboardUtil.getContact();
            sendMessage.setReplyMarkup(contact);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void userSetNewContact() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (message.hasContact()) {

            user.setPhoneNumber(message.getContact().getPhoneNumber());
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");

            if (user.getRole().equals(Role.CUSTOMER)) {

                user.setStatus(Status.MENU);
                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                sendMessage.setReplyMarkup(menu);
            } else {
                user.setStatus(Status.ADMIN_MENU);

                ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
                sendMessage.setReplyMarkup(adminMenu);

            }

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.hasText()) {

            String text = message.getText();
            String replace = text.replace(" ", "");
            if (Pattern.matches("[+]998[0-9]{9}", replace)) {

                user.setPhoneNumber(message.getText().substring(1));
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");

                if (user.getRole().equals(Role.CUSTOMER)) {

                    user.setStatus(Status.MENU);
                    ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                    sendMessage.setReplyMarkup(menu);

                } else {
                    user.setStatus(Status.ADMIN_MENU);

                    ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
                    sendMessage.setReplyMarkup(adminMenu);

                }
            } else if (Pattern.matches("[0-9]{9}", replace)) {

                user.setPhoneNumber("998" + message.getText());
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");

                if (user.getRole().equals(Role.CUSTOMER)) {

                    user.setStatus(Status.MENU);
                    ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                    sendMessage.setReplyMarkup(menu);
                } else {

                    user.setStatus(Status.ADMIN_MENU);
                    ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
                    sendMessage.setReplyMarkup(adminMenu);

                }
            } else {

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Telefon raqam noto'g'ri kiritildi." : "Номер телефона введен неверно.");
            }

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

    }

    public void userSetNewName() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (Pattern.matches("[A-Z a-z]+", message.getText())) {
            user.setFullName(message.getText());
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Ism sharifingiz o'zgartirildi." : "Ваше имя было изменено.");

            if (user.getRole().equals(Role.CUSTOMER)) {

                user.setStatus(Status.MENU);
                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                sendMessage.setReplyMarkup(menu);
            } else {

                user.setStatus(Status.ADMIN_MENU);
                ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
                sendMessage.setReplyMarkup(adminMenu);
            }
        } else {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Ism sharif kiritilganda belgi bo'lmasligi shart.Boshqatdan kiriting." :
                    "Имя при вводе не должно быть символом Введите еще раз.");
        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public void userSetNewLanguage(String data) {

        user.setLanguage(data.equals(DemoUtil.LANG_UZ) ? Language.UZ : Language.RU);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(user.getLanguage().equals(Language.RU) ? "Язык изменен." : "Til o'zgartrildi.");

        if (user.getRole().equals(Role.CUSTOMER)) {

            user.setStatus(Status.MENU);
            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);
        }else {

            user.setStatus(Status.ADMIN_MENU);
            ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(user.getLanguage());
            sendMessage.setReplyMarkup(adminMenu);

        }

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }
}
