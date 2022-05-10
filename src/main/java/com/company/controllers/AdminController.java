package com.company.controllers;

import com.company.App;
import com.company.database.Database;
import com.company.database.DbConnection;
import com.company.enumm.Language;
import com.company.enumm.Status;
import com.company.models.User;
import com.company.services.*;
import com.company.utils.DemoUtil;
import com.company.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AdminController extends Thread {

    public Message message;
    public User user;
    public Language language;

    public AdminController(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        if (user.getStatus().equals(Status.ADMIN_SEND_CUSTOMER_RESPONSE)) {

            AdminService adminService = new AdminService(message, user);
            adminService.sendCustomerResponse();

        } else if (user.getStatus().equals(Status.REFRESH)) {

            UserService userService = new UserService(message, user);
            userService.refresh();

        } else if (user.getStatus().equals(Status.ADMIN_MENU)) {

            AdminService adminService = new AdminService(message, user);
            adminService.start();

        } else if (user.getStatus().equals(Status.USER_SETTING_MENU)) {

            SettingService settingService = new SettingService(message, user);
            settingService.start();

        } else if (user.getStatus().equals(Status.SET_NEW_FULLNAME)) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewName();

        } else if (user.getStatus().equals(Status.SET_NEW_CONTACT)) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewContact();

        } else if (user.getStatus().equals(Status.USER_SEND_PRODUCT_PHOTO)) {

            UserService userService = new UserService(message, user);
            userService.sendProductPhoto();

        } else if (user.getStatus().equals(Status.USER_SEND_PRODUCT_CONTACT)) {

            UserService userService = new UserService(message, user);
            userService.sendProductContact();

        } else if (user.getStatus().equals(Status.USER_SEND_PRODUCT_LOCATION)) {

            UserService userService = new UserService(message, user);
            userService.sendProductLocation();

        } else if (user.getStatus().equals(Status.USER_SEND_PRODUCT_INFO)) {

            UserService userService = new UserService(message, user);
            userService.sendProductInfo();

        } else if (user.getStatus().equals(Status.PREMIUM_REKLAMA)) {

            AdvertisementService advertisementService = new AdvertisementService(message, user);
            advertisementService.create();

        } else if (user.getStatus().equals(Status.ADMIN_WRITE_RESPONSE)) {

            User user2 = Database.customers.stream()
                    .filter(user1 -> user1.getStatus().equals(Status.WAIT_RESPONSE))
                    .findAny().get();

            user2.setStatus(Status.USER_SHOW_LIKED);
            user.setStatus(Status.ADMIN_MENU);

            DbConnection.setStatusUser(user2.getId(), user2.getStatus());
            DbConnection.setStatusUser(user.getId(), user.getStatus());


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user2.getId()));
            sendMessage.setText(message.getText());

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            SendMessage sendMessage1 = new SendMessage();
            sendMessage1.setChatId(String.valueOf(user.getId()));
            sendMessage1.setText(language.equals(Language.UZ) ? "Javob uzatildi." : "Ответ прошел.");
            sendMessage1.setReplyMarkup(KeyboardUtil.getAdminMenu(language));

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

        } else if (user.getStatus().equals(Status.ADMIN_CREATE_CATEGORY)) {

            AdminService adminService = new AdminService(message, user);
            adminService.createCategory();

        }
    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        String data = callbackQuery.getData();

        if (data.startsWith("✅/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.confirmProduct(data);

        } else if (data.equals(DemoUtil.BACK)) {

            user.setStatus(Status.ADMIN_MENU);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("❌/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.rejectProduct(data);

        } else if (data.startsWith("RESPONSE/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.adminResponseBlokedUser(data);

        } else if (user.getStatus().equals(Status.SET_LANGUAGE) &&
                (data.equals(DemoUtil.LANG_UZ) || data.equals(DemoUtil.LANG_RU))) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewLanguage(data);

        } else if (user.getStatus().equals(Status.USER_GIVE_REKLAMA)) {

            UserService userService = new UserService(message, user);
            userService.giveReklama(data);

        } else if (data.startsWith("AR/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.workAdministrator(data);

        } else if (data.equals(DemoUtil.CONFIRM)) {

            AdvertisementService service = new AdvertisementService(message, user);
            service.start();

        } else if ((user.getStatus().equals(Status.ADMIN_CUSTOMER_CRUD)) && data.startsWith("Cus/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.operationCrudCustomer(data);

        } else if ((user.getStatus().equals(Status.ADMIN_CUSTOMER_CRUD)) && data.startsWith("Block/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.customerBlockedOrUnblocked(data);

        } else if (user.getStatus().equals(Status.ADMIN_CUSTOMER_CRUD) && data.startsWith("CP/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.customerProduct(data);

        } else if (user.getStatus().equals(Status.CATEGORY_CRUD)) {

            CategoryService categoryService = new CategoryService(message, user);
            categoryService.crudCallback(data);

        } else if (user.getStatus().equals(Status.ADMIN_CREATE_CATEGORY)) {

            AdminService adminService = new AdminService(message, user);
            adminService.createCategory(data);


        } else if (user.getStatus().equals(Status.ADMIN_UPDATE_CATEGORY)) {

            AdminService adminService = new AdminService(message, user);
            adminService.updatecategory(data);

        } else if (user.getStatus().equals(Status.ADMIN_DELETED_CATEGORY)) {

            AdminService adminService = new AdminService(message, user);
            adminService.deletedCategory(data);
        }
    }
}
