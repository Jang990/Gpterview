// 요청을 보내기 전 응답대기 화면 표시
function waitAiResponse() {
    disableSendBtn(); // 전송버튼 잠시 비활성화
    displayWaitingPanel(); // 응답 대기 화면 출력.
    scroll(); // 채팅 스크롤 밑으로 내리기.
    decreaseInterval = setInterval(decreaseRemainingTime, 100);
}

// 응답을 최소 1.5초의 뒤에 보여주기 위한 함수
function decreaseRemainingTime() {
    if(remainingTime <= 0){
        clearInterval(decreaseInterval);
        return;
    }
    remainingTime -= 100;
}

// waiting 패널 지우기
function removeWaitingPanel() {
    clearInterval(decreaseInterval);
    $('#talk-history .temporary').remove();
}

// waiting 패널 화면 표시
function displayWaitingPanel(data) {
    const randomMessage = createRandomMessage();
    const waitingPanel = createWaitingPanel(randomMessage);
    $('#talk-history').append(waitingPanel);
}

function createWaitingPanel(msg) {
// 로딩 이미지 출처 - https://icons8.com/preloaders/
    return `
            <li class="mb-3 d-flex justify-content-start temporary">
                <div class="d-flex flex-row justify-content-start col-10" style="min-width:0px;">
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