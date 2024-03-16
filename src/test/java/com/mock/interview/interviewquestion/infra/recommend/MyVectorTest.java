package com.mock.interview.interviewquestion.infra.recommend;

import com.mock.interview.interviewquestion.infra.recommend.dto.CurrentQuestion;
import com.mock.interview.interviewquestion.infra.recommend.dto.QuestionMetaData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class MyVectorTest {
    TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
    CosineSimilarityCalculator cosineSimilarityCalculator = new CosineSimilarityCalculator();
    @Test
    void test() {
        List<String> base = Arrays.asList("Spring", "의", "MVC", "대해", "아는대로", "설명", "해보세요");
        List<QuestionMetaData> simpleTestData = SimpleRecommendScoreCalculatorTest.createSimpleTestData();
        List<List<String>> targetDocs = simpleTestData.stream().map(QuestionMetaData::getNecessaryWords).toList();
        double[][] vector = new double[targetDocs.size()][base.size()];
        double[] baseVector = new double[base.size()];
        for (int i = 0; i < base.size(); i++) {
            baseVector[i] = tfidfCalculator.tfIdf(base, targetDocs, base.get(i));
        }
        for (int i = 0; i < base.size(); i++) {
            System.out.print(String.format("%.5f \t", baseVector[i]));
        }
        System.out.println();

        for (int i = 0; i < targetDocs.size(); i++) { // 전체 문서 순회
            double[] targetVector = tfidfCalculator.tfIdfVector(base, targetDocs.get(i), targetDocs);
            System.out.println(targetDocs.get(i) + " " + cosineSimilarityCalculator.calculate(baseVector, targetVector));
        }

        /*for (int i = 0; i < vector.length; i++) {
            for (String word : targetDocs.get(i)) {
                System.out.print(String.format("%6s\t", word));
            }
            System.out.println();
            for (int j = 0; j < vector[0].length; j++) {
                System.out.print(String.format("%.6f\t", vector[i][j]));
            }
            System.out.println();
            System.out.println();
        }*/
    }


}
