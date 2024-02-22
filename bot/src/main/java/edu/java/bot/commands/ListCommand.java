package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.TrackedLinkRepository;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;

@Component
public class ListCommand implements Command {
    private final TrackedLinkRepository trackedLinkRepository;

    public ListCommand(TrackedLinkRepository trackedLinkRepository) {
        this.trackedLinkRepository = trackedLinkRepository;
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
        List<URI> trackedLinks = trackedLinkRepository.getTrackedLinks(chatId);

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
