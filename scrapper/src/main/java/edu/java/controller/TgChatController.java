package edu.java.controller;

import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TgChatController {

    private final TgChatService tgChatService;

    public ResponseEntity<Void> registerChat(Long id) {
        return tgChatService.registerChat(id);
    }

    public ResponseEntity<Void> deleteChat(Long id) {
        return tgChatService.deleteChat(id);
    }
}
