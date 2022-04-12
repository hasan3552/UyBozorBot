package com.company.service;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.ProductStatus;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.Product;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Getter
@Setter
public class AdminService extends Thread {

    private Message message;
    private User user;
    private Language language;

    public AdminService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        if (message.getText().equals("/start")) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("<b>ASSALOMU ALEYKUM " + user.getFullName() + "</b>");
            sendMessage.setParseMode(ParseMode.HTML);

            ReplyKeyboardMarkup adminMenu = KeyboardUtil.getAdminMenu(language);
            sendMessage.setReplyMarkup(adminMenu);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (message.getText().equals(DemoUtil.SETTING_UZ) || message.getText().equals(DemoUtil.SETTING_RU)) {

            UserService userService = new UserService(message, user);
            userService.setting();
        } else if (message.getText().equals(DemoUtil.GIVE_REKLAMA_UZ) ||
                message.getText().equals(DemoUtil.GIVE_REKLAMA_RU)) {

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

        }else if (message.getText().equals(DemoUtil.ADD_ADMIN_UZ) || message.getText().equals(DemoUtil.ADD_ADMIN_RU)){

            if (user.getRole().equals(Role.ADMIN)){

                SendMessage sendMessage=  new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Kechirasiz sizga buning uchun ruhsat berilmagan."
                        : "Жаль, что тебе не разрешили это сделать.");

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }else {

                addAdmin();
            }
        }else if (message.getText().equals(DemoUtil.REKLAMA_GENERAL_UZ) || message.getText().equals(DemoUtil.REKLAMA_GENERAL_RU)){

            user.setStatus(Status.PREMIUM_REKLAMA);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Reklama uchun rasm jo'nating." : "Присылайте фото для рекламы.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }

    public void addAdmin() {
        user.setStatus(Status.ADMIN_ADD_OR_REVOKE_ADMIN);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ? "Administrator oynasi." : "Окно администратора.");

        InlineKeyboardMarkup adminAddOrRevoke = KeyboardUtil.getAdminAddOrRevoke(language);
        sendMessage.setReplyMarkup(adminAddOrRevoke);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

    }

    public void sendCustomerResponse() {

        if (message.hasText()) {
            Product product1 = Database.products.stream()
                    .filter(product -> product.getStatus().equals(ProductStatus.REQUEST))
                    .findAny().get();

            product1.setStatus(ProductStatus.BREAK);

            user.setStatus(Status.ADMIN_MENU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(product1.getUserId()));
            sendMessage.setText("<b>FROM ADMIN:</b>\n\n" + message.getText());
            sendMessage.setParseMode(ParseMode.HTML);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Faqat text kiriting. Istimos boshqatdan kiriting."
                    : "Просто введите текст. Пожалуйста, введите еще раз.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }
    }

    public void confirmProduct(String data) {

        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);
        long productId = Long.parseLong(split[2]);

        Database.products.stream()
                .filter(product -> product.getId() == (long) productId)
                .findAny().get().setIsSending(true);

        Language language1 = Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get().getLanguage();


        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(userId));
        sendMessage.setText(("<b>FROM ADMIN:</b>\n\n") + (language1.equals(Language.UZ) ?
                "E'loningiz joylashtirildi." : "Ваше объявление размещено."));
        sendMessage.setParseMode(ParseMode.HTML);
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void rejectProduct(String data) {

        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);
        long productId = Long.parseLong(split[2]);

        Role role = Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get().getRole();

        if (role.equals(Role.CUSTOMER)) {

            user.setStatus(Status.ADMIN_SEND_CUSTOMER_RESPONSE);
            Database.products.stream()
                    .filter(product -> product.getId() == productId)
                    .findAny().get().setStatus(ProductStatus.REQUEST);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "Izoh qoldiring." : "Оставить комментарий.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else {

            Database.products.stream()
                    .filter(product -> product.getId() == productId)
                    .findAny().get().setStatus(ProductStatus.BREAK);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ? "E'lon bekor qilindi." : "Объявление было отменено.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
    }

    public void workAdministrator(String data) {


        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);

        User user2 = Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        user.setStatus(Status.ADMIN_MENU);
        user2.setStatus(Status.REFRESH);

        if (user2.getRole().equals(Role.CUSTOMER)) {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Admin roli berildi." : "Была дана роль администратора.");
            user2.setRole(Role.ADMIN);

        } else {

            sendMessage.setText(language.equals(Language.UZ) ?
                    "Adminlik roli olindi." : "Роль администратора взята на себя.");
            user2.setRole(Role.CUSTOMER);

        }

        SendMessage sendMessage1 = new SendMessage();
        sendMessage1.setChatId(String.valueOf(userId));
        sendMessage1.setText(user2.getLanguage().equals(Language.UZ) ?
                "Sizning botdagi rolingiz o'zgardi. Iltimos <b>\uD83D\uDD01REFRESH</b> tugmasini bosing." :
                "Ваша роль в боте изменилась. Нажмите кнопку <b>\uD83D\uDD01REFRESH</b>");
        sendMessage1.setParseMode(ParseMode.HTML);

        ReplyKeyboardMarkup refresh = KeyboardUtil.getRefresh();
        sendMessage1.setReplyMarkup(refresh);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

}
