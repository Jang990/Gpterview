package com.mock.interview.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TalkController {
    @PostMapping("/talk-test")
    public String talk(@RequestBody String clientTalk) {
        log.info("사용자 음성 : {}", clientTalk);
        return "안녕하세요!";
    }

    @PostMapping("/test")
    public String test(String talk) {
        log.info("Hello World! : {}", talk);
        return "ok";
    }
}
