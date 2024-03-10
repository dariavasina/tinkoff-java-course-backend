package edu.java.controller;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.service.LinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController {
    private final LinksService linkService;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader Long tgChatId) {
        return linkService.getLinks(tgChatId);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(@RequestHeader Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        return linkService.addLink(tgChatId, addLinkRequest);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.deleteLink(tgChatId, removeLinkRequest);
    }
}
