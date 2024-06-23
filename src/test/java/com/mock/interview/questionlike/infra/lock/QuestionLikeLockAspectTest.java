package com.mock.interview.questionlike.infra.lock;

import com.mock.interview.questionlike.presentation.dto.QuestionLikeDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class QuestionLikeLockAspectTest {
    @Autowired
    QuestionLikeLockAspect aspect;

    public static long result = 0;

//    @Test
    void test1() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenAnswer(invocation -> result++);
        when(pjp.getArgs()).thenReturn(new Object[]{new QuestionLikeDto(1L, 1L)});

        int testThreadCnt = 1000;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(testThreadCnt);

        for (int i = 0; i < testThreadCnt; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();  // 모든 스레드가 이 부분에서 대기
                    aspect.applyLocking(pjp);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } finally {
                    endLatch.countDown();  // 스레드 종료를 알림
                }
            }).start();
        }

        startLatch.countDown();  // 모든 스레드를 동시에 시작하게 함
        endLatch.await();  // 모든 스레드가 작업을 마칠 때까지 대기
        System.out.println(result);
    }

}