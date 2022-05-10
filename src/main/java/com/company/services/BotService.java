package com.company.services;

import com.company.App;
import com.company.database.DbConnection;
import com.company.enumm.Language;
import com.company.enumm.Status;
import com.company.models.User;
import com.company.utils.DemoUtil;
import com.company.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class BotService extends Thread{

    public Message message;
    public User user;
    public Language language;

    public BotService(Message message, User user) {
        this.message = message;
        this.user = user;
        language = user.getLanguage();
    }

    @Override
    public void run() {

        if (message.getText().equals("/start")){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("Please share contact.");
            ReplyKeyboardMarkup contact = KeyboardUtil.getContact();
            sendMessage.setReplyMarkup(contact);

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }


    }

    public void workCallbackQuery(CallbackQuery callbackQuery) {

        String data = callbackQuery.getData();

        if (user.getStatus().equals(Status.GIVE_LANGUAGE) &&
                (data.equals(DemoUtil.LANG_UZ) || data.equals(DemoUtil.LANG_RU))){

            user.setLanguage(data.equals(DemoUtil.LANG_RU) ? Language.RU : Language.UZ);
            DbConnection.setLanguageUser(user.getId(),user.getLanguage());

            user.setStatus(Status.GIVE_FULL_NAME);
            DbConnection.setStatusUser(user.getId(), user.getStatus());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(user.getLanguage().equals(Language.UZ) ? "Iltimos to'liq ism sharifingizni kiriting." +
                    "\n\n<b>MASALAN: FAYZULLAYEV HASAN</b>" :
                    "Пожалуйста, введите свое полное имя.\n\n<b> НАПРИМЕР: ФАЙЗУЛЛАЕВ ХАСАН </b>");
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

            App.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());
            App.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

        }

    }
}
