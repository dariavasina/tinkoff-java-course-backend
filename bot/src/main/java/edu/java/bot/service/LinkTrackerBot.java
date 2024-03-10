package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.respository.TrackedLinkRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkTrackerBot implements Bot {
    private static final Logger LOGGER = LogManager.getLogger(LinkTrackerBot.class);

    private final TelegramBot telegramBot;
    private List<Command> commands;

    private final TrackedLinkRepository trackedLinkRepository = new TrackedLinkRepository();

    private final UserMessageProcessor userMessageProcessor;

    @Autowired
    public LinkTrackerBot(TelegramBot telegramBot, List<Command> commands, UserMessageProcessor userMessageProcessor) {
        this.telegramBot = telegramBot;
        this.commands = commands;
        this.userMessageProcessor = userMessageProcessor;

        setMyCommands();

    }

    private void setMyCommands() {
        List<BotCommand> botApiCommands = new ArrayList<>();
        for (Command command : commands) {
            botApiCommands.add(command.toApiCommand());
        }
        BotCommand[] botCommandsArray = botApiCommands.toArray(new BotCommand[0]);

        SetMyCommands setMyCommands = new SetMyCommands(botCommandsArray);

        execute(setMyCommands);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> BaseResponse execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        int processedUpdates = 0;
        for (Update update : updates) {
            if (update.message() != null) {
                String messageText = update.message().text();
                Long chatId = update.message().chat().id();

                SendMessage response = userMessageProcessor.process(update);

                SendResponse sendResponse = telegramBot.execute(response);
                if (!sendResponse.isOk()) {
                    LOGGER.error("Message failed to send. Error: " + sendResponse.errorCode());
                } else {
                    processedUpdates++;
                }
            }
        }
        return processedUpdates;
    }


    @Override
    public void start() {
        telegramBot.setUpdatesListener(updates -> {
            process(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public void close() {
        telegramBot.removeGetUpdatesListener();
    }


}
