package edu.java.model.request;

import jakarta.validation.constraints.NotEmpty;

public record AddLinkRequest(
    @NotEmpty String link
    ) {
    }
