let stompClient = null;
let socket = null;

function initSocket(interviewId) {
    socket = new SockJS('/ws/interview');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/interview/' + interviewId, function (data) {
            console.log(data);
            const response = JSON.parse(data.body);
            const pairId = response.conversationPairId;
            const questions = response.question;

            setTimeout(function () {
                // speak(data.content); // 응답 메시지를 음성으로 전달
                removeWaitingPanel();

                if(questions.length == 1) {
                    displayResponse(pairId, questions[0].content);
                    enableSendBtn();
                }
                else if(questions.length > 1) {
                    displayRecommendation(pairId, questions);
                }

                scroll();
                remainingTime = loadingTime;
            }, remainingTime);
        });
    });

    // 에러가 발생했을 때 실행되는 콜백 함수
    socket.onerror = function (error) {
        console.error('WebSocket 오류:', error);
    };

    // 연결이 닫혔을 때 실행되는 콜백 함수
    socket.onclose = function (event) {
        console.log('WebSocket 연결이 닫혔습니다.');
    };

    // 페이지를 떠날 때 WebSocket 연결을 닫음
    window.onbeforeunload = function () {
        stompClient.disconnect();
    };

}