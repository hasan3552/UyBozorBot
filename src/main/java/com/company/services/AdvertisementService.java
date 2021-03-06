package com.company.services;

import com.company.App;
import com.company.database.Database;
import com.company.database.DbConnection;
import com.company.enumm.AdStatus;
import com.company.enumm.Language;
import com.company.enumm.Role;
import com.company.models.Advertisement;
import com.company.models.User;
import com.company.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());


        for (User customer : users) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(customer.getId()));

            InputFile inputFile = new InputFile(advertisement1.getPhoto());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(advertisement1.getBody());

            InlineKeyboardMarkup reklama =
                    KeyboardUtil.getReklama(advertisement1.getInlineName(), advertisement1.getInlineUrl());
            sendPhoto.setReplyMarkup(reklama);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

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
            DbConnection.setAdvertisementPhoto(advertisement1);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Relkama matnini kiriting." : "?????????????? ?????????? ????????????????????.");

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_PHOTO) && message.hasText()) {

            advertisement1.setBody(message.getText());
            advertisement1.setStatus(AdStatus.HAS_CAPTION);
            DbConnection.setAdvertisementBody(advertisement1);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Inline keyboard uchun nom qo'ying." : "???????????????? ?????? ?????? ???????????????????? ????????????????????.");
            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_CAPTION) && message.hasText()) {

            advertisement1.setStatus(AdStatus.HAS_INLINE_NAME);
            advertisement1.setInlineName(message.getText());
            DbConnection.setAdvertisementInlineName(advertisement1);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Inline keybordga url yuboring." : "?????????????????? URL ???? ???????????????????? ????????????????????.");

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (advertisement1.getStatus().equals(AdStatus.HAS_INLINE_NAME)) {

            if (message.getText().startsWith("http")) {

                advertisement1.setStatus(AdStatus.READY);
                advertisement1.setInlineUrl(message.getText());
                DbConnection.setAdvertisementInlineUrl(advertisement1);

                SendPhoto sendPhoto=  new SendPhoto();
                sendPhoto.setChatId(String.valueOf(user.getId()));

                sendPhoto.setCaption(advertisement1.getBody());

                InputFile inputFile = new InputFile(advertisement1.getPhoto());
                sendPhoto.setPhoto(inputFile);

                InlineKeyboardMarkup request = KeyboardUtil.getRequestAdvertisementFromAdmin(
                        advertisement1.getInlineName(), advertisement1.getInlineUrl());
                sendPhoto.setReplyMarkup(request);

                App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
            } else {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));

                sendMessage.setText(language.equals(Language.UZ) ?
                        "URL kiritishda xatolik iltimos boshqatdan urinib ko'ring." :
                        "???????????? ?????? ?????????? URL, ???????????????????? ?????? ??????.");
                App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }

    }
}
