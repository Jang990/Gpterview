function createConversationPairUriPrefix(conversationId) {
    return "/api/interview/" + getInterviewId() + "/conversation/pair/" + conversationId;
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
    if($('#talk-history').find('li').length === 0)
        waitAiResponse();
    initSocket();
    scroll();
});

// 응답을 최소 1.5초의 뒤에 보여주기 위한 함수
function decreaseRemainingTime() {
    if(remainingTime <= 0){
        clearInterval(decreaseInterval);
        return;
    }

    remainingTime -= 100;
}

// 현재 내용을 토대로 응답을 다시 받음.
function retryResponse() {
    removeLastChatting();
    const conversationPairId = getConversationPairId();
    sendRequest(createConversationPairUriPrefix(conversationPairId)+"/changing-topic", null);
}

// 마지막 채팅 제거
function removeLastChatting() {
    var lastChatting = $("#talk-history li:last");

    if (lastChatting.length > 0) {
        lastChatting.remove();
    }
}

// 재전송 버튼 지우기
function removeRetryingBtn() {
    // retryBtn을 가지고 있는 Div 찾기
    const retryBtnDiv = $("#retryBtnDiv");

    // retryBtn이 존재하는 경우에만 실행
    if (retryBtnDiv.length > 0) {
        // 버튼 제거에 따라 버튼 부모의 너비를 10 -> 8로 변경
        var col10Div = retryBtnDiv.closest(".col-10");
        col10Div.removeClass("col-10").addClass("col-8");

        // retryBtn 제거
        retryBtnDiv.remove();
    }
}


// 서버에서 받는 Message 형식을 맞출 것.
function createMessage(role, message) {
    return ({"role": role, "content": message});
}

// 응답완료 버튼 클릭시 동작 - sendBtn에 disabled 여부에 따라 함수 실행여부가 결정된다.
function sendMessage() {
    if(sendBtn.prop("disabled"))
        return;

    const nowMsg = $('#result').val(); // 현재 사용자 작성 답변 가져오기.
    if(isInvalidChattingMessage(nowMsg)) {
        showChattingErrorMessage(contentMinLength + '자 이상 ' + contentMaxLength + '자 이하 입력해주세요.');
        return;
    }

    $('#result').val(''); // 사용자 응답창 지워주기
    removeRetryingBtn(); // 다른 질문을 요청할 수 없도록 버튼 제거

    const newConversationItem = createUserMessage(nowMsg);
    $('#talk-history').append(newConversationItem); // 화면에 UserMessage 추가해주기
    scroll();

    const message = createMessage("USER", nowMsg); // TODO: USER를 타임리프로 변경
    const conversationPairId = getConversationPairId();
    sendRequest(createConversationPairUriPrefix(conversationPairId)+"/answer", message);
}

function isInvalidChattingMessage(message) {
    return message.length < contentMinLength || contentMaxLength < message.length;
}

function disableSendBtn() {
    sendBtn.prop("disabled", true);
}

function enableSendBtn() {
    sendBtn.prop("disabled", false);
}

function waitAiResponse() {
    disableSendBtn(); // 전송버튼 잠시 비활성화
    displayWaitingPanel(); // 응답 대기 화면 출력.
    scroll(); // 채팅 스크롤 밑으로 내리기.
    decreaseInterval = setInterval(decreaseRemainingTime, 100);
}

