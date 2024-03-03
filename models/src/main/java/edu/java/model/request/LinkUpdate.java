package edu.java.model.request;

import java.util.List;

public record LinkUpdate(
    long id,
    String url,
    String description,
    List<Long> tgChatIds
) {
}
