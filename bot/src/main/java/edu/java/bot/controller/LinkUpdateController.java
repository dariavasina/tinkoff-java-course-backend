package edu.java.bot.controller;


import edu.java.bot.service.LinkUpdateService;
import edu.java.model.request.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class LinkUpdateController {

    private final LinkUpdateService linkUpdateService;

    @Autowired
    public LinkUpdateController(LinkUpdateService linkUpdateService) {
        this.linkUpdateService = linkUpdateService;
    }

    @PostMapping
    public ResponseEntity<Void> handleLinkUpdate(@Validated @RequestBody LinkUpdate linkUpdateRequest) {
        linkUpdateService.processLinkUpdate(linkUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