// 서버로 요청보내기.
function sendRequest(requestURL, message) {
    console.log(message);
    waitAiResponse();

    $.ajax({
        type: 'POST',
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

function showChattingErrorMessage(errorMessage) {
    // console.log(errorMessage);
    errorMessageField.text(errorMessage);
    chattingErrorToast.show();
}

function displayWaitingPanel(data) {
    const randomMessage = createRandomMessage();
    const waitingPanel = createWaitingPanel(randomMessage);
    $('#talk-history').append(waitingPanel);
}

function createRandomMessage() {
    const waitingMessages = [
        "머리를 굴리는중...",
        "잠시 커피를 마시는 중...",
        "질문을 고민 중...",
        "이력서를 확인하는 중...",
        "미소 짓는 중...",
        "잠시 물을 마시는 중...",
        "종이에 무언가를 체크 중...",
        "생각을 정리하는 중...",
        "미간을 찌푸리는 중..."
    ];

    return waitingMessages[Math.floor(Math.random() * waitingMessages.length)];
}

// 서버로 받은 응답 값을 화면에 면접관으로 표시
function displayResponse(msgObj) {
    const newConversationItem = createGptMessage(msgObj.content);
    $('#talk-history').append(newConversationItem);
    setCurrentConversationPairId(msgObj.conversationPairId);
    scroll();
}

// waiting 패널 지우기
function removeWaitingPanel() {
    $('#talk-history .temporary').remove();
}

function getCurrentTime() {
    var now = new Date();
    var hours = now.getHours().toString().padStart(2, '0');
    var minutes = now.getMinutes().toString().padStart(2, '0');
    return hours + ':' + minutes;
}

function createWaitingPanel(msg) {
// 로딩 이미지 출처 - https://icons8.com/preloaders/
    return `
            <li class="mb-3 d-flex justify-content-start temporary">
                <div class="d-flex flex-row justify-content-start col-8" style="min-width:0px;">
                    <img src="../image/interviewer.png" alt="면접관 사진" style="width: 50px; height: 50px;">
                    <div class="p-3 ms-3" style="border-radius: 15px; background-color: rgba(57, 192, 237,.2); min-width:0px;">
                        <div class="bubblingG">
                            <span id="bubblingG_1"></span>
                            <span id="bubblingG_2"></span>
                            <span id="bubblingG_3"></span>
                        </div>
                        <p class="ps-2 mb-0 content" name="content">${msg}</p>
                    </div>
                </div>
            </li>`;
}

function createUserMessage(msg) {
    return `
           <li class="mb-3 d-flex justify-content-end">
                <div class="d-flex flex-row justify-content-end col-8" style="min-width:0px;">
                    <div class="p-3 me-3 border" style="border-radius: 15px; background-color: #fbfbfb; min-width:0px;">
                        <input type="hidden" name="role" class="role" value="USER">
                        <p class="small mb-0 content" name="content">${msg}</p>
                    </div>
                    <img src="../image/applicant.png" alt="avatar"
                     class="rounded-circle d-flex align-self-start shadow-1-strong"
                     style="width: 50px; height: 50px;">
                </div>
           </li>`;
}

function createGptMessage(msg) {
    return `
            <li class="mb-3 d-flex justify-content-start">
                <div class="d-flex flex-row justify-content-start col-10" style="min-width:0px;">
                    <img src="../image/interviewer.png" alt="면접관 사진" style="width: 50px; height: 50px;">
                    <div class="p-3 ms-3" style="border-radius: 15px; background-color: rgba(57, 192, 237,.2); min-width:0px;">
                        <input type="hidden" name="role" class="role" value="INTERVIEWER">
                        <p class="small mb-0 content" name="content">${msg}</p>
                    </div>
                    <div class="d-flex align-items-end col-2 ps-2" style="min-width:0px;" id="retryBtnDiv">
                        <button id="retryBtn" class="btn btn-primary btn-sm" onclick="retryResponse()">
                            <p class="small mb-0">다른 질문</p>
                        </button>
                    </div>
                    <div class="d-flex align-items-end col-2" style="min-width:0px;" id="reviewBtnDiv">
                        <button id="reviewBtn" class="btn btn-link btn-sm">
                            <img style="height: 2vh" th:onclick="|review(${msg.id})|" src="../image/review.png" alt="다시보기">
                        </button>
                    </div>
                </div>
            </li>`;
}

function scroll() {
    const talkHistoryArticle = $('#talk-card');
    talkHistoryArticle.scrollTop(talkHistoryArticle[0].scrollHeight);
}


// result TextArea에서 Ctrl+Enter 입력시 메시지를 보내는 기능
const taResult = $("#result");
taResult.on("keydown", function (event) {
    // Ctrl (또는 Command on Mac) 키와 Enter 키를 동시에 눌렀을 때
    if ((event.ctrlKey || event.metaKey) && event.key === "Enter") {
        sendMessage();
    }
});