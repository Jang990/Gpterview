package com.mock.interview.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringFormatterTest {

    @Test
    @DisplayName("성공")
    void testSuccess() {
        String target = "tem";
        String replaceStr = "user";
        String s = "Hello World! ${"+target+"}";
        Map<String, Object> map = new HashMap<>();
        map.put(target, replaceStr);

        // Hello World! user
        String result = StringFormatter.format(s, map);
        assertThat(result).contains(replaceStr);
    }

    @Test
    @DisplayName("잘못된 파라미터")
    void testMal() {
        String s = "Hello World! ${aaa}";
        Map<String, Object> map = new HashMap<>();
        map.put("${aaa}", "user");

        // Hello World! null
        String result = StringFormatter.format(s, map);
        assertThat(result).contains("null");
    }

    @Test
    @DisplayName("잘못된 템플릿")
    void testMal2() {
        String s = "Hello World! ${{}";
        Map<String, Object> map = new HashMap<>();
        map.put("{", "user");

        // Hello World! ${{}
        String result = StringFormatter.format(s, map);
        assertThat(result).doesNotContain("null");
    }

}