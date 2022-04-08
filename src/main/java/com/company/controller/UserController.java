package com.company.controller;

import com.company.Main;
import com.company.enums.Language;
import com.company.enums.Status;
import com.company.model.User;
import com.company.service.UserService;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.regex.Pattern;

@Getter
@Setter
public class UserController extends Thread {

    private Message message;
    private User user;
    private Language language;

    public UserController(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

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

        }else if (user.getStatus().equals(Status.MENU) &&
                (message.getText().equals(DemoUtil.SETTING_UZ) || message.getText().equals(DemoUtil.SETTING_RU))){
            user.setStatus(Status.USER_SETTING_MENU);

            UserService userService = new UserService(message,user);
            userService.setting();

        }else if (user.getStatus().equals(Status.USER_SETTING_MENU)
                && (message.getText().equals(DemoUtil.BACK_REPLY_UZ) || message.getText().equals(DemoUtil.BACK_REPLY_RU))){
            user.setStatus(Status.MENU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Asosiy menyu" : "Главное меню");

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(Status.USER_SETTING_MENU) &&
                (message.getText().equals(DemoUtil.SETTING_LANGUAGE_UZ) || message.getText().equals(DemoUtil.SETTING_LANGUAGE_RU))){

            user.setStatus(Status.SET_LANGUAGE);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Tilni tanlang" : "Выберите язык");

            InlineKeyboardMarkup language = KeyboardUtil.getLanguage();
            sendMessage.setReplyMarkup(language);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(Status.USER_SETTING_MENU) &&
                (message.getText().equals(DemoUtil.SETTING_FULLNAME_UZ) || message.getText().equals(DemoUtil.SETTING_FULLNAME_RU))){

           user.setStatus(Status.SET_NEW_FULLNAME);

           SendMessage sendMessage = new SendMessage();
           sendMessage.setChatId(String.valueOf(user.getId()));
           sendMessage.setText(language.equals(Language.UZ) ?
                   "Ism familiyangizni kiriting." : "Введите свое имя и фамилию.");
           sendMessage.setReplyMarkup(null);

           Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(Status.SET_NEW_FULLNAME)){

            SendMessage sendMessage=  new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));

            if (Pattern.matches("[A-Z a-z]+",message.getText())){
                user.setStatus(Status.MENU);
                user.setFullName(message.getText());

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Ism sharifingiz o'zgartirildi." : "Ваше имя было изменено.");
                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                sendMessage.setReplyMarkup(menu);

            }else {

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Ism sharif kiritilganda belgi bo'lmasligi shart.Boshqatdan kiriting." :
                        "Имя при вводе не должно быть символом Введите еще раз.");
            }

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        }else if (user.getStatus().equals(Status.USER_SETTING_MENU) &&
                (message.getText().equals(DemoUtil.SETTING_CALL_NUMBER_UZ) || message.getText().equals(DemoUtil.SETTING_CALL_NUMBER_RU))){
            user.setStatus(Status.SET_NEW_CONTACT);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Yangi raqamingizni jo'nating." : "Отправьте свой новый номер.");

            ReplyKeyboardMarkup contact = KeyboardUtil.getContact();
            sendMessage.setReplyMarkup(contact);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if (user.getStatus().equals(Status.SET_NEW_CONTACT)){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));

            if (message.hasContact()){
                user.setStatus(Status.MENU);
                user.setPhoneNumber(message.getContact().getPhoneNumber());

                sendMessage.setText(language.equals(Language.UZ) ?
                        "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");
                ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                sendMessage.setReplyMarkup(menu);

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            }else if (message.hasText()){

                String text = message.getText();
                String replace = text.replace(" ", "");
                if (Pattern.matches("[+]998[0-9]{9}",replace) ){

                    user.setStatus(Status.MENU);
                    user.setPhoneNumber(message.getText().substring(1));

                    sendMessage.setText(language.equals(Language.UZ) ?
                            "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");
                    ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                    sendMessage.setReplyMarkup(menu);

                }else if (Pattern.matches("[0-9]{9}", replace)){

                    user.setStatus(Status.MENU);
                    user.setPhoneNumber("998"+message.getText());

                    sendMessage.setText(language.equals(Language.UZ) ?
                            "Telefon raqam o'zgartirildi." : "Номер телефона изменен.");
                    ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(language);
                    sendMessage.setReplyMarkup(menu);

                }else {

                    sendMessage.setText(language.equals(Language.UZ) ?
                            "Telefon raqam noto'g'ri kiritildi." : "Номер телефона введен неверно.");
                }


                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            }

        }


    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        String data = callbackQuery.getData();

        if (data.equals(DemoUtil.BACK)){

            user.setStatus(Status.MENU);

        }else if (user.getStatus().equals(Status.SET_LANGUAGE) &&
                (data.equals(DemoUtil.LANG_UZ) || data.equals(DemoUtil.LANG_RU))){

            user.setStatus(Status.MENU);
            user.setLanguage(data.equals(DemoUtil.LANG_UZ) ? Language.UZ : Language.RU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Язык изменен." : "Til o'zgartrildi.");

            ReplyKeyboardMarkup menu = KeyboardUtil.getMenu(user.getLanguage());
            sendMessage.setReplyMarkup(menu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }

    }
}
