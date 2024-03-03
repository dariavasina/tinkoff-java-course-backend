package edu.java.model.request;

import org.jetbrains.annotations.NotNull;

public record RemoveLinkRequest(
    @NotNull String link
) {
}
