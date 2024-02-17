package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.service.LinkTracker;
import edu.java.bot.service.UserMessageProcessor;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ListCommandTest {
    @Test
    public void testListCommandNotEmpty() throws URISyntaxException {
        LinkTracker linkTracker = mock(LinkTracker.class);
        URI uri1 = new URI("https://example.com");
        URI uri2 = new URI("https://github.com");
        when(linkTracker.getTrackedLinks(anyLong())).thenReturn((List<URI>) List.of(uri1, uri2));

        ListCommand listCommand = new ListCommand(linkTracker);
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(List.of(listCommand));

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat().id()).thenReturn(123L);

        SendMessage response = userMessageProcessor.process(update);

        assertEquals("Отслеживаемые ссылки:\nhttps://example.com\nhttps://github.com\n", response.getParameters().get("text"));
    }

    @Test
    public void testListCommandEmpty() {
        LinkTracker linkTracker = mock(LinkTracker.class);
        when(linkTracker.getTrackedLinks(anyLong())).thenReturn(List.of());


        ListCommand listCommand = new ListCommand(linkTracker);
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(List.of(listCommand));

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.text()).thenReturn("/list");
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(123L);

        SendMessage actualSendMessage = userMessageProcessor.process(update);
        String expectedMessage = "Список отслеживаемых ссылок пуст.";

        assertEquals(actualSendMessage.getParameters().get("text"), expectedMessage);
    }
}
