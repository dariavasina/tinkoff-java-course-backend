package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.utility.BotUtils;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class LinkTrackerBot implements Bot {

    private final TelegramBot telegramBot;
    private final ApplicationConfig applicationConfig;
    private ArrayList<String> trackedLinks;

    public LinkTrackerBot(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        System.out.println(applicationConfig.telegramToken());
        telegramBot = new TelegramBot(applicationConfig.telegramToken());
        //telegramBot = new TelegramBot("6756123765:AAE6M3dKBwVg6_4te9pFVmbP7mjDTxqSlkI");

        //todo FIX TOKEN
        this.trackedLinks = new ArrayList<>();
        System.out.println("bot created");
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> BaseResponse execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        int processedUpdates = 0;
        for (Update update : updates) {
            SendMessage response = processUpdate(update);
            if (response != null) {
                BaseResponse baseResponse = execute(response);
                if (baseResponse.isOk()) {
                    processedUpdates++;
                }
            }
        }
        return processedUpdates;
    }


    private SendMessage processUpdate(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        switch (messageText) {
            case "/start":
                // логика регистрации пользователя
                return new SendMessage(chatId, "Вы зарегистрированы!");
            case "/help":
                // логика вывода окна с командами
                return new SendMessage(
                    chatId,
                    "Список команд:\n/start - зарегистрировать пользователя\n/help - вывести окно с командами\n/track - начать отслеживание ссылки\n/untrack - прекратить отслеживание ссылки\n/list - показать список отслеживаемых ссылок"
                );
            case "/track":
                // логика начала отслеживания ссылки
                return new SendMessage(chatId, "Отслеживание ссылки начато!");
            case "/untrack":
                // логика прекращения отслеживания ссылки
                return new SendMessage(chatId, "Отслеживание ссылки прекращено!");
            case "/list":
                // логика вывода списка отслеживаемых ссылок
                if (trackedLinks.isEmpty()) {
                    return new SendMessage(chatId, "Список отслеживаемых ссылок пуст.");
                } else {
                    StringBuilder messageBuilder = new StringBuilder("Список отслеживаемых ссылок:\n");
                    for (String link : trackedLinks) {
                        messageBuilder.append(link).append("\n");
                    }
                    return new SendMessage(chatId, messageBuilder.toString());
                }
            default:
                return new SendMessage(chatId, "Неизвестная команда. Попробуйте /help для получения списка команд.");
        }
    }

    @Override
    public void start() {
        telegramBot.setUpdatesListener(updates -> {
            System.out.println("LISTENING...");
            System.out.println(updates);
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public void close() {
        telegramBot.removeGetUpdatesListener();
    }

}
