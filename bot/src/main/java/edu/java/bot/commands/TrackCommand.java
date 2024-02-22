package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.TrackedLinkRepository;
import edu.java.bot.utils.URLValidator;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class TrackCommand implements Command {
    private final TrackedLinkRepository trackedLinkRepository;

    public TrackCommand(TrackedLinkRepository trackedLinkRepository) {
        this.trackedLinkRepository = trackedLinkRepository;
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
        if (parts.length != 2) {
            return new SendMessage(chatId, "Не указан URL для отслеживания.");
        }
        String url = parts[1];
        if (!URLValidator.isValidUrl(url)) {
            return new SendMessage(chatId, "Неверный формат URL. Попробуйте снова");
        }
        try {
            URI uri = URLValidator.extractUri(url);

            trackedLinkRepository.trackLink(chatId, uri);

            return new SendMessage(chatId, "Ссылка добавлена для отслеживания");
        } catch (URISyntaxException e) {
            return new SendMessage(chatId, "Неверный URL. Попробуйте снова");
        }
    }
}
