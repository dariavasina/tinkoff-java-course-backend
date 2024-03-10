package edu.java.model.request;

import jakarta.validation.constraints.NotEmpty;

public record RemoveLinkRequest(
    @NotEmpty String link
) {
}
