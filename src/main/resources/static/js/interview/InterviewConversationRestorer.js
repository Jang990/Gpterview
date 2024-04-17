function isEmptyHistory() {
    const historyElement = $('#talk-history').find('li');
    return historyElement.length === 0 ||
        (historyElement.length === 1 && historyElement.hasClass('waiting-element'));
}

function restoreHistory(interviewId, conversationId) {
    waitAiResponse();
    request(interviewId, conversationId, 0);
}

// AJAX 요청을 보내는 함수
function request(interviewId, conversationId, retryCount) {
    $.ajax({
        url: `/api/interview/${interviewId}/conversation/pair/${conversationId}`,
        method: 'GET',
        success: function(response) {
            // AJAX 요청이 성공한 경우 처리할 로직 추가
            if(!isEmptyHistory())
                return;
            console.log(response);
            const pairId = response.pair.id;
            const questionContent = response.question.content;

            if(response.pair.status === "RESTART_RECOMMENDED") {
                removeWaitingPanel();
                retryRecommendation();
                return;
            }

            if(pairId === null || questionContent === null)
                retryOrExit(interviewId, conversationId, retryCount);

            removeWaitingPanel();
            displayResponse(pairId, questionContent);
            enableSendBtn();
            scroll();
        },
        error: function(xhr, status, error) { } // 오류 처리 로직 필요.
    });
}

// 요청 재시도 또는 종료하는 함수
function retryOrExit(interviewId, conversationId, retryCount) {
    if (retryCount >= 3) { // 최대 3번 시도
        exit(); // 3번의 재시도 후에도 결과가 없으면 종료
        return;
    }
    setTimeout(function() {
        request(interviewId, conversationId, retryCount + 1); // 재요청
    }, 3000); // 3초 후에 재시도
}

// 종료하는 함수
function exit() {
    removeWaitingPanel();
    displayResponse(null, "서버에 오류가 발생했습니다. 잠시 후 재시도 해주세요.");
    enableSendBtn();
    scroll();
}