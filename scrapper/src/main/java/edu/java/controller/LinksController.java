package edu.java.controller;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.service.LinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController {
    private final LinksService linkService;

    public ResponseEntity<ListLinksResponse> getLinks(Long tgChatId) {
        return linkService.getLinks(tgChatId);
    }

    public ResponseEntity<LinkResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return linkService.addLink(tgChatId, addLinkRequest);
    }

    public ResponseEntity<LinkResponse> deleteLink(
        Long tgChatId,
        RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.deleteLink(tgChatId, removeLinkRequest);
    }
}
