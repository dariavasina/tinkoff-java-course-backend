package edu.java.bot;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.service.LinkTrackerBot;
import org.apache.kafka.common.network.Send;
import org.junit.Before;
import org.junit.Test;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.service.UserMessageProcessor;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ser.Serializers;

public class HelpCommandTest {

    private HelpCommand helpCommand;
    private UserMessageProcessor messageProcessor;
    private LinkTrackerBot bot;

    @Before
    public void setUp() {
        helpCommand = new HelpCommand();
        messageProcessor = mock(UserMessageProcessor.class);
        bot = mock(LinkTrackerBot.class);
    }

    @Test
    public void testHandle() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.text()).thenReturn("/help");


        Chat chat = mock(Chat.class);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(123L);
        when(update.message()).thenReturn(message);

        String expectedResponseText = "Your expected response text here";


        SendMessage sendMessage = new SendMessage(123L, expectedResponseText);

        BaseResponse actualResponse = bot.execute(helpCommand.handle(update));

        String actualMessageText = ((SendResponse) actualResponse).message().text();

        // Call the method under test
        //SendMessage actualResponse =

        // Now you can compare the actual response with the expected response
        assertEquals(expectedResponseText, actualResponse);
        //assertEquals(123L, actualResponse.chatId());
    }

}
