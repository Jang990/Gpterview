package com.mock.interview.interviewquestion.infra.analyzer;

import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.okt.Tagger;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class KorAnalyzer {

    /** , ! @ . 등등 */
    private final String PUNCTUATION_TYPE = "SF";

    public List<Sentence> analyzeMorphemes(String base) {
        Tagger tagger = new Tagger();
        List<Sentence> tag = tagger.tag(base);
        return tag;
    }

    public List<String> extractNecessaryWords(String base) {
        List<String> result = new LinkedList<>();
        List<Sentence> tag = analyzeMorphemes(base);
        for (Sentence sent : tag) { /* 나는 사과,배,귤이 있어요. */
            for (Word word : sent) { /* 나는 | 사과,배,귤,이 | 있어요 */
                for(Morpheme morph : word){ /* {'나', '는'} | {'사과', ',', '배', ',', '귤', '이'} | {'있어요', '.'} */
                    boolean isUnnecessaryWords = morph.isJosa() || morph.getTag().name().equalsIgnoreCase(PUNCTUATION_TYPE);
                    if (isUnnecessaryWords) {
                        continue;
                    }
                    result.add(morph.getSurface());
                }
            }
        }

        return result;
    }
}
