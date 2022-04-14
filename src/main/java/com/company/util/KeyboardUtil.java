package com.company.util;

import com.company.db.Database;
import com.company.enums.Language;
import com.company.enums.Role;
import com.company.model.Category;
import com.company.model.Product;
import com.company.model.User;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KeyboardUtil {


    public static ReplyKeyboardMarkup getContact() {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("\uD83D\uDCDE Contact");

        row.add(button);
        rowList.add(row);

        markup.setKeyboard(rowList);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    public static InlineKeyboardMarkup getLanguage() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("\uD83C\uDDFA\uD83C\uDDFF UZBEK");
        button.setCallbackData(DemoUtil.LANG_UZ);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("\uD83C\uDDF7\uD83C\uDDFA RUSSIAN");
        button1.setCallbackData(DemoUtil.LANG_RU);
        row1.add(button1);
        rowList.add(row1);


        markup.setKeyboard(rowList);
        return markup;

    }

    public static ReplyKeyboardMarkup getMenu(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.KVARTIRA_UZ : DemoUtil.KVARTIRA_RU);
        row.add(button);
        KeyboardButton button1 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.HOUSE_UZ : DemoUtil.HOUSE_RU);
        row.add(button1);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button2 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.GROUND_UZ : DemoUtil.GROUND_RU);
        row1.add(button2);
        KeyboardButton button3 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.FIELD_YARD_UZ : DemoUtil.FIELD_YARD_RU);
        row1.add(button3);
        rowList.add(row1);

        KeyboardRow row3 = new KeyboardRow();

        KeyboardButton button6 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.MY_REKLAMA_UZ : DemoUtil.MY_REKLAMA_RU);
        row3.add(button6);
        KeyboardButton button7 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.GIVE_REKLAMA_UZ : DemoUtil.GIVE_REKLAMA_RU);
        row3.add(button7);
        rowList.add(row3);

        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton button4 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.LIKED_UZ : DemoUtil.LIKED_RU);
        row2.add(button4);
        KeyboardButton button5 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.SETTING_UZ : DemoUtil.SETTING_RU);
        row2.add(button5);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        markup.setResizeKeyboard(true);
        return markup;

    }

    public static InlineKeyboardMarkup getCategoryMenuForUser(List<Category> categories, Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Category category : categories) {
            if (!category.getIsDeleted()) {

                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();

                button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
                button.setCallbackData("C/" + category.getId() + "/0");
                System.out.println("C/" + category.getId() + "/0");
                row.add(button);
                rowList.add(row);
            }
        }

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? " Orqaga" : "Назад");
        button1.setCallbackData(DemoUtil.BACK);
        row1.add(button1);
        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static ReplyKeyboardMarkup getSettingMenu(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.SETTING_LANGUAGE_UZ : DemoUtil.SETTING_LANGUAGE_RU);
        row.add(button);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.SETTING_FULLNAME_UZ : DemoUtil.SETTING_FULLNAME_RU);
        row1.add(button1);
        rowList.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.SETTING_CALL_NUMBER_UZ : DemoUtil.SETTING_CALL_NUMBER_RU);
        row2.add(button2);
        rowList.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button3 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.BACK_REPLY_UZ : DemoUtil.BACK_REPLY_RU);
        row3.add(button3);
        rowList.add(row3);

        markup.setKeyboard(rowList);
        markup.setResizeKeyboard(true);

        return markup;

    }

    public static InlineKeyboardMarkup getProduct
            (List<Product> products, long productId, int categoryId, Language language, User user) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        Product product = products.get((int) productId);

        if (productId != 0) {

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("⏪");
            button.setCallbackData("C/" + categoryId + "/" + (productId - 1));
            row.add(button);

        }

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("\uD83D\uDDA4");
        button4.setCallbackData("Liked" + (product.getId()) + "/" + user.getId());
        row.add(button4);

        if (products.size() != productId + 1) {

            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText("⏩");
            button1.setCallbackData("C/" + categoryId + "/" + (productId + 1));
            row.add(button1);

        }
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? "\uD83D\uDCCC Joylashuv" : "\uD83D\uDCCC Место нахождения");
        button1.setCallbackData("Loc/" + (product.getId()));
        row1.add(button1);

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCDE");
        button.setCallbackData("Call/" + (product.getId()));
        row1.add(button);

        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getProductLiked
            (int step, Language language, int productId, int likedSize) {


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        if (step != 0) {

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("⏪");
            button.setCallbackData("Lleft/" + step);
            row.add(button);

        }

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("\uD83D\uDDD1");
        button4.setCallbackData("D/" + productId);
        row.add(button4);


        if (step + 1 < likedSize) {

            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText("⏩");
            button1.setCallbackData("Lright/" + step);
            row.add(button1);

        }
        rowList.add(row);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static ReplyKeyboardMarkup getAdminMenu(Language language) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.CATEGORY_CRUD_UZ : DemoUtil.CATEGORY_CRUD_RU);
        row.add(button);
        KeyboardButton button1 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.CUSTOMER_CRUD_UZ : DemoUtil.CUSTOMER_CRUD_RU);
        row.add(button1);
        rowList.add(row);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.REKLAMA_PRODUCT_UZ : DemoUtil.REKLAMA_PRODUCT_RU);
        row1.add(button2);
        KeyboardButton button3 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.SETTING_UZ : DemoUtil.SETTING_RU);
        row1.add(button3);
        rowList.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button4 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.REKLAMA_GENERAL_UZ : DemoUtil.REKLAMA_GENERAL_RU);
        row2.add(button4);
        KeyboardButton button5 = new KeyboardButton(language.equals(Language.UZ) ?
                DemoUtil.ADD_ADMIN_UZ : DemoUtil.ADD_ADMIN_RU);
        row2.add(button5);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public static InlineKeyboardMarkup getMainCategory(Language language) {

        List<Category> categories = Database.categories.stream()
                .filter(category -> category.getCategoryId() == 0 && !category.getIsDeleted())
                .toList();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Category category : categories) {

            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
            button.setCallbackData("ct/" + category.getId());
            row.add(button);
            rowList.add(row);

        }

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getSendAdminProductRequest(Long customerId, Long productId) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("✅");
        button.setCallbackData("✅/" + customerId + "/" + productId);
        row.add(button);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("❌");
        button1.setCallbackData("❌/" + customerId + "/" + productId);
        row.add(button1);

        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getShowMyProduct(
            Long userId, Language language, int productId, List<Product> products) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        if (productId != 0) {

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("⏪");
            button.setCallbackData("My/" + userId + "/" + (productId - 1));
            row.add(button);
        }

        Long id = products.get(productId).getId();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("\uD83D\uDDD1");
        button.setCallbackData("MyD/" + userId + "/" + id);
        row.add(button);

        if (products.size() - 1 > productId) {

            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText("⏩");
            button1.setCallbackData("My/" + userId + "/" + (productId + 1));
            row.add(button1);
        }

        rowList.add(row);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getAdminAddOrRevoke(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<User> users = Database.customers.stream()
                .filter(user -> !user.getRole().equals(Role.SUPER_ADMIN) && !user.getIsBlocked())
                .toList();

        //Collections.sort(users);

        for (User user : users) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(user.getId() + "   ROLE:" + user.getRole());
            button.setCallbackData("AR/" + user.getId());
            row.add(button);
            rowList.add(row);
        }

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }


    public static ReplyKeyboardMarkup getRefresh() {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("\uD83D\uDD01REFRESH");
        row.add(button);
        rowList.add(row);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getRequestAdvertisementFromAdmin(String inlineName, String inlineUrl) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(inlineName);
        button.setCallbackData("reklama");

        //LoginUrl loginUrl = new LoginUrl(inlineUrl);
        button.setUrl(inlineUrl);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("✅");
        button1.setCallbackData(DemoUtil.CONFIRM);
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("❌");
        button2.setCallbackData(DemoUtil.BACK);
        row1.add(button2);

        rowList.add(row1);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getReklama(String inlineName, String inlineUrl) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(inlineName);
        button.setCallbackData("reklama");
        button.setUrl(inlineUrl);
        row.add(button);
        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;
    }

    public static InlineKeyboardMarkup getCategoryCRUD(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(language.equals(Language.UZ) ? "➕ Kategoriya qo'shish." : "➕ Добавьте категорию.");
        button.setCallbackData(DemoUtil.CATEGORY_CREATE);
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ?
                "\uD83D\uDCC6 Kategoriya ko'rish." : "\uD83D\uDCC6 Категория просмотра.");
        button1.setCallbackData(DemoUtil.CATEGORY_SHOW);
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ?
                "♻️ Kategoriya o'zgartirish." : "♻️ Смена категории.");
        button2.setCallbackData(DemoUtil.CATEGORY_UPDATE);
        row2.add(button2);
        rowList.add(row2);


        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(language.equals(Language.UZ) ?
                "\uD83D\uDDD1 Kategoriya o'chirish." : "\uD83D\uDDD1 Удалить категорию.");
        button3.setCallbackData(DemoUtil.CATEGORY_DELETED);
        row3.add(button3);
        rowList.add(row3);


        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button4.setCallbackData(DemoUtil.BACK);
        row4.add(button4);
        rowList.add(row4);
        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup getCustomerMenuForCrudOperation(User customer, Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText(customer.getIsBlocked() ? language.equals(Language.UZ) ?
                "\uD83D\uDD13 Blockdan yechish" : "\uD83D\uDD13 Разблокировать" : language.equals(Language.UZ) ?
                "\uD83D\uDD10 Blocklash" : "\uD83D\uDD10 Блокировка");
        button.setCallbackData("Block/" + (customer.getId()));
        row.add(button);
        rowList.add(row);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(language.equals(Language.UZ) ? " \uD83D\uDD0E Mijozning e'lonlari "
                : " \uD83D\uDD0E Объявления клиентов ");
        button1.setCallbackData("CP/" + (customer.getId()) + "/0");
        row1.add(button1);
        rowList.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(customer.getLanguage().equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getAllCustomers(Language language) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<User> customers = Database.customers.stream()
                .filter(user -> user.getRole().equals(Role.CUSTOMER) || user.getRole().equals(Role.ADMIN))
                .toList();

        for (User customer : customers) {

            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(customer.getId() + "  ROLE:" + customer.getRole());
            button.setCallbackData("Cus/" + customer.getId());
            row.add(button);
            rowList.add(row);

        }

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static ReplyKeyboardMarkup getContactAdmin() {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("✏️ REQUEST");
        row.add(button);
        rowList.add(row);

        markup.setKeyboard(rowList);
        return markup;

    }

    public static InlineKeyboardMarkup getBlockedUserResponse(Long userId) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("✏️ RESPONSE");
        button.setCallbackData("RESPONSE/" + userId);
        row.add(button);

        rowList.add(row);
        markup.setKeyboard(rowList);

        return markup;

    }

    public static InlineKeyboardMarkup getUserProducts
            (int step, Language language, long customerId, int productSize) {


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        if (step != 0) {

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("⏪");
            button.setCallbackData("CP/" + (customerId) + "/" + (step - 1));
            row.add(button);

        }

        if (step + 1 < productSize) {

            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText("⏩");
            button1.setCallbackData("CP/" + (customerId) + "/" + (step + 1));
            row.add(button1);

        }
        rowList.add(row);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(language.equals(Language.UZ) ? "\uD83D\uDD1A Chiqish" : "\uD83D\uDD1A Выход");
        button2.setCallbackData(DemoUtil.BACK);
        row2.add(button2);
        rowList.add(row2);

        markup.setKeyboard(rowList);
        return markup;

    }
}