package com.mock.interview.interview.infrastructure;

import com.mock.interview.interviewquestion.infra.ai.prompt.fomatter.StringFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class StringFormatterTest {

    @Test
    @DisplayName("성공")
    void testSuccess() {
        String target = "tem";
        String replaceStr = "user";
        String s = "Hello World! $_"+target+"_";
        Map<String, String> map = new HashMap<>();
        map.put(target, replaceStr);

        // Hello World! user
        String result = StringFormatter.format(s, map);
        assertThat(result).contains(replaceStr);
    }

    @Test
    @DisplayName("잘못된 파라미터")
    void testMal() {
        String s = "Hello World! $_aaa_";
        Map<String, String> map = new HashMap<>();
        map.put("$_aaa_", "user");

        // Hello World! null
        String result = StringFormatter.format(s, map);
        assertThat(result).contains("null");
    }

    @Test
    @DisplayName("파라미터 매칭 실패")
    void testMissMatch() {
        String s = "Hello World! $_aaa_";
        Map<String, String> map = new HashMap<>();
        map.put("abc", "user");

        // Hello World! null
        String result = StringFormatter.format(s, map);
        assertThat(result).contains("null");
    }

}