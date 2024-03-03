package edu.java.client;


import edu.java.dto.response.StackOverflowQuestionResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private final WebClient webClient;

    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StackOverflowQuestionResponse getQuestionById(String id) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("/questions/{id}").queryParam("site", "stackoverflow").build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve().bodyToMono(StackOverflowQuestionResponse.class).block();
    }
}
