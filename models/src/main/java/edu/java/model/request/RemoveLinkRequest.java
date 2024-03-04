package edu.java.model.request;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

public record RemoveLinkRequest(
    @NotEmpty String link
) {
}
