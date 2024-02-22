package edu.java.bot.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;


@Repository
public class TrackedLinkRepository {
    private Map<Long, List<URI>> trackedLinks = new HashMap<>();

    public void trackLink(Long chatId, URI link) {
        List<URI> links = trackedLinks.getOrDefault(chatId, new ArrayList<>());

        links.add(link);

        trackedLinks.put(chatId, links);
    }

    public List<URI> getTrackedLinks(Long chatId) {
        return trackedLinks.getOrDefault(chatId, new ArrayList<>());
    }

    public boolean untrackLink(Long chatId, URI link) {
        List<URI> links = trackedLinks.get(chatId);
        if (links != null && links.remove(link)) {
            return true;
        }
        return false; // link not found
    }
}

