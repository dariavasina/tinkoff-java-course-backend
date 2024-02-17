package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkTracker;
import java.net.URI;
import java.net.URISyntaxException;

public class TrackCommand implements Command {
    private final LinkTracker linkTracker;
    public TrackCommand(LinkTracker linkTracker) {
        this.linkTracker = linkTracker;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        String[] parts = messageText.split("\\s+", 2);
        if (parts.length == 2) {
            String url = parts[1];
            try {
                URI uri = new URI(url);

                linkTracker.trackLink(chatId, uri);

                return new SendMessage(chatId, "Ссылка добавлена для отслеживания");
            } catch (URISyntaxException e) {
                return new SendMessage(chatId, "Неверный URL. Попробуйте снова");
            }
        } else {
            return new SendMessage(chatId, "Не указан URL для отслеживания.");
        }
    }
}
