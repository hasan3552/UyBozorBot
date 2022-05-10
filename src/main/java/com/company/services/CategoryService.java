package com.company.services;

import com.company.App;
import com.company.database.Database;
import com.company.database.DbConnection;
import com.company.enumm.Language;
import com.company.enumm.Status;
import com.company.models.*;
import com.company.utils.DemoUtil;
import com.company.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryService extends Thread {

    public Message message;
    public User user;
    public Language language;

    public CategoryService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }


    public void workCallbackQuery(String data) {

        if (data.startsWith("C/")) {

            String replace = data.replace("C/", "");
            String[] split = replace.split("/");

            int categoryId = Integer.parseInt(split[0]);
            long productId = Long.parseLong(split[1]);

            List<Product> products = Database.products.stream()
                    .filter(product -> (product.getCategoryId() == categoryId) &&
                            !product.getIsDeleted() && product.getIsSending())
                    .collect(Collectors.toList());


            if (!products.isEmpty()) {

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(user.getId()));

                InlineKeyboardMarkup productMarkup =
                        KeyboardUtil.getProduct(products, productId, categoryId, language, user);
                sendPhoto.setReplyMarkup(productMarkup);

                InputFile inputFile = new InputFile(products.get((int) productId).getFileId());
                sendPhoto.setPhoto(inputFile);
                sendPhoto.setCaption(products.get((int) productId).getText());

                App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(message.getChatId()));
                deleteMessage.setMessageId(message.getMessageId());
                App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            } else {
                user.setStatus(Status.MENU);
                DbConnection.setStatusUser(user.getId(),user.getStatus());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Bunday kategoriyali product afsuski bo'sh." :
                        "Эта категория товаров, к сожалению, пуста.");

                App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(message.getChatId()));
                deleteMessage.setMessageId(message.getMessageId());
                App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            }

        } else if (data.startsWith("Loc")) {

            SendLocation sendLocation = new SendLocation();
            sendLocation.setChatId(String.valueOf(user.getId()));

            String[] split = data.split("/");
            int productId = Integer.parseInt(split[1]);

            Product product1 = Database.products.stream()
                    .filter(product -> product.getId() == productId)
                    .findAny().get();

            Integer locationId = product1.getLocationId();

            Location location1 = Database.locations.stream()
                    .filter(location -> location.getId() == (long) locationId)
                    .findAny().get();

            sendLocation.setLatitude(location1.getLate());
            sendLocation.setLongitude(location1.getLang());

            App.MY_TELEGRAM_BOT.sendMsg(sendLocation);

        } else if (data.startsWith("Call")) {

            SendContact sendContact = new SendContact();
            sendContact.setChatId(String.valueOf(user.getId()));

            String[] split = data.split("/");
            int productId = Integer.parseInt(split[1]);

            Product product1 = Database.products.stream()
                    .filter(product -> product.getId() == productId)
                    .findAny().get();

            User user = Database.customers.stream()
                    .filter(user1 -> user1.getId() == (long) product1.getUserId())
                    .findAny().get();

            sendContact.setPhoneNumber(product1.getContactProduct());
            sendContact.setFirstName(user.getFullName());

            App.MY_TELEGRAM_BOT.sendMsg(sendContact);

        } else if (data.startsWith("Liked")) {

            String replace = data.replace("Liked", "");
            String[] split = replace.split("/");
            String productId = split[0];
            String userId = split[1];

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));

            Liked liked = new Liked((long) Database.likeds.size() + 1, Long.parseLong(userId), Long.parseLong(productId), false);

            if (Database.likeds.stream()
                    .noneMatch(liked1 -> liked1.getUserId() == (long) liked.getUserId() &&
                            liked1.getProductId() == (long) liked.getProductId() && !liked1.getIsDeleted())) {

                Database.likeds.add(liked);
                DbConnection.addLikedProduct(liked);

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Saralanganlar ro'yhatiga qo'shildi." : "Добавил в шорт-лист.");

            } else {
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Ushbu product allaqachon ro'yhatda bor" : "Этот продукт уже указан");
            }
            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void crud() {
        user.setStatus(Status.CATEGORY_CRUD);
        DbConnection.setStatusUser(user.getId(), user.getStatus());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Kategoriya CRUD menyusi." : "Категория CRUD меню");

        InlineKeyboardMarkup categoryCRUD = KeyboardUtil.getCategoryCRUD(language);
        sendMessage.setReplyMarkup(categoryCRUD);

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void crudCallback(String data) {

        if (data.equals(DemoUtil.CATEGORY_CREATE)) {

            user.setStatus(Status.ADMIN_CREATE_CATEGORY);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            InlineKeyboardMarkup mainCategory = KeyboardUtil.getMainCategory(language);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Qaysi bosh kategoriyaga qo'shmoqchisiz." : "Какую основную категорию вы хотите добавить.");
            sendMessage.setReplyMarkup(mainCategory);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals(DemoUtil.CATEGORY_SHOW)) {
            user.setStatus(Status.CATEGORY_CRUD);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            String cateUz = "";
            String cateRu = "";

            for (Category category : Database.categories) {
                if (!category.getIsDeleted()) {
                    cateUz += category.getId() + ".  " + category.getNameUz() + " - " + category.getCategoryId() + "\n";
                    cateRu += category.getId() + ".  " + category.getNameRu() + " - " + category.getCategoryId() + "\n";
                }
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    cateUz : cateRu);
            sendMessage.setReplyMarkup(KeyboardUtil.getCategoryCRUD(language));

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals(DemoUtil.CATEGORY_UPDATE)) {

            user.setStatus(Status.ADMIN_UPDATE_CATEGORY);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            InlineKeyboardMarkup mainCategory = KeyboardUtil.getMainCategory(language);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Bosh kategoriyani tanlang." : "Выберите общую категорию.");
            sendMessage.setReplyMarkup(mainCategory);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals(DemoUtil.CATEGORY_DELETED)) {

            user.setStatus(Status.ADMIN_DELETED_CATEGORY);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            InlineKeyboardMarkup mainCategory = KeyboardUtil.getMainCategory(language);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Bosh kategoriyani tanlang." : "Выберите общую категорию.");
            sendMessage.setReplyMarkup(mainCategory);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
    }
}