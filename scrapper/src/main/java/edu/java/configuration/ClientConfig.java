package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Value("${github.base-url}")
    private String githubBaseUrl;

    @Value("${stack-overflow.base-url}")
    private String stackoverflowBaseUrl;

    @Bean
    public GitHubClient githubWebClient() {
        return new GitHubClient(WebClient.builder(), githubBaseUrl);
    }

    @Bean
    public StackOverflowClient stackoverflowWebClient() {
        return new StackOverflowClient(WebClient.builder(), stackoverflowBaseUrl);
    }
}
