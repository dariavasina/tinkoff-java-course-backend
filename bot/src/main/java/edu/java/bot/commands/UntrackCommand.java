package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkTracker;
import edu.java.bot.utils.URLValidator;
import java.net.URI;
import java.net.URISyntaxException;

public class UntrackCommand implements Command {
    private final LinkTracker linkTracker;

    public UntrackCommand(LinkTracker linkTracker) {
        this.linkTracker = linkTracker;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        String[] parts = messageText.split("\\s+", 2);
        if (parts.length == 2) {
            String url = parts[1];
            if (URLValidator.isValidUrl(url)) {
                try {
                    URI uri = URLValidator.extractUri(url);

                    linkTracker.untrackLink(chatId, uri);

                    return new SendMessage(chatId, "Отслеживание ссылки прекращено!");
                } catch (URISyntaxException e) {
                    return new SendMessage(chatId, "Неверный URL. Попробуйте снова");
                }
            } else {
                return new SendMessage(chatId, "Неверный формат URL. Попробуйте снова");
            }

        } else {
            return new SendMessage(chatId, "Не указан URL для прекращения отслеживания");
        }


    }

}
