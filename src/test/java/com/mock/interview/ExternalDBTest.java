package com.mock.interview;

import com.mock.interview.global.QueryDslConfig;
import com.mock.interview.global.RedisConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import({QueryDslConfig.class, RedisConfig.class})
public @interface ExternalDBTest {
}
