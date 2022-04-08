package com.company.util;

import com.company.db.Database;
import com.company.enums.Language;
import com.company.model.Category;
import com.company.model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
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
                 DemoUtil.LIKED_UZ: DemoUtil.LIKED_RU);
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

            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu());
            button.setCallbackData("C/"+category.getCategoryId());
            row.add(button);
            rowList.add(row);

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
}
