package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.StackOverflowClient;
import edu.java.dto.response.StackOverflowQuestionResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackOverflowClientTest {

    @RegisterExtension
    private static WireMockExtension wireMockExtension =
        WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    private StackOverflowClient stackOverflowClient;
    @BeforeEach
    public void setup() {
        stackOverflowClient = new StackOverflowClient(WebClient.builder(), wireMockExtension.baseUrl());
    }

    @Test
    public void stackOverflowTest() {
        wireMockExtension.stubFor(get("/questions/1274057?site=stackoverflow")
            .willReturn(okJson("""
                {
                    "items": [
                        {
                            "tags": [
                                "git",
                                "gitignore",
                                "git-rm"
                            ],
                            "owner": {
                                "account_id": 9225,
                                "reputation": 100349,
                                "user_id": 16957,
                                "user_type": "registered",
                                "accept_rate": 69,
                                "profile_image": "https://www.gravatar.com/avatar/c5380b957ff8ce5cb14cf2282ffeb720?s=256&d=identicon&r=PG",
                                "display_name": "Ivan",
                                "link": "https://stackoverflow.com/users/16957/ivan"
                            },
                            "is_answered": true,
                            "view_count": 2351171,
                            "protected_date": 1504805794,
                            "accepted_answer_id": 1274447,
                            "answer_count": 34,
                            "score": 8184,
                            "last_activity_date": 1706450501,
                            "creation_date": 1250191402,
                            "last_edit_date": 1658719875,
                            "question_id": 1274057,
                            "content_license": "CC BY-SA 4.0",
                            "link": "https://stackoverflow.com/questions/1274057/how-do-i-make-git-forget-about-a-file-that-was-tracked-but-is-now-in-gitignore",
                            "title": "How do I make Git forget about a file that was tracked, but is now in .gitignore?"
                        }
                    ],
                    "has_more": false,
                    "quota_max": 300,
                    "quota_remaining": 298
                }""")));
        StackOverflowQuestionResponse response = stackOverflowClient.getQuestionById("1274057");

        assertEquals(34, response.getAnswerCount());
        assertEquals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1706450501), ZoneOffset.UTC), response.getLastActivityDate());
    }
}
