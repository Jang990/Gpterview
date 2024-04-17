// 서버로 받은 응답 값을 화면에 면접관으로 표시
function displayResponse(pairId, content) {
    setCurrentConversationPairId(pairId);
    const newConversationItem = createGptMessage(content);
    $('#talk-history').append(newConversationItem);
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
                        <button id="retryBtn" class="btn btn-primary btn-sm" onclick="retryRecommendation()">
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