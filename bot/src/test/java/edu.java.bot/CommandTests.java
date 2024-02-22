package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.*;
import edu.java.bot.service.TrackedLinkRepository;
import edu.java.bot.service.UserMessageProcessor;
import org.junit.Test;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CommandTests {
    @Test
    public void testStartCommand() {
        StartCommand startCommand = new StartCommand();

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/start");

        SendMessage response = startCommand.handle(update);
        assertEquals("Вы зарегистрированы!", response.getParameters().get("text"));
    }

    private void mockRegistration() throws NoSuchFieldException, IllegalAccessException {
        UserMessageProcessor userMessageProcessorMock = mock(UserMessageProcessor.class);

        Field field = UserMessageProcessor.class.getDeclaredField("userRegistrationStatus");
        field.setAccessible(true);

        field.set(userMessageProcessorMock, new HashMap<Long, Boolean>());
    }

    @Test
    public void testHelpCommand() throws NoSuchFieldException, IllegalAccessException {
        HelpCommand helpCommand = new HelpCommand();

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/help");

        mockRegistration();


        SendMessage response = helpCommand.handle(update);

        StringBuilder messageBuilder = new StringBuilder("Список команд:\n");
        messageBuilder.append("/start - зарегистрировать пользователя\n");
        messageBuilder.append("/help - вывести окно с командами\n");
        messageBuilder.append("/track - начать отслеживание ссылки\n");
        messageBuilder.append("/untrack - прекратить отслеживание ссылки\n");
        messageBuilder.append("/list - показать список отслеживаемых ссылок");
        String expectedResponse = messageBuilder.toString();

        assertEquals(expectedResponse, response.getParameters().get("text"));
    }

    @Test
    public void testTrackCommand() throws NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        TrackCommand trackCommand = new TrackCommand(trackedLinkRepository);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/track https://example.com");

        mockRegistration();


        SendMessage response = trackCommand.handle(update);
        assertEquals("Ссылка добавлена для отслеживания", response.getParameters().get("text"));
    }

    @Test
    public void testUntrackCommand() throws NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        UntrackCommand untrackCommand = new UntrackCommand(trackedLinkRepository);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/untrack https://example.com");

        mockRegistration();

        SendMessage response = untrackCommand.handle(update);
        assertEquals("Отслеживание ссылки прекращено!", response.getParameters().get("text"));
    }

    @Test
    public void testIncorrectLinkTrackCommand() throws NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        TrackCommand trackCommand = new TrackCommand(trackedLinkRepository);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/track htta:/dgksjdfhjsf.com");

        mockRegistration();


        SendMessage response = trackCommand.handle(update);
        assertEquals("Неверный формат URL. Попробуйте снова", response.getParameters().get("text"));
    }

    @Test
    public void testIncorrectLinkUntrackCommand() throws NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        UntrackCommand untrackCommand = new UntrackCommand(trackedLinkRepository);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/untrack sdfjhskdjfhsdf");

        mockRegistration();

        SendMessage response = untrackCommand.handle(update);
        assertEquals("Неверный формат URL. Попробуйте снова", response.getParameters().get("text"));
    }

    @Test
    public void testRandomMessage() throws NoSuchFieldException, IllegalAccessException {
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(new ArrayList<>());

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("random message");

        Field field = UserMessageProcessor.class.getDeclaredField("userRegistrationStatus");
        field.setAccessible(true);
        Map<Long, Boolean> userRegistrationStatus = (Map<Long, Boolean>) field.get(userMessageProcessor);
        userRegistrationStatus.put(123L, true);

        SendMessage response = userMessageProcessor.process(update);
        assertEquals("Неизвестная команда. Попробуйте /help для получения списка команд.", response.getParameters().get("text"));
    }

    @Test
    public void testListCommandNotEmpty() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        URI uri1 = new URI("https://example.com");
        URI uri2 = new URI("https://github.com");
        when(trackedLinkRepository.getTrackedLinks(anyLong())).thenReturn((List<URI>) List.of(uri1, uri2));

        ListCommand listCommand = new ListCommand(trackedLinkRepository);
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(List.of(listCommand));

        Field field = UserMessageProcessor.class.getDeclaredField("userRegistrationStatus");
        field.setAccessible(true);
        Map<Long, Boolean> userRegistrationStatus = (Map<Long, Boolean>) field.get(userMessageProcessor);
        userRegistrationStatus.put(123L, true);

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
    public void testListCommandEmpty() throws NoSuchFieldException, IllegalAccessException {
        TrackedLinkRepository trackedLinkRepository = mock(TrackedLinkRepository.class);
        when(trackedLinkRepository.getTrackedLinks(anyLong())).thenReturn(List.of());


        ListCommand listCommand = new ListCommand(trackedLinkRepository);
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(List.of(listCommand));

        Field field = UserMessageProcessor.class.getDeclaredField("userRegistrationStatus");
        field.setAccessible(true);
        Map<Long, Boolean> userRegistrationStatus = (Map<Long, Boolean>) field.get(userMessageProcessor);
        userRegistrationStatus.put(123L, true);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.text()).thenReturn("/list");
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(123L);

        mockRegistration();


        SendMessage actualSendMessage = userMessageProcessor.process(update);
        String expectedMessage = "Список отслеживаемых ссылок пуст.";

        assertEquals(actualSendMessage.getParameters().get("text"), expectedMessage);
    }


}
