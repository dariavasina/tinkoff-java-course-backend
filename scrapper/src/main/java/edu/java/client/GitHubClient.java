package edu.java.client;

import edu.java.response.GitHubRepositoryResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public GitHubRepositoryResponse getRepositoryInfo(String owner, String repoName) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repoName)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class).block();
    }
}

