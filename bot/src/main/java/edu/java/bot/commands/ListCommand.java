package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        List<String> trackedLinks = new ArrayList<>(); // Заглушка
        if (trackedLinks.isEmpty()) {
            return new SendMessage(chatId, "Список отслеживаемых ссылок пуст.");
        } else {
            StringBuilder messageBuilder = new StringBuilder("Список отслеживаемых ссылок:\n");
            for (String link : trackedLinks) {
                messageBuilder.append(link).append("\n");
            }
            return new SendMessage(chatId, messageBuilder.toString());
        }
    }
}
