// 응답완료 버튼 클릭시 동작 - sendBtn에 disabled 여부에 따라 함수 실행여부가 결정된다.
function sendMessage() {
    if(sendBtn.prop("disabled"))
        return;

    const nowMsg = $('#result').val(); // 현재 사용자 작성 답변 가져오기.
    if(isInvalidChattingMessage(nowMsg)) {
        showChattingErrorMessage(contentMinLength + '자 이상 ' + contentMaxLength + '자 이하 입력해주세요.');
        return;
    }

    removeRetryingBtn(); // 다른 질문을 요청할 수 없도록 버튼 제거
    displayAnswer(nowMsg);
    waitAiResponse();
    const message = createMessage("USER", nowMsg);
    sendRequest(createConversationPairUriPrefix()+"/answer", message);
}

// 서버에서 받는 Message 형식을 맞출 것.
function createMessage(role, message) {
    return ({"role": role, "content": message});
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

// 사용자 입력 오류 화면 표시
function showChattingErrorMessage(errorMessage) {
    // console.log(errorMessage);
    errorMessageField.text(errorMessage);
    chattingErrorToast.show();
}

// 전송버튼 비활성화
function disableSendBtn() {
    sendBtn.prop("disabled", true);
}

// 전송버튼 활성화
function enableSendBtn() {
    sendBtn.prop("disabled", false);
}

function isInvalidChattingMessage(message) {
    return message.length < contentMinLength || contentMaxLength < message.length;
}

// result TextArea에서 Ctrl+Enter 입력시 메시지를 보내는 기능
const taResult = $("#result");
taResult.on("keydown", function (event) {
    // Ctrl (또는 Command on Mac) 키와 Enter 키를 동시에 눌렀을 때
    if ((event.ctrlKey || event.metaKey) && event.key === "Enter") {
        sendMessage();
    }
});