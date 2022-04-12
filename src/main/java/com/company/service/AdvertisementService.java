package com.company.service;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.AdStatus;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.model.Advertisement;
import com.company.model.User;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class AdvertisementService extends Thread {

    public Message message;
    public User user;
    public Language language;

    public AdvertisementService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        Advertisement advertisement1 = Database.advertisements.stream()
                .filter(advertisement -> !advertisement.getIsSending() &&
                        advertisement.getStatus().equals(AdStatus.READY))
                .findAny().get();

        List<User> users = Database.customers.stream()
                .filter(user1 -> user1.getRole().equals(Role.CUSTOMER))
                .toList();


        for (User customer : users) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(customer.getId()));

            InputFile inputFile = new InputFile(advertisement1.getPhoto());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(advertisement1.getBody());

            InlineKeyboardMarkup reklama =
                    KeyboardUtil.getReklama(advertisement1.getInlineName(), advertisement1.getInlineUrl());
            sendPhoto.setReplyMarkup(reklama);

            Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

        }


    }

    public void create() {

        Advertisement advertisement1 = Database.advertisements.stream()
                .filter(advertisement -> !advertisement.getStatus().equals(AdStatus.READY))
                .findAny().get();

        if (advertisement1.getStatus().equals(AdStatus.NEW) && message.hasPhoto()) {

            List<PhotoSize> photo = message.getPhoto();
            String fileId = photo.get(photo.size() - 1).getFileId();
            advertisement1.setPhoto(fileId);
            advertisement1.setStatus(AdStatus.HAS_PHOTO);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Relkama matnini kiriting." : "Введите текст объявления.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_PHOTO) && message.hasText()) {

            advertisement1.setBody(message.getText());
            advertisement1.setStatus(AdStatus.HAS_CAPTION);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Inline keyboard uchun nom qo'ying." : "Назовите его для встроенной клавиатуры.");
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_CAPTION) && message.hasText()) {

            advertisement1.setStatus(AdStatus.HAS_INLINE_NAME);
            advertisement1.setInlineName(message.getText());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Inline keybordga url yuboring." : "Отправьте URL на встроенную клавиатуру.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_INLINE_NAME)) {

            if (message.getText().startsWith("http")) {
                advertisement1.setStatus(AdStatus.READY);
                advertisement1.setInlineUrl(message.getText());

                SendPhoto sendPhoto=  new SendPhoto();
                sendPhoto.setChatId(String.valueOf(user.getId()));

                sendPhoto.setCaption(advertisement1.getBody());

                InputFile inputFile = new InputFile(advertisement1.getPhoto());
                sendPhoto.setPhoto(inputFile);

                InlineKeyboardMarkup request = KeyboardUtil.getRequestAdvertisementFromAdmin(
                        advertisement1.getInlineName(), advertisement1.getInlineUrl());
                sendPhoto.setReplyMarkup(request);

                Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
            } else {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));

                sendMessage.setText(language.equals(Language.UZ) ?
                        "URL kiritishda xatolik iltimos boshqatdan urinib ko'ring." :
                        "Ошибка при вводе URL, попробуйте еще раз.");
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }

    }
}
