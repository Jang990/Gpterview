package com.mock.interview.interviewquestion.infra.gpt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringTokenConfig {

    @Bean
    public Encoding stringTokenCounter() {
        /* (EncodingRegistry, Encoding) 두 클래스는 Thread-safe
        참고 : https://github.com/knuddelsgmbh/jtokkit */
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        return registry.getEncoding(EncodingType.CL100K_BASE);
    }

}
