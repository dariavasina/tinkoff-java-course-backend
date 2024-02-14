package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;

public class UserMessageProcessor {
    private final List<Command> commands;

    public UserMessageProcessor(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> commands() {
        return commands;
    }

    public SendMessage process(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        // Поиск подходящей команды
        for (Command command : commands) {
            if (messageText.startsWith(command.command())) {
                return command.handle(update);
            }
        }

        // Если команда не найдена, возвращаем сообщение об ошибке
        return new SendMessage(chatId, "Неизвестная команда. Попробуйте /help для получения списка команд.");
    }
}
