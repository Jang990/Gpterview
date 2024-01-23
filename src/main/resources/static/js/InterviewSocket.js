let stompClient = null;
let socket = null;
let interviewId = null;

$(document).ready(function () {
    interviewId = getInterviewId();
    if($('#talk-history').find('li').length === 0)
        waitAiResponse();
    initSocket();
});

function getInterviewId() {
    const currentUrl = window.location.pathname;
    return currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
}

function initSocket() {
    socket = new SockJS('/ws/interview');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/interview/' + interviewId, function (data) {
            console.log(data);
            clearInterval(decreaseInterval);
            setTimeout(function () {
                // speak(data.content); // 응답 메시지를 음성으로 전달
                removeWaitingPanel();
                displayResponse(JSON.parse(data.body).content);
                scroll();
                remainingTime = loadingTime;
                enableSendBtn();
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