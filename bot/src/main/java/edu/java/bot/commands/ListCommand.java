package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkTracker;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    private final LinkTracker linkTracker;
    public ListCommand(LinkTracker linkTracker) {
        this.linkTracker = linkTracker;
    }

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
        List<URI> trackedLinks = linkTracker.getTrackedLinks(chatId);

        if (trackedLinks.isEmpty()) {
            return new SendMessage(chatId, "Список отслеживаемых ссылок пуст.");
        } else {
            StringBuilder messageBuilder = new StringBuilder("Отслеживаемые ссылки:\n");
            for (URI link : trackedLinks) {
                messageBuilder.append(link).append("\n");
            }
            return new SendMessage(chatId, messageBuilder.toString());
        }
    }
}
