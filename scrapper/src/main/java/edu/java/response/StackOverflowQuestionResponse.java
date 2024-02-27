package edu.java.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Getter;

@Getter
public class StackOverflowQuestionResponse {
    private OffsetDateTime lastActivityDate;
    private Integer answerCount;

    @JsonProperty("items")
    private void getQuestionInfo(Object[] items) {
        if (items != null && items.length > 0) {
            Map<String, Object> item = (Map<String, Object>) items[0];
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            lastActivityDate = mapper.convertValue(item.get("last_activity_date"), OffsetDateTime.class);
            answerCount = mapper.convertValue(item.get("answer_count"), Integer.class);
        }
    }

}
