package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMessageProcessor {
    private final List<Command> commands;
    private Map<Long, Boolean> userRegistrationStatus = new HashMap<>();

    public UserMessageProcessor(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> commands() {
        return commands;
    }


    public SendMessage process(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        userRegistrationStatus.putIfAbsent(chatId, false);
        boolean isUserRegistered = userRegistrationStatus.get(chatId);

        if (!isUserRegistered) {
            if (!messageText.startsWith("/start")) {
                return new SendMessage(chatId, "Для начала работы с ботом, пожалуйста, введите /start.");
            }

            userRegistrationStatus.put(chatId, true);
        }

        for (Command command : commands) {
            if (messageText.startsWith(command.command())) {
                return command.handle(update);
            }
        }
        return new SendMessage(chatId, "Неизвестная команда. Попробуйте /help для получения списка команд.");
    }
}
