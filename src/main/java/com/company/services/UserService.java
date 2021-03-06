package com.company.services;

import com.company.App;
import com.company.database.Database;
import com.company.database.DbConnection;
import com.company.enumm.Language;
import com.company.enumm.Role;
import com.company.enumm.Status;
import com.company.models.*;
import com.company.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserService extends Thread {

    public Message message;
    public User user;
    public Language language;

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
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            user.setPhoneNumber(message.getContact().getPhoneNumber());
            DbConnection.setUserPhoneNumber(user.getId(), user.getPhoneNumber());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("Change language");

            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (user.getStatus().equals(Status.GIVE_FULL_NAME) && message.hasText()) {

            user.setStatus(Status.MENU);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            user.setRole(Role.CUSTOMER);
            DbConnection.setRoleUser(user.getId(), user.getRole());

            user.setFullName(message.getText());
            DbConnection.setUserFullName(user.getId(), user.getFullName());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "<b>Assalomu aleykum " + user.getFullName() + "</b>\nOnline uy bozor botiga xush kelipsiz\uD83D\uDE0A" :
                    "<b> ???????????????????????? " + user.getFullName() + "</b>\n?????????? ???????????????????? ?? ?????? ?????? ?????????????????? ????????????-??????????\uD83D\uDE0A");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
            sendMessage.setReplyMarkup(menu);
            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void showCategoryToUser(Integer categoryId) {

        List<Category> categories = Database.categories.stream()
                .filter(category -> category.getCategoryId() == (long) categoryId)
                .collect(Collectors.toList());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Qanday xonadon qidiryapsiz\uD83E\uDD14" :
                "?????????? ???????????????? ???? ??????????\uD83E\uDD14");

        InlineKeyboardMarkup categoryMenuForUser = KeyboardUtil.getCategoryMenuForUser(categories, language);
        sendMessage.setReplyMarkup(categoryMenuForUser);

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
    }

    public void setting() {
        user.setStatus(Status.USER_SETTING_MENU);
        DbConnection.setStatusUser(user.getId(), user.getStatus());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Sozlamalar bo'limi." : "???????????? ????????????????.");

        ReplyKeyboardMarkup settingMenu = KeyboardUtil.getSettingMenu(language);
        sendMessage.setReplyMarkup(settingMenu);

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public List<Product> myLikedProduct() {

        List<Liked> likeds = Database.likeds.stream()
                .filter(liked -> liked.getUserId() == (long) user.getId())
                .filter(liked -> !liked.getIsDeleted())
                .collect(Collectors.toList());

        List<Product> productList = new ArrayList<>();
        for (Product product : Database.products) {
            for (Liked liked : likeds) {

                if (!product.getIsDeleted() && product.getIsSending() &&
                        liked.getProductId() == (long) product.getId()) {
                    productList.add(product);
                }
            }
        }

        return productList;
    }

    public void showUserLiked() {

        List<Product> products = myLikedProduct();

        if (!products.isEmpty()) {
            user.setStatus(Status.USER_SHOW_LIKED);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(user.getId()));
            sendPhoto.setCaption(products.get(0).getText());

            InputFile inputFile = new InputFile(products.get(0).getFileId());
            sendPhoto.setPhoto(inputFile);

            InlineKeyboardMarkup productLiked =
                    KeyboardUtil.getProductLiked(0, language, 0, products.size());

            sendPhoto.setReplyMarkup(productLiked);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Sizning saralangan productlar ro'yhati bo'sh" : "?????? ???????????? ?????????????????? ?????????????????? ????????");

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }
    }


    public void workLikedButton(String data) {

        if (data.startsWith("Lright/")) {

            List<Product> products = myLikedProduct();

            String[] split = data.split("/");
            int step = Integer.parseInt(split[1]);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(user.getId()));
            sendPhoto.setCaption(products.get(step + 1).getText());

            InlineKeyboardMarkup productLiked =
                    KeyboardUtil.getProductLiked(step + 1, language, step + 1, products.size());
            sendPhoto.setReplyMarkup(productLiked);

            InputFile inputFile = new InputFile(products.get(step + 1).getFileId());
            sendPhoto.setPhoto(inputFile);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("Lleft/")) {

            UserService userService = new UserService(message, user);
            List<Product> products = userService.myLikedProduct();

            String[] split = data.split("/");
            int step = Integer.parseInt(split[1]);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(user.getId()));
            sendPhoto.setCaption(products.get(step - 1).getText());

            InlineKeyboardMarkup productLiked =
                    KeyboardUtil.getProductLiked(step - 1, language, step - 1, products.size());
            sendPhoto.setReplyMarkup(productLiked);

            InputFile inputFile = new InputFile(products.get(step - 1).getFileId());
            sendPhoto.setPhoto(inputFile);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("D/")) {

            System.out.println(data);
            String[] split = data.split("/");
            int productId = Integer.parseInt(split[1]);

            UserService userService = new UserService(message, user);
            List<Product> products1 = userService.myLikedProduct();
            Long id = products1.get(productId).getId();

            Liked liked1 = Database.likeds.stream()
                    .filter(liked -> liked.getUserId() == (long) user.getId() &&
                            liked.getProductId() == (long) id && !liked.getIsDeleted())
                    .findAny().get();

            liked1.setIsDeleted(true);
            DbConnection.setLikedPotho(liked1.getId(),liked1.getIsDeleted());

            if (products1.size() > 1) {

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(user.getId()));

                InlineKeyboardMarkup productLiked =
                        KeyboardUtil.getProductLiked(0, language, 0, products1.size() - 1);
                sendPhoto.setReplyMarkup(productLiked);

                List<Product> products2 = userService.myLikedProduct();

                sendPhoto.setCaption(products2.get(0).getText());
                InputFile inputFile = new InputFile(products2.get(0).getFileId());
                sendPhoto.setPhoto(inputFile);

                App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(message.getChatId()));
                deleteMessage.setMessageId(message.getMessageId());
                App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
            } else {
                user.setStatus(Status.MENU);
                DbConnection.setStatusUser(user.getId(), user.getStatus());

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Sizning saralangan productlar listingiz do'sh." :
                        "?????? ???????????? ?????????????????? ?????????????????? ????????????????????.");
                App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(message.getChatId()));
                deleteMessage.setMessageId(message.getMessageId());
                App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            }
        }
    }

    public void sendProductPhoto() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (message.hasPhoto()) {

            user.setStatus(Status.USER_SEND_PRODUCT_CONTACT);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            Product product1 = Database.products.stream()
                    .filter(product -> product.getUserId() == (long) user.getId() &&
                            !product.getIsSending() && product.getFileId() == null)
                    .findAny().get();


            List<PhotoSize> photo = message.getPhoto();
            String fileId = photo.get(photo.size() - 1).getFileId();

            product1.setFileId(fileId);
            DbConnection.setProductPotho(product1.getId(),product1.getFileId());

            sendMessage.setText(language.equals(Language.UZ) ? "Iltimos mijozlar bo'g'lanishi uchun telefon raqam" +
                    " jo'nating. Telefon raqam o'zbekiston hududida ishlashi shart." : "????????????????????, ???????????????? ?????????? " +
                    "???????????????? ?????? ?????????? ?? ????????????????. ???????????????????? ?????????? ???????????? ???????????????? ?? ??????????????????????.");

        } else {
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Iltimos faqat rasm uzating." : "????????????????????, ???????????????? ?????? ???????????? ????????????????????.");

        }

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void sendProductContact() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (message.hasContact()) {
            user.setStatus(Status.USER_SEND_PRODUCT_LOCATION);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            Product product1 = Database.products.stream()
                    .filter(product -> !product.getIsSending() && product.getUserId() == (long) user.getId() &&
                            product.getContactProduct() == null)
                    .findAny().get();

            product1.setContactProduct(message.getContact().getPhoneNumber());
            DbConnection.setProductPhoneNumber(product1.getId(),product1.getContactProduct());

            sendMessage.setText(language.equals(Language.UZ) ? "Ko'chmas mulkning joylashuvini jo'nating." :
                    "???????????????? ?????????????????????????????? ??????????????.");


        } else if (Pattern.matches("[+]998[0-9]{9}", message.getText()) ||
                Pattern.matches("[0-9]{9}", message.getText())) {
            user.setStatus(Status.USER_SEND_PRODUCT_LOCATION);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            Product product1 = Database.products.stream()
                    .filter(product -> !product.getIsSending() && product.getUserId() == (long) user.getId() &&
                            product.getContactProduct() == null)
                    .findAny().get();

            product1.setContactProduct(message.getText().contains("+") ? message.getText().substring(1) :
                    "998" + message.getText());
            DbConnection.setProductPhoneNumber(product1.getId(),product1.getContactProduct());

            sendMessage.setText(language.equals(Language.UZ) ? "Ko'chmas mulkning joylashuvini jo'nating." :
                    "???????????????? ?????????????????????????????? ??????????????.");

        } else {
            sendMessage.setText(language.equals(Language.UZ) ? "Raqam noto'g'ri kiritildi. Iltimos boshqatdan " +
                    "urinib ko'ring." : "?????????? ???????????? ??????????????????????. ????????????????????, ???????????????????? ?????? ??????.");

        }
        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public void sendProductLocation() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (message.hasLocation()) {
            user.setStatus(Status.USER_SEND_PRODUCT_INFO);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            Product product1 = Database.products.stream()
                    .filter(product -> !product.getIsSending() && product.getUserId() == (long) user.getId() &&
                            product.getLocationId() == null)
                    .findAny().get();

            org.telegram.telegrambots.meta.api.objects.Location location1 = message.getLocation();
            Location location = new Location(location1.getLongitude(), location1.getLatitude());
            Database.locations.add(location);
            DbConnection.addLocatsion(location);

            product1.setLocationId(location.getId());
            DbConnection.setProductLocation(product1.getId(), location.getId());

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Ko'chmas mulk haqida to'liq ma'lumot kiriting. E'loningiz uchun eng muhim parametr hisoblanadi."
                    : "?????????????? ???????????? ???????????????????? ???? ??????????????. ?????? ?????????? ???????????? ???????????????? ?????? ???????????? ????????????????????.");

        } else {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Faqat joylashuv uzating." : "???????????? ?????????? ????????????????.");

        }

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void sendProductInfo() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        if (message.hasText()) {
            if (user.getRole().equals(Role.CUSTOMER)) {

                user.setStatus(Status.MENU);
                DbConnection.setStatusUser(user.getId(), user.getStatus());

            } else {

                user.setStatus(Status.ADMIN_MENU);
                DbConnection.setStatusUser(user.getId(), user.getStatus());

            }

            Product product1 = Database.products.stream()
                    .filter(product -> !product.getIsSending() && product.getUserId() == (long) user.getId() &&
                            product.getText() == null)
                    .findAny().get();

            product1.setText(message.getText());
            DbConnection.setProductText(product1.getId(), product1.getText());

            sendMessage.setText(language.equals(Language.UZ) ?
                    "E'loningiz qabul qilindi. Adminlarimiz ko'rib chiqib 1-3 kun ichida e'lon joylashtiriladi." :
                    "???????? ???????????????????? ??????????????. ???????? ???????????????????????????? ???????????????????? ?? ?????????????????? ???????????????????? ?? ?????????????? 1-3 ????????.");

            List<User> admins = Database.customers.stream()
                    .filter(user1 -> user1.getRole().equals(Role.SUPER_ADMIN))
                    .collect(Collectors.toList());

            for (User admin : admins) {
                SendLocation sendLocation = new SendLocation();

                Location location1 = Database.locations.stream()
                        .filter(location -> location.getId() == (long) product1.getLocationId())
                        .findAny().get();
                sendLocation.setLongitude(location1.getLang());
                sendLocation.setLatitude(location1.getLate());
                sendLocation.setChatId(String.valueOf(admin.getId()));

                App.MY_TELEGRAM_BOT.sendMsg(sendLocation);

                SendContact sendContact = new SendContact(String.valueOf(admin.getId()),
                        product1.getContactProduct(), admin.getLanguage().equals(Language.UZ) ?
                        "E'lon uchun contact." : "?????????????? ???? ????????????????????.");

                App.MY_TELEGRAM_BOT.sendMsg(sendContact);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(admin.getId()));
                sendPhoto.setCaption(product1.getText());

                InlineKeyboardMarkup markup = KeyboardUtil.getSendAdminProductRequest(user.getId(), product1.getId());
                sendPhoto.setReplyMarkup(markup);

                InputFile inputFile = new InputFile(product1.getFileId());
                sendPhoto.setPhoto(inputFile);

                App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
            }

        } else {
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Faqat matn kiritishingiz mumkin." : "???? ???????????? ?????????????? ???????????? ??????????.");
        }
        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void giveReklama(String data) {

        if (data.startsWith("ct/")) {

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);

            List<Category> categories = Database.categories.stream()
                    .filter(category -> category.getCategoryId() == categoryId && !category.getIsDeleted())
                    .collect(Collectors.toList());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "E'lon categegoriyasini tanlang." : "???????????????? ?????????????????? ????????????????????.");

            InlineKeyboardMarkup categoryMenuForUser = KeyboardUtil.getCategoryMenuForUser(categories, language);
            sendMessage.setReplyMarkup(categoryMenuForUser);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);


            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("C/")) {

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);

            Product product = new Product(user.getId(), categoryId);
            Database.products.add(product);
            DbConnection.addProduct(product);

            user.setStatus(Status.USER_SEND_PRODUCT_PHOTO);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Ko'chmas mulkning suratini kiriting." : "?????????????? ???????????????????? ??????????????.");

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }
    }

    public void showOwnProduct(String data) {

        if (data.startsWith("My/")) {

            String[] split = data.split("/");

            long userId = Long.parseLong(split[1]);
            long productId = Long.parseLong(split[2]);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(userId));

            List<Product> products = Database.products.stream()
                    .filter(product -> product.getUserId() == (long) user.getId() && product.getIsSending() &&
                            !product.getIsDeleted()).collect(Collectors.toList());

            Product product = products.get((int) productId);

            InputFile inputFile = new InputFile(product.getFileId());
            sendPhoto.setPhoto(inputFile);

            InlineKeyboardMarkup showMyProduct =
                    KeyboardUtil.getShowMyProduct(userId, language, (int) productId, products);
            sendPhoto.setCaption(product.getText());
            sendPhoto.setReplyMarkup(showMyProduct);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("MyD/")) {

            String[] split = data.split("/");
            long userId = Long.parseLong(split[1]);
            long productId = Long.parseLong(split[2]);

            Product product1 = Database.products.stream()
                    .filter(product -> product.getId() == productId)
                    .findAny().get();
            product1.setIsDeleted(true);
            DbConnection.setProductDeleted(product1.getId(), product1.getIsDeleted());

            List<Product> products = Database.products.stream()
                    .filter(product -> product.getUserId() == (long) user.getId() && product.getIsSending() &&
                            !product.getIsDeleted()).collect(Collectors.toList());

            Product product = products.get(0);


            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(userId));

            InputFile inputFile = new InputFile(product.getFileId());
            sendPhoto.setPhoto(inputFile);

            InlineKeyboardMarkup showMyProduct =
                    KeyboardUtil.getShowMyProduct(userId, language, 0, products);
            sendPhoto.setCaption(product.getText());
            sendPhoto.setReplyMarkup(showMyProduct);

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }

    }

    public void refresh() {

        if (user.getRole().equals(Role.ADMIN)) {
            user.setStatus(Status.ADMIN_MENU);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("<b>ASSALOMU ALEYKUM " + user.getFullName() + "</b>");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(language);
            sendMessage.setReplyMarkup(adminMenu);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else {
            user.setStatus(Status.MENU);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("<b>ASSALOMU ALEYKUM " + user.getFullName() + "</b>");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
            sendMessage.setReplyMarkup(menu);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void myReklama() {

        user.setStatus(Status.USER_SHOW_OWN_PRODUCT);
        DbConnection.setStatusUser(user.getId(), user.getStatus());

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(user.getId()));

        List<Product> products = Database.products.stream()
                .filter(product -> product.getUserId() == (long) user.getId() && product.getIsSending() &&
                        !product.getIsDeleted()).collect(Collectors.toList());

        if (!products.isEmpty()) {

            InputFile inputFile = new InputFile(products.get(0).getFileId());
            sendPhoto.setPhoto(inputFile);

            InlineKeyboardMarkup showMyProduct =
                    KeyboardUtil.getShowMyProduct(user.getId(), language, 0, products);
            sendPhoto.setReplyMarkup(showMyProduct);
            sendPhoto.setCaption(products.get(0).getText());

            App.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }else {
            user.setStatus(Status.MENU);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Sizning elonlar listingiz bo'sh. " : "?????? ???????????? ???????????????????? ????????.");

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }
    public void blockedUser() {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Murojaatingiz yuborildi. Tez orada javob qaytaramiz." :
                "?????? ???????????? ?????? ??????????????????. ???? ?????????????? ?? ?????????????????? ??????????.");
        sendMessage.setReplyMarkup(KeyboardUtil.getContactAdmin());

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        User user1 = Database.customers.stream()
                .filter(user2 -> user2.getRole().equals(Role.SUPER_ADMIN))
                .findAny().get();


        SendMessage sendMessage1 = new SendMessage();
        sendMessage1.setChatId(String.valueOf(user1.getId()));
        sendMessage1.setText("<b>FROM BLOCKED CUSTOMER :" + user.getId() + "</b>\n\n" + message.getText());
        sendMessage1.setParseMode(ParseMode.HTML);

        InlineKeyboardMarkup blockedUserResponse = KeyboardUtil.getBlockedUserResponse(user.getId());
        sendMessage1.setReplyMarkup(blockedUserResponse);

        App.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

    }
}
