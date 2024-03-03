package edu.java.model.request;

import jakarta.validation.constraints.NotNull;

public record AddLinkRequest(
    @NotNull String link
    ) {
    }
