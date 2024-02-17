package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.LinkTrackerBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class LinkTrackerBotTest {

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private LinkTrackerBot linkTrackerBot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessUpdate_StartCommand() {
        // Mocking the Update
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1234L);

        // Mocking the response of SendMessage request
        SendResponse sendResponse = mock(SendResponse.class);
        when(sendResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(ArgumentMatchers.any(SendMessage.class))).thenReturn(sendResponse);

        // Calling the method to test
        linkTrackerBot.process(Collections.singletonList(update));

        // Verifying that SendMessage request is executed with expected parameters
        verify(telegramBot).execute(new SendMessage(1234L, "Вы зарегистрированы!"));
    }
}
