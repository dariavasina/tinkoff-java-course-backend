package edu.java.controller;

import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TgChatController {

    private final TgChatService tgChatService;

    @PostMapping
    public ResponseEntity<Void> registerChat(@RequestHeader Long id) {
        return tgChatService.registerChat(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteChat(@RequestHeader Long id) {
        return tgChatService.deleteChat(id);
    }
}
