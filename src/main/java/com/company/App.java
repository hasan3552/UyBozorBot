package com.company;

import com.company.controllers.BotControl;
import com.company.database.Database;
import com.company.database.DbConnection;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {

    public static BotControl MY_TELEGRAM_BOT;

    public static void main(String[] args) {

        try {

            App.MY_TELEGRAM_BOT = new BotControl();
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
