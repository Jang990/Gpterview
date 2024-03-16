package com.mock.interview.interviewquestion.infra.recommend.calculator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TFIDFCalculator {

    public double[] tfIdfVector(List<String> base, List<String> target, List<List<String>> documents) {
        double[] vector = new double[base.size()];
        for (int i = 0; i < base.size(); i++) { // 타겟 문서에서 기준 문서의 단어에 대한 TF-IDF값 구하기
            vector[i] = tfIdf(target, documents, base.get(i));
        }
        return vector;
    }

    /**
     * TF-IDF 계산
     * @param doc 비교할 문서 형태소 처리 결과
     * @param docs 비교할 다른 문서들 형태소 처리 결과
     * @param term 기준 문서 형태소 중 한 단어
     * @return
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        double result = tf(doc, term) * idf(docs, term);
        if(Double.isNaN(result) || Double.isInfinite(result))
            return 0;
        return result;
    }

    /**
     * 특정 문서에서 기준 문서의 단어가 등장한 횟수
     * @param doc 특정 문서
     * @param term 기준 문서의 단어
     * @return
     */
    private double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    /** 전체 문서들에서 term이 얼마나 적게 나왔는지 */
    private double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }
}
