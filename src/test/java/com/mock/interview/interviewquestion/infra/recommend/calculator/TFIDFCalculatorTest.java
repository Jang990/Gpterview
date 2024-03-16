package com.mock.interview.interviewquestion.infra.recommend.calculator;

import com.mock.interview.interviewquestion.infra.recommend.calculator.TFIDFCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

class TFIDFCalculatorTest {
    TFIDFCalculator calculator = new TFIDFCalculator();

    @Test
    void test() {
        List<String> base = List.of("A","B","C","D","E","F","G");
        List<List<String>> docs = List.of(
                base,
                List.of("A", "B", "C", "D", "E", "A"),
                List.of("A", "B"),
                List.of("A", "C"),
                List.of("A", "C"),
                List.of("A", "C"),
                List.of("A", "C"),
                List.of("A")
        );

        for (String word : base) {
            double result = calculator.tfIdf(docs.get(0), docs, word);
            System.out.println(word + " : " + result);
        }
    }
}