package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;








@Component
public class LinkTrackerBot implements Bot {
    private static final Logger LOGGER = Logger.getLogger(LinkTrackerBot.class.getName());

    private final TelegramBot telegramBot;
    private final ApplicationConfig applicationConfig;
    private ArrayList<Command> commands = new ArrayList<>();

    private final LinkTracker linkTracker = new LinkTracker();

    private final UserMessageProcessor userMessageProcessor;

    public LinkTrackerBot(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        telegramBot = new TelegramBot(applicationConfig.telegramToken());

        commands.add(new StartCommand());
        commands.add(new HelpCommand());
        commands.add(new TrackCommand(linkTracker));
        commands.add(new UntrackCommand(linkTracker));
        commands.add(new ListCommand(linkTracker));

        userMessageProcessor = new UserMessageProcessor(commands);

    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> BaseResponse execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        int processedUpdates = 0;
        for (Update update : updates) {
            String messageText = update.message().text();
            Long chatId = update.message().chat().id();

            SendMessage response = userMessageProcessor.process(update);
            if (response != null) {
                SendResponse sendResponse = telegramBot.execute(response);
                if (!sendResponse.isOk()) {
                    LOGGER.severe("Message failed to send. Error: " + sendResponse.errorCode());
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
