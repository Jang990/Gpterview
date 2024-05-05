function createConversationPairUriPrefix() {
    return "api/interview/" + getInterviewId() + "/conversation/pair/" + getConversationPairId();
}

const loadingTime = 1500;
const sendBtn = $('.sendBtn');
const errorMessageField = $('#errorMessageField');
const contentMinLength = 3;
const contentMaxLength = 200;
let chattingErrorToast = new bootstrap.Toast(document.getElementById('chattingErrorToast'));

let remainingTime = loadingTime;
let decreaseInterval;


$(document).ready(function () {
    const interviewId = getInterviewId();
    const conversationId = getConversationPairId();
    initSocket(interviewId);
    if(isEmptyHistory()) {
        restoreHistory(interviewId, conversationId);
    }
    scroll();
});

function getInterviewId() {
    const currentUrl = window.location.pathname;
    return currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
}

// 마지막 채팅 제거
function removeLastChatting() {
    var lastChatting = $("#talk-history li:last");

    if (lastChatting.length > 0) {
        lastChatting.remove();
    }
}

// 서버로 요청보내기.
function sendRequest(requestURL, message) {
    sendMethodRequest(requestURL, 'POST', message);
}

function sendPatchRequest(requestURL, message) {
    sendMethodRequest(requestURL, 'PATCH', message);
}

// 서버로 요청보내기.
function sendMethodRequest(requestURL, method, message) {
    console.log(message);
    console.log(requestURL);

    $.ajax({
        type: method,
        url: requestURL,
        contentType: 'application/json',
        data: JSON.stringify(message),
        success: function(data) {

        },
        error: function(error) {
            console.log(error);
            let errorMessage;
            if(error.status == 400) {
                showChattingErrorMessage(error.responseJSON.errorMessage);
                enableSendBtn();
                return;
            }
            if(error.status == 413) {
                disableSendBtn();
                removeWaitingPanel();
                displayResponse("수고하셨습니다. 면접이 종료되었습니다.");
                removeRetryingBtn();
                return;
            }

            if(error.status == 401)
                errorMessage = '로그인을 다시 진행한 후 시도해주세요.';
            else
                errorMessage = error.responseJSON.message;
            $('#creationGroupError').text(errorMessage);
            enableSendBtn();
            console.log(errorMessage);
        }
    });
}

function getCurrentTime() {
    var now = new Date();
    var hours = now.getHours().toString().padStart(2, '0');
    var minutes = now.getMinutes().toString().padStart(2, '0');
    return hours + ':' + minutes;
}

// 하단으로 스크롤 이동
function scroll() {
    const talkHistoryArticle = $('#talk-card');
    talkHistoryArticle.scrollTop(talkHistoryArticle[0].scrollHeight);
}