package com.company.service;

import com.company.Main;
import com.company.db.Database;
import com.company.enums.*;
import com.company.model.Advertisement;
import com.company.model.Category;
import com.company.model.Product;
import com.company.model.User;
import com.company.util.DemoUtil;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

public class AdminService extends Thread {

    public Message message;
    public User user;
    public Language language;

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

        } else if (message.getText().equals(DemoUtil.ADD_ADMIN_UZ) || message.getText().equals(DemoUtil.ADD_ADMIN_RU)) {

            if (user.getRole().equals(Role.ADMIN)) {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Kechirasiz sizga buning uchun ruhsat berilmagan."
                        : "Жаль, что тебе не разрешили это сделать.");

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            } else {

                addAdmin();
            }
        } else if (message.getText().equals(DemoUtil.REKLAMA_GENERAL_UZ) || message.getText().equals(DemoUtil.REKLAMA_GENERAL_RU)) {

            user.setStatus(Status.PREMIUM_REKLAMA);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Reklama uchun rasm jo'nating." : "Присылайте фото для рекламы.");

            Advertisement advertisement = new Advertisement();
            Database.advertisements.add(advertisement);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (message.getText().equals(DemoUtil.CATEGORY_CRUD_UZ) || message.getText().equals(DemoUtil.CATEGORY_CRUD_RU)) {

            CategoryService categoryService = new CategoryService(message, user);
            categoryService.crud();

        } else if ((message.getText().equals(DemoUtil.CUSTOMER_CRUD_RU) || message.getText().equals(DemoUtil.CUSTOMER_CRUD_UZ))) {

            if (user.getRole().equals(Role.SUPER_ADMIN)) {

                user.setStatus(Status.ADMIN_CUSTOMER_CRUD);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Mijozni tanlang." : "Выберите клиента.");
                sendMessage.setReplyMarkup(KeyboardUtil.getAllCustomers(language));

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            } else {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ? "Kechirasiz sizga buning uchun ruhsat berilmagan."
                        : "Жаль, что тебе не разрешили это сделать.");

                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
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

        } else {

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

    public void operationCrudCustomer(String data) {

        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);

        User user2 = Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        sendMessage.setText(user.getLanguage().equals(Language.UZ) ?
                " TANLANG " : " ВЫБЕРИТЕ ");
        sendMessage.setParseMode(ParseMode.HTML);

        InlineKeyboardMarkup customerForCrudOperation =
                KeyboardUtil.getCustomerMenuForCrudOperation(user2, language);
        sendMessage.setReplyMarkup(customerForCrudOperation);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void customerBlockedOrUnblocked(String data) {

        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);

        User user2 = Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get();

        SendMessage sendMessage1 = new SendMessage();
        sendMessage1.setChatId(String.valueOf(user.getId()));
        sendMessage1.setText(language.equals(Language.UZ) ? user2.getIsBlocked() ?
                " Foydalanuvchi blokdan yechildi." : "Foydalanuvchi bloklandi." : user2.getIsBlocked() ?
                " Пользователь разблокирован." : " Пользователь заблокирован.");
        user.setStatus(Status.ADMIN_MENU);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage1);

        if (user2.getIsBlocked()) {
            user2.setIsBlocked(false);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(userId));
            sendMessage.setText(user2.getLanguage().equals(Language.UZ) ?
                    "Siz blokdan ochildingiz." : "Вы разблокировали блокировку.");

            if (user2.getRole().equals(Role.CUSTOMER)) {
                sendMessage.setReplyMarkup(KeyboardUtil.getMenu(user2.getLanguage()));
                user2.setStatus(Status.MENU);
            } else {
                sendMessage.setReplyMarkup(KeyboardUtil.getAdminMenu(user2.getLanguage()));
                user2.setStatus(Status.ADMIN_MENU);
            }

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else {
            user2.setIsBlocked(true);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(userId));
            sendMessage.setText(user2.getLanguage().equals(Language.UZ) ?
                    "Hurmatli foydalanuvchi siz bloklandingiz. Agar admin bilan bog'lanmoqchi bo'lsangiz" +
                            "✏️ REQUEST tugmasini bosing." : "Уважаемый пользователь, вы заблокированы. " +
                    "Если вы хотите связаться с администратором, нажмите ✏️ REQUEST.");

            ReplyKeyboardMarkup contactAdmin = KeyboardUtil.getContactAdmin();
            sendMessage.setReplyMarkup(contactAdmin);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void adminResponseBlokedUser(String data) {

        String[] split = data.split("/");
        long userId = Long.parseLong(split[1]);

        Database.customers.stream()
                .filter(user1 -> user1.getId() == userId)
                .findAny().get().setStatus(Status.WAIT_RESPONSE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText("<b>RESPONSE: </b>");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        user.setStatus(Status.ADMIN_WRITE_RESPONSE);

        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void createCategory() {

        Optional<Category> optional = Database.categories.stream()
                .filter(category -> category.getStatus().equals(CategoryStatus.NEW))
                .findAny();

        if (optional.isPresent()) {

            Category category = optional.get();
            category.setNameUz(message.getText());
            category.setStatus(CategoryStatus.HAS_NAME_UZ);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Categoriyaning nomi(ruscha) : " : "Название категории (рус.) : ");
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else {
            Optional<Category> optional1 = Database.categories.stream()
                    .filter(category -> category.getStatus().equals(CategoryStatus.HAS_NAME_UZ))
                    .findAny();

            if (optional1.isPresent()) {

                Category category = optional1.get();
                category.setNameRu(message.getText());
                category.setStatus(CategoryStatus.HAS_NAME_RU);
                category.setIsDeleted(false);

                user.setStatus(Status.ADMIN_MENU);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(user.getId()));
                sendMessage.setText(language.equals(Language.UZ) ?
                        "Yangi categoriya saqlandi. " : "Новая категория сохранена. ");
                Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }

    }

    public void createCategory(String data) {

        String[] split = data.split("/");
        int categoryId = Integer.parseInt(split[1]);

        Category category = new Category(categoryId);
        Database.categories.add(category);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setText(language.equals(Language.UZ) ?
                "Categoriyaning nomi(o'zbek) : " : "Название категории (узбекский)");
        Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }

    public void updatecategory(String data) {


        if (data.startsWith("ct/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.updatecategory(data);

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);

            List<Category> categories = Database.categories.stream()
                    .filter(category -> category.getCategoryId() == categoryId)
                    .toList();

            InlineKeyboardMarkup categoryMenuForUser = KeyboardUtil.getCategoryMenuForUser(categories, language);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Qaysi kategoriyani o'zgartirmoqchisiz." : "Какую категорию вы хотите изменить.");
            sendMessage.setReplyMarkup(categoryMenuForUser);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("C/")) {

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);
            Category category1 = Database.categories.stream()
                    .filter(category -> category.getId() == categoryId)
                    .findAny().get();

            category1.setIsDeleted(true);
            category1.setStatus(CategoryStatus.NEW);
            user.setStatus(Status.ADMIN_CREATE_CATEGORY);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Categoriyaning nomi(o'zbek) : " : "Название категории (узбекский)");
            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }
    }

    public void deletedCategory(String data) {

        if (data.startsWith("ct/")) {

            AdminService adminService = new AdminService(message, user);
            adminService.deletedCategory(data);

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);

            List<Category> categories = Database.categories.stream()
                    .filter(category -> category.getCategoryId() == categoryId)
                    .toList();

            InlineKeyboardMarkup categoryMenuForUser = KeyboardUtil.getCategoryMenuForUser(categories, language);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Qaysi kategoriyani o'chirmoqchisiz." : "Какую категорию вы хотите удалить?");
            sendMessage.setReplyMarkup(categoryMenuForUser);

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        } else if (data.startsWith("C/")) {

            String[] split = data.split("/");
            int categoryId = Integer.parseInt(split[1]);

            Category category1 = Database.categories.stream()
                    .filter(category -> category.getId() == categoryId)
                    .findAny().get();

            category1.setIsDeleted(true);
            user.setStatus(Status.ADMIN_MENU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(language.equals(Language.UZ) ?
                    "Kategoriya o'chirildi." : "Категория удалена.");

            Main.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }
    }

    public void customerProduct(String data) {

        String[] split = data.split("/");
        long customerId = Long.parseLong(split[1]);
        int step = Integer.parseInt(split[2]);

        List<Product> products = Database.products.stream()
                .filter(product -> product.getUserId() == customerId)
                .toList();

        Product product = products.get(step);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(user.getId()));

        InputFile inputFile = new InputFile(product.getFileId());
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(product.getText());

        InlineKeyboardMarkup userProducts = KeyboardUtil.getUserProducts(step, language, customerId, products.size());
        sendPhoto.setReplyMarkup(userProducts);

        Main.MY_TELEGRAM_BOT.sendMsg(sendPhoto);


        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        Main.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

    }
}
