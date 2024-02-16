package com.mock.interview.conversation.infrastructure.interview.gpt;

import com.mock.interview.conversation.infrastructure.interview.dto.Message;
import com.mock.interview.conversation.infrastructure.interview.setting.InterviewSetting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class ChatGPTRequesterTest {

    @Autowired ChatGPTRequester requester;

//    @Test
    void test() {
        /* 400 Bad Request: "{<EOL>  "error": {<EOL>
        "message": "'너는 IT 분야 백엔드 포지션의 면접관이야.\\n메시지가 길다고 마음대로 면접을 마무리하지마.\\n항상 단 하나의 질문만 해. 이미 대화한 주제를 질문하지마.\\n항상 3문장 이하로 응답해. 항상 마지막 문장으로 면접자를 평가할 수 있는 질문을 해.\\n지원자의 응답에 대한 평가는 간단하게. 질문은 자세하게 해.\\n\\nuser는 다음과 같은 경험을 함 [- 개요 |  ChatGPT와 모의 면접을 진행할 수 있는 서비스\\r\\n- 진행인원 |  **개인 프로젝트**\\r\\n- 핵심 기술 |  Java, Spring Boot, JPA, MySQL, Redis, Thymeleaf\\r\\n- 담당 내용 | \\r\\nRedis의 SETNX로 락을 구현하여 동시 요청 제어 기능 구현\\r\\nAI가 지원자에게 적합한 응답을 생성할 수 있는 프롬프트 생성 기능\\r\\nOpenAI API 요청 기능\\r\\n- 개선 경험 |\\r\\nStomp와 비동기 처리로 OpenAI API 연동 시 사용자 대기시간 89% 단축, - 개요 | 지도를 통해 고객 또는 장소를 조회, 관리하는 서비스\\r\\n- 진행 인원 | 디자이너 1, 모바일 2, **백엔드 2**\\r\\n- 핵심 기술 | Java, Spring Boot, JPA, PostgreSQL, Jenkins, Prometheus\\r\\n- 담당 내용 |\\r\\nSpring Data JPA를 활용한 고객 데이터 관리 기능\\r\\n현재 위치에서 반경 내의 고객 필터링 기능\\r\\n엑셀 파일을 이용한 고객 추가 기능 구현 및 관련 웹 페이지 구성\\r\\n비관적 락을 통한 동시성 문제 해결\\r\\nGrafana, Prometheus를 활용한 모니터링 환경 구성\\r\\nJenkins를 활용한 배포 자동화\\r\\n- 개선 경험 |\\r\\nPostgreSQL로 DB를 변경하여 반경 검색 쿼리 속도 93% 향상\\r\\n다수 데이터에 대해 JDBC Template을 사용해 저장 성능 57% 향상\\r\\nSpring의 프로토타입 빈을 활용한 파일 파싱 모듈 구조 개선].\\n지원자가 진행한 프로젝트에서 왜 이런 기술을 썼고, 어떤 문제를 경험했는지 등등 지원자 경험에 기반한 질문을 해줘.\\n마지막 핵심 주제를 파악하고 핵심 주제를 바꿔서 다른 주제로 질문해.'
        is too long - 'tools.0.function.description'",<EOL>    "type": "invalid_request_error",<EOL>    "param": null,<EOL>    "code": null<EOL>  }<EOL>}<EOL>" */
        InterviewSetting mockSetting = mock(InterviewSetting.class);

        // 1142자 불가능
        when(mockSetting.getConcept()).thenReturn("너는 IT 분야 백엔드 포지션의 면접관이야.\\\\n메시지가 길다고 마음대로 면접을 마무리하지마.\\\\n항상 단 하나의 질문만 해. 이미 대화한 주제를 질문하지마.\\\\n항상 3문장 이하로 응답해. 항상 마지막 문장으로 면접자를 평가할 수 있는 질문을 해.\\\\n지원자의 응답에 대한 평가는 간단하게. 질문은 자세하게 해.\\\\n\\\\nuser는 다음과 같은 경험을 함 [- 개요 |  ChatGPT와 모의 면접을 진행할 수 있는 서비스\\\\r\\\\n- 진행인원 |  **개인 프로젝트**\\\\r\\\\n- 핵심 기술 |  Java, Spring Boot, JPA, MySQL, Redis, Thymeleaf\\\\r\\\\n- 담당 내용 | \\\\r\\\\nRedis의 SETNX로 락을 구현하여 동시 요청 제어 기능 구현\\\\r\\\\nAI가 지원자에게 적합한 응답을 생성할 수 있는 프롬프트 생성 기능\\\\r\\\\nOpenAI API 요청 기능\\\\r\\\\n- 개선 경험 |\\\\r\\\\nStomp와 비동기 처리로 OpenAI API 연동 시 사용자 대기시간 89% 단축, - 개요 | 지도를 통해 고객 또는 장소를 조회, 관리하는 서비스\\\\r\\\\n- 진행 인원 | 디자이너 1, 모바일 2, **백엔드 2**\\\\r\\\\n- 핵심 기술 | Java, Spring Boot, JPA, PostgreSQL, Jenkins, Prometheus\\\\r\\\\n- 담당 내용 |\\\\r\\\\nSpring Data JPA를 활용한 고객 데이터 관리 기능\\\\r\\\\n현재 위치에서 반경 내의 고객 필터링 기능\\\\r\\\\n엑셀 파일을 이용한 고객 추가 기능 구현 및 관련 웹 페이지 구성\\\\r\\\\n비관적 락을 통한 동시성 문제 해결\\\\r\\\\nGrafana, Prometheus를 활용한 모니터링 환경 구성\\\\r\\\\nJenkins를 활용한 배포 자동화\\\\r\\\\n- 개선 경험 |\\\\r\\\\nPostgreSQL로 DB를 변경하여 반경 검색 쿼리 속도 93% 향상\\\\r\\\\n다수 데이터에 대해 JDBC Template을 사용해 저장 성능 57% 향상\\\\r\\\\nSpring의 프로토타입 빈을 활용한 파일 파싱 모듈 구조 개선].\\\\n지원자가 진행한 프로젝트에서 왜 이런 기술을 썼고, 어떤 문제를 경험했는지 등등 지원자 경험에 기반한 질문을 해줘.\\\\n마지막 핵심 주제를 파악하고 핵심 주제를 바꿔서 다른 주제로 질문해.");
        // 600자 가능
        when(mockSetting.getConcept()).thenReturn("너는 {IT} 분야 {백엔드} 포지션의 면접관. 항상 단 하나의 질문만 해. 항상 5문장 이하로 응답해. 항상 마지막 문장은 질문으로 끝내. 넌 지원자를 평가하는 면접관이라는 것을 명심해. 기술면접을 시작하자. 운영체제, 네트워크, 데이터베이스, 자료구조, 알고리즘 등에 대해 물어볼 건데 간단한 개념질문보다는 응용질문을 해봐. messages를 통해 이미 대화한 주제를 response로 반환하지마 예시 템플릿을 줄게 assistant:안녕하세요. 면접을 시작하겠습니다. 준비되셨나요?. user:네. 준비됐습니다. assistant:운영체제의 프로세스와 스레드의 차이점에 대해 설명해주세요. user:죄송합니다. 잘 모르겠습니다. assistant:영체제의 프로세스와 스레드는 모두 실행 단위입니다. 하지만 프로세스는 독립적인 메모리 공간을 가지고 실행되는 반면, 스레드는 프로세스 내에서 공유하는 메모리를 가지고 실행됩니다. 또한, 프로세스는 독립적으로 스케줄링되고 자원을 할당받지만, 스레드는 프로세스의 자원을 공유하여 실행됩니다.이제 제 질문입니다. 운영체제에서 프로세스 간 통신을 위해 사용되는 방법에는 어떤 것들이 있나요? 이런식으로 진행해. 템플릿을 그대로 사용하지마.");

        Message message = requester.sendRequest(new InterviewAIRequest(new ArrayList<>(), mockSetting));
        System.out.println(message);
    }
}