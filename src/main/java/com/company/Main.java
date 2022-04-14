package com.company;

import com.company.controller.BotControl;
import com.company.db.Database;
import com.company.db.DbConnection;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static BotControl MY_TELEGRAM_BOT;

    public static void main(String[] args) {

        try {

            Main.MY_TELEGRAM_BOT = new BotControl();
            Database.compile();
            DbConnection.readFromDatabase();
            headerMethod();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void headerMethod() {

        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(MY_TELEGRAM_BOT);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        // Stack
    }
}
