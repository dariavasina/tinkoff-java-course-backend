package edu.java.model.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LinkUpdate(
    long id,
    @NotEmpty String url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {
}
