package edu.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        log.info("Links updated");
    }
}
