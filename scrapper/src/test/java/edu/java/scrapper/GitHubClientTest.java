package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.response.GitHubRepositoryResponse;
import edu.java.client.GitHubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubClientTest {
    @RegisterExtension
    private static WireMockExtension wireMockExtension =
        WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    private GitHubClient gitHubClient;
    @BeforeEach
    public void setup() {
        gitHubClient = new GitHubClient(WebClient.builder(), wireMockExtension.baseUrl());
    }

    private void configStubGitHub() {
        wireMockExtension.stubFor(get(urlEqualTo("/repos/user/test"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                                {
                                    "name": "test",
                                    "created_at": "2023-09-13T21:17:36Z",
                                    "updated_at": "2024-02-18T10:28:37Z",
                                    "pushed_at": "2024-01-31T22:21:31Z"
                                }
                                """)));
    }

    @Test
    public void gitHubTest() {
        configStubGitHub();
        GitHubRepositoryResponse response = gitHubClient.getRepositoryInfo("user", "test");

        assertEquals((OffsetDateTime.parse("2024-02-18T10:28:37Z")), response.updatedAt());
        assertEquals(OffsetDateTime.parse("2024-01-31T22:21:31Z"), response.pushedAt());
    }
}

