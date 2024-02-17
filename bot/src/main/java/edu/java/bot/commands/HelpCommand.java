package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    @Override
    public String command() {
        return "/help";
    }

    public HelpCommand() {
    }

    @Override
    public String description() {
        return "Вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        StringBuilder messageBuilder = new StringBuilder("Список команд:\n");
        messageBuilder.append("/start - зарегистрировать пользователя\n");
        messageBuilder.append("/help - вывести окно с командами\n");
        messageBuilder.append("/track - начать отслеживание ссылки\n");
        messageBuilder.append("/untrack - прекратить отслеживание ссылки\n");
        messageBuilder.append("/list - показать список отслеживаемых ссылок");
        return new SendMessage(chatId, messageBuilder.toString());
    }
}
