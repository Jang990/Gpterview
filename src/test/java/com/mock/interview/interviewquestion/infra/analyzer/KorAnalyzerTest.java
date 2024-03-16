package com.mock.interview.interviewquestion.infra.analyzer;

import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.okt.Tagger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class KorAnalyzerTest {

    KorAnalyzer korAnalyzer;
    String base = "HTTP METHOD에는 get, post, put, delete 가 있습니다.\n" +
            "단순히 종류를 나열하는 것이 아닌, get과 post 간의 차이점에 대해 말할 수 있어야 합니다.";

    @BeforeEach
    void beforeEach() {
        korAnalyzer = new KorAnalyzer();
    }

    @Test
    @DisplayName("형태소 분석")
    void test() {
        List<String> result = korAnalyzer.extractNecessaryWords("Spring MVC에 대해 아는대로 설명해보세요.");
        System.out.println(result);
    }

//    @Test
    @DisplayName("샘플 코드")
    void sample() {
        Tagger tagger = new Tagger();
        List<Sentence> tag = tagger.tag("React의 장점은 무엇인가요?");
        for (Sentence sent : tag) {
            System.out.println("===== Sentence =====");
            System.out.println(sent.surfaceString()); // 단순히 종류를 나열하는 것이 아닌, get과 post 간의 차이점에 대해 말할 수 있어야 합니다.

            System.out.println("# Analysis Result");
            // println(sent.singleLineString())
            for (Word word : sent) {
                System.out.print("Word [" + word.getId() + "] " + word.getSurface() + " = "); // Word [0] 단순히

                for(Morpheme morph : word){

                    System.out.print(morph.getSurface() + "/" + morph.getTag() + "   "); // 단순히/VA
                    System.out.print(morph.isJosa() + " " + morph.isModifier() + " " + morph.isNoun() + " " + morph.isPredicate() + " \t");
                }
                System.out.println();
            }
        }
    }

}