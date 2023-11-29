var recognition;

function startRecognition() {
    recognition = new window.webkitSpeechRecognition();

    recognition.onstart = function(event) {
        $('#result').attr("placeholder", "음성 인식 중...");
    };

    recognition.onresult = function(event) {
        // 음성 인식 결과 나옴
        var transcript = event.results[0][0].transcript;
        const text = $('#result').val() + transcript + "\n";
        $('#result').val(text);
        $('#result').attr("placeholder", "질문을 듣고 '말하기' 버튼을 눌러서 대답해주세요!");
    };
    recognition.start();
}

function speak(text) {
  var msg = new SpeechSynthesisUtterance(text);
  msg.voice = window.speechSynthesis.getVoices()[0]; // 첫번째 목소리로 설정
  msg.rate = 1.5; // 속도 설정 (기본값은 1)
  window.speechSynthesis.speak(msg);
}

function sendMessage() {
    const nowMsg = $('#result').val();
    var talkHistory = {
        message: nowMsg,
    };
    $.ajax({
        type: 'POST',
        url: '/chat',
        contentType: 'application/json',
        data: JSON.stringify(talkHistory),
        beforeSend: function() {
            const newConversationItem = createUserMessage(nowMsg);
            $('#talk-history').append(newConversationItem);
            $('#result').val('');
        },
        success: function(data) {
            console.log(talkHistory);
            console.log(data);
            speak(data);
            const newConversationItem = createGptMessage(data);
            $('#talk-history').append(newConversationItem);
        },
        error: function(error) {
            let errorMessage;
            if(error.status == 400)
                errorMessage = error.responseJSON[0].message;
            else if(error.status == 401)
                errorMessage = '로그인을 다시 진행한 후 시도해주세요.';
            else
                errorMessage = error.responseJSON.message;
            $('#creationGroupError').text(errorMessage);
        }
    });
}

function getCurrentTime() {
    var now = new Date();
    var hours = now.getHours().toString().padStart(2, '0');
    var minutes = now.getMinutes().toString().padStart(2, '0');
    return hours + ':' + minutes;
}

function createUserMessage(msg) {
    return `
           <li class="d-flex justify-content-start mb-4">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Avatars/avatar-6.webp" alt="avatar"
                     class="rounded-circle d-flex align-self-start me-3 shadow-1-strong" width="60">
                <div class="card w-100">
                    <div class="card-header d-flex justify-content-between p-3">
                        <p class="fw-bold mb-0">지원자</p>
                        <p class="text-muted small mb-0"><i class="far fa-clock"></i> ${getCurrentTime()}</p>
                    </div>
                    <div class="card-body">
                        <p class="mb-0">${msg}</p>
                    </div>
                </div>
            </li>`;
}

function createGptMessage(msg) {
    return `
            <li class="d-flex justify-content-end mb-4">
                <div class="card w-100">
                    <div class="card-header d-flex justify-content-between p-3">
                        <p class="fw-bold mb-0">면접관</p>
                        <p class="text-muted small mb-0"><i class="far fa-clock"></i> ${getCurrentTime()}</p>
                    </div>
                    <div class="card-body">
                        <p class="mb-0">${msg}</p>
                    </div>
                </div>
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Avatars/avatar-5.webp" alt="avatar"
                     class="rounded-circle d-flex align-self-start ms-3 shadow-1-strong" width="60">
            </li>`;
}