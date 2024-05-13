let recognition;
let recognitionControlBtn;
let isListening;
$(document).ready(function () {
    isListening = false;
    recognitionControlBtn = $('#speckBtn');
});
function startRecognition() {
    recognition = new window.webkitSpeechRecognition();

    recognition.onstart = function(event) {
        $('.my-answer-textarea').attr("placeholder", "음성 인식 중...");
    };

    recognition.onend = function() {
        if(isListening)
            recognition.start();
    };

    recognition.onresult = function(event) {
        // 음성 인식 결과 나옴
        var transcript = event.results[0][0].transcript;
        const text = $('.my-answer-textarea').val() + transcript + " ";
        $('.my-answer-textarea').val(text);
    };

    isListening = true;
    recognition.start();
    recognitionControlBtn.text("중지");
    recognitionControlBtn.removeClass("btn-success").addClass("btn-danger");
    recognitionControlBtn.attr('onclick', 'stopRecognition()');
}

function stopRecognition() {
    isListening = false;
    recognition.stop();
    recognitionControlBtn.attr('onclick', 'startRecognition()');
    recognitionControlBtn.text("말하기");
    recognitionControlBtn.removeClass("btn-danger").addClass("btn-success");
    $('.my-answer-textarea').attr("placeholder", "질문을 듣고 답변해주세요! Ctrl+Enter로 메시지 전송 가능합니다.");
}

function speak(text) {
    var msg = new SpeechSynthesisUtterance(text);
    msg.voice = window.speechSynthesis.getVoices()[0]; // 첫번째 목소리로 설정
    msg.rate = 1.5; // 속도 설정 (기본값은 1)
    window.speechSynthesis.speak(msg);
}