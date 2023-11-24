package com.mock.interview.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestTalkController {
    @PostMapping("/talk-test")
    public String talk(@RequestBody ClientTalkRequest request) {
        log.info("사용자 음성 : {}", request);
        return "응답 완료했습니다.";
    }
}
