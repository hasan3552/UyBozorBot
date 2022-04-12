package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.ProductStatus;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.Product;
import com.company.model.User;
import com.company.service.AdminService;
import com.company.service.AdvertisementService;
import com.company.service.SettingService;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

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
        }else if (user.getStatus().equals(Status.PREMIUM_REKLAMA)){

            AdvertisementService advertisementService = new AdvertisementService(message,user);
            advertisementService.start();

        }

    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        String data = callbackQuery.getData();

        if (data.startsWith("✅/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.confirmProduct(data);

        } else if (data.equals(DemoUtil.BACK)) {

            user.setStatus(Status.ADMIN_MENU);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("❌/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.rejectProduct(data);

        } else if (user.getStatus().equals(Status.SET_LANGUAGE) &&
                (data.equals(DemoUtil.LANG_UZ) || data.equals(DemoUtil.LANG_RU))) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewLanguage(data);

        } else if (user.getStatus().equals(Status.USER_GIVE_REKLAMA)) {

            UserService userService = new UserService(message, user);
            userService.giveReklama(data);

        } else if (data.startsWith("AR/")) {

            AdminService adminService = new AdminService(message,user);
            adminService.workAdministrator(data);

        }
    }
}
