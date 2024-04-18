function displayRecommendation(pairId, questions) {
    setCurrentConversationPairId(pairId);
    const component = createRecommendationComponent(questions);
    $('#talk-history').append(component);
}

function removeRecommendationComponent() {
    $('#talk-history .recommended-questions').remove();
}

function createRecommendationComponent(questions) {
    const prefix = `
        <li class="d-flex mb-3 justify-content-start recommended-questions">
            <div class="col-10">
                <div class="d-flex flex-column" style="min-width:0px;">
                    <div class="d-flex justify-content-center">
                        <p>질문을 선택해주세요.</p>
                    </div>
    `;

    let btnGroup = "";
    questions.forEach(qu => {
        btnGroup += createRecommendationBtn(qu);
    });

    const suffix = `
                    <div class="d-flex justify-content-center">
                        <button class="btn btn-primary me-2 btn-sm" onclick="retryRecommendation()">다른 추천</button>
                        <button class="btn btn-secondary btn-sm" onclick="requestAi()">AI 요청</button>
                    </div>
                </div>
            </div>
        </li>
    `

    return prefix + btnGroup + suffix;
}

function createRecommendationBtn(question) {
    return `
        <button class="btn btn-light btn-block mb-2 w-100 text-truncate" onclick="selectQuestion(${question.id}, this.getElementsByTagName('p')[0].innerText)">
            <p class="small mb-0">${question.content}</p>
        </button>
    `;
}

// 질문 선택
function selectQuestion(questionId, content) {
    const apiUri = createConversationPairUriPrefix() +"/question/connection/" + questionId
    sendRequest(apiUri, null);

    const pairId = getConversationPairId();
    removeRecommendationComponent();
    displayResponse(pairId, content);
    enableSendBtn();
}

// 다른 추천 부탁
function retryRecommendation() {
    const apiUri = createConversationPairUriPrefix() + "/recommendation";
//    removeRecommendationComponent();
    removeLastChatting();
    waitAiResponse();
    sendRequest(apiUri, null);
}

// AI 응답으로 변경
function requestAi() {
    const apiUri = createConversationPairUriPrefix() + "/recommendation/ai";
    removeRecommendationComponent();
    waitAiResponse();
    sendPatchRequest(apiUri, null);
}

