package com.company.controller;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.User;
import com.company.service.CategoryService;
import com.company.service.SettingService;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class UserController extends Thread {

    public Message message;
    public User user;
    public Language language;

    public UserController(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        System.out.println("user.getStatus() = " + user.getStatus());
        if (user.getStatus().equals(Status.MENU) &&
                (message.getText().equals(DemoUtil.KVARTIRA_UZ) || message.getText().equals(DemoUtil.KVARTIRA_RU) ||
                        message.getText().equals(DemoUtil.FIELD_YARD_UZ) || message.getText().equals(DemoUtil.FIELD_YARD_RU) ||
                        message.getText().equals(DemoUtil.HOUSE_UZ) || message.getText().equals(DemoUtil.HOUSE_RU) ||
                        message.getText().equals(DemoUtil.GROUND_UZ) || message.getText().equals(DemoUtil.GROUND_RU))) {

            user.setStatus(Status.USER_SHOW_CATEGORY);

            Integer categoryId =
                    (message.getText().equals(DemoUtil.KVARTIRA_UZ) || message.getText().equals(DemoUtil.KVARTIRA_RU)) ? 1 :
                            (message.getText().equals(DemoUtil.FIELD_YARD_UZ) || message.getText().equals(DemoUtil.FIELD_YARD_RU)) ? 4 :
                                    (message.getText().equals(DemoUtil.HOUSE_UZ) || message.getText().equals(DemoUtil.HOUSE_RU)) ? 2 : 3;

            UserService userService = new UserService(message, user);
            userService.showCategoryToUser(categoryId);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (user.getStatus().equals(Status.REFRESH)) {

            UserService userService = new UserService(message, user);
            userService.refresh();

        } else if (user.getStatus().equals(Status.MENU) && (message.getText().equals(DemoUtil.SETTING_UZ) || message.getText().equals(DemoUtil.SETTING_RU))) {

            UserService userService = new UserService(message, user);
            userService.setting();

        } else if (user.getStatus().equals(Status.MENU) && (message.getText().equals(DemoUtil.LIKED_UZ) || message.getText().equals(DemoUtil.LIKED_RU))) {
            UserService userService = new UserService(message, user);
            userService.showUserLiked();
        } else if (user.getStatus().equals(Status.USER_SETTING_MENU)) {

            SettingService settingService = new SettingService(message, user);
            settingService.start();

        } else if (user.getStatus().equals(Status.SET_NEW_FULLNAME)) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewName();

        } else if (user.getStatus().equals(Status.SET_NEW_CONTACT)) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewContact();

        } else if (user.getStatus().equals(Status.MENU) && (message.getText().equals(DemoUtil.GIVE_REKLAMA_UZ) || message.getText().equals(DemoUtil.GIVE_REKLAMA_RU))) {

            user.setStatus(Status.USER_GIVE_REKLAMA);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "E'lon kategoriyasini tanlang." : "Выберите категорию объявления.");
            InlineKeyboardMarkup mainCategory = KeyboardUtil.getMainCategory(language);
            sendMessage.setReplyMarkup(mainCategory);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

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

        } else if (user.getStatus().equals(Status.MENU) && (message.getText().equals(DemoUtil.MY_REKLAMA_UZ) || message.getText().equals(DemoUtil.MY_REKLAMA_RU))) {

            UserService userService = new UserService(message, user);
            userService.myReklama();

        }else if (!(user.getStatus().equals(Status.BLOCKED_USER)) &&
                user.getIsBlocked() && (message.getText().equals("✏️ REQUEST"))){

            user.setStatus(Status.BLOCKED_USER);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "<b>XABARINGIZNI QOLDIRING.</b>" : "<b> ОСТАВЬТЕ СВОЕ СООБЩЕНИЕ. </b>");
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(Status.BLOCKED_USER)){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Murojaatingiz yuborildi. Tez orada javob qaytaramiz." :
                    "Ваш запрос был отправлен. Мы ответим в ближайшее время.");
            sendMessage.setReplyMarkup(KeyboardUtil.getContactAdmin());

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            User user1 = Database.customers.stream()
                    .filter(user2 -> user2.getRole().equals(Role.SUPER_ADMIN))
                    .findAny().get();


            SendMessage sendMessage1=  new SendMessage();
            sendMessage1.setChatId(String.valueOf(user1.getId()));
            sendMessage1.setText("<b>FROM BLOCKED CUSTOMER :"+user.getId()+"</b>\n\n"+message.getText());
            sendMessage1.setParseMode(ParseMode.HTML);

            InlineKeyboardMarkup blockedUserResponse = KeyboardUtil.getBlockedUserResponse(user.getId());
            sendMessage1.setReplyMarkup(blockedUserResponse);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

        }
    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        String data = callbackQuery.getData();

        if (data.equals(DemoUtil.BACK)) {

            user.setStatus(Status.MENU);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (user.getStatus().equals(Status.SET_LANGUAGE) &&
                (data.equals(DemoUtil.LANG_UZ) || data.equals(DemoUtil.LANG_RU))) {

            SettingService settingService = new SettingService(message, user);
            settingService.userSetNewLanguage(data);

        } else if (user.getStatus().equals(Status.USER_SHOW_CATEGORY)) {

            CategoryService categoryService = new CategoryService(message, user);
            categoryService.workCallbackQuery(data);

        } else if (user.getStatus().equals(Status.USER_SHOW_LIKED)) {

            UserService userService = new UserService(message, user);
            userService.workLikedButton(data);

        } else if (user.getStatus().equals(Status.USER_GIVE_REKLAMA)) {

            UserService userService = new UserService(message, user);
            userService.giveReklama(data);

        } else if (user.getStatus().equals(Status.USER_SHOW_OWN_PRODUCT)) {

            UserService userService = new UserService(message, user);
            userService.showOwnProduct(data);

        }
    }
}
