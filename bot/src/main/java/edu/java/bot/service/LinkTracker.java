package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkTracker {
    private Map<Long, List<URI>> trackedLinks = new HashMap<>();

    public void trackLink(Long chatId, URI link) {
        List<URI> links = trackedLinks.getOrDefault(chatId, new ArrayList<>());

        links.add(link);

        trackedLinks.put(chatId, links);
    }

    public List<URI> getTrackedLinks(Long chatId) {
        return trackedLinks.getOrDefault(chatId, new ArrayList<>());
    }

    public void untrackLink(Long chatId, URI link) {
        trackedLinks.getOrDefault(chatId, new ArrayList<>()).remove(link);
    }
}

