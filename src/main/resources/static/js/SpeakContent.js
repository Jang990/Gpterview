let recognition;
function startRecognition() {
    recognition = new window.webkitSpeechRecognition();

    recognition.onstart = function(event) {
        $('.my-answer-textarea').attr("placeholder", "음성 인식 중...");
    };

    recognition.onresult = function(event) {
        // 음성 인식 결과 나옴
        var transcript = event.results[0][0].transcript;
        const text = $('.my-answer-textarea').val() + transcript + "\n";
        $('.my-answer-textarea').val(text);
        $('.my-answer-textarea').attr("placeholder", "질문을 듣고 답변해주세요! Ctrl+Enter로 메시지 전송 가능합니다.");
    };
    recognition.start();
}

function speak(text) {
    var msg = new SpeechSynthesisUtterance(text);
    msg.voice = window.speechSynthesis.getVoices()[0]; // 첫번째 목소리로 설정
    msg.rate = 1.5; // 속도 설정 (기본값은 1)
    window.speechSynthesis.speak(msg);
}