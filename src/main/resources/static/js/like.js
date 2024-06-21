const likeErrorToastElement = document.getElementById('likeErrorToast');
const likeErrorMessageField = document.getElementById('likeErrorMessageField');

const likeErrorToast = new bootstrap.Toast(likeErrorToastElement);
function createUrl(questionId) {
    return "/api/question/" + questionId + "/like";
}

function requiredLoginForLike() {
    showQuestionLikeToast('질문을 추천하려면 로그인이 필요합니다.');
}

function showQuestionLikeToast(content) {
    likeErrorToastElement.style.display = 'block';
    likeErrorMessageField.textContent = content;
    likeErrorToast.show();
    likeErrorToast.addEventListener('hidden.bs.toast', function () {
        likeErrorToastElement.style.display = 'none';
    })
}

function likeQuestion(questionId, element) {
    $.ajax({
            type: 'POST',
            url: createUrl(questionId),
            contentType: 'application/json',
            success: function(data) {
                increaseLike(element);
                showQuestionLikeToast('추천 완료!');
            },
            error: function(error) {
                console.log(error);
                if(error.status == 409) {
                    showQuestionLikeToast('이미 추천한 질문입니다.');
                    return;
                }
            }
    });
}

function increaseLike(element) {
    // 버튼 텍스트 업데이트
    var iconElement = element.querySelector('i');
    var currentText = iconElement.textContent.trim();
    var likes = parseInt(currentText.split(' ')[0]); // "1 Likes"에서 숫자 부분 추출
    likes += 1; // 좋아요 수 증가
    iconElement.textContent = ` ${likes} Likes`; // 기존 아이콘 요소의 텍스트 업데이트
    iconElement.classList.add('fas', 'fa-thumbs-up', 'me-1'); // 아이콘 클래스 유지
}