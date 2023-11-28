var recognition;
var resultArea = document.getElementById("result").setAttribute("disabled", "");

function startRecognition() {
    recognition = new window.webkitSpeechRecognition();
    recognition.onend = function() {
//        document.getElementById("result").removeAttribute("disabled");
    };

    recognition.onresult = function(event) {
        var transcript = event.results[0][0].transcript;
        document.getElementById("result").innerHTML += transcript + "\n";
    };
    recognition.start();

}

        // ==============

$(document).ready(function() {
  $("#sendBtn").on("click", function() {
    getText();
  });
  // voiceschanged 이벤트 핸들러 등록
  //window.speechSynthesis.onvoiceschanged = function() {
  //  var voices = window.speechSynthesis.getVoices();
  //  console.log(voices);
  //};
});

function getText() {
  var clientTalk = {"clientTalk": $('#result').val()};
  $.ajax({
    url: "/talk-test",
    type: "POST",
    contentType: "application/json",
    //data: $("#result").val(),
    data: JSON.stringify(clientTalk),
    success: function(result) {
      console.log(result);
      var text = result;
      speak(text);
    },
    error: function(error) {
      console.log(error);
    }
  });
}

function speak(text) {
  var msg = new SpeechSynthesisUtterance(text);
  msg.voice = window.speechSynthesis.getVoices()[0]; // 첫번째 목소리로 설정
  msg.rate = 1.5; // 속도 설정 (기본값은 1)
  window.speechSynthesis.speak(msg);
}

function sendMessage() {
    var talkHistory = {
        message: $('#result').val(),
    };
    $.ajax({
        type: 'POST',
        url: '/chat',
        contentType: 'application/json',
        data: JSON.stringify(talkHistory),
        success: function(data) {
            // message
            console.log(talkHistory);
            console.log(data);
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