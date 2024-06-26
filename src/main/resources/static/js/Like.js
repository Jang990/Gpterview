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
                increaseLike(questionId, element);
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

function cancelQuestionLike(questionId, element) {
    $.ajax({
                type: 'DELETE',
                url: createUrl(questionId),
                contentType: 'application/json',
                success: function(data) {
                    decreaseLike(questionId, element);
                    showQuestionLikeToast('추천 취소!');
                },
                error: function(error) {
                    console.log(error);
                    if(error.status == 404) {
                        showQuestionLikeToast('추천되지 않은 질문입니다.');
                        return;
                    }
                }
        });
}

function increaseLike(questionId, element) {
    // 버튼 텍스트 업데이트
    var iconElement = element.querySelector('i');
    var currentText = iconElement.textContent.trim();
    var likes = parseInt(currentText.split(' ')[0]); // "1 Likes"에서 숫자 부분 추출
    likes += 1; // 좋아요 수 증가
    iconElement.textContent = ` ${likes} Likes`; // 기존 아이콘 요소의 텍스트 업데이트

    // 아이콘 클래스 변경
    iconElement.classList.remove('far'); // 기존 빈 아이콘 클래스 제거
    iconElement.classList.add('fas'); // 꽉 찬 아이콘 클래스 추가

    // onClick 이벤트 변경
    element.setAttribute('onclick', `cancelQuestionLike(${questionId}, this)`);
}

function decreaseLike(questionId, element) {
    // 버튼 텍스트 업데이트
    var iconElement = element.querySelector('i');
    var currentText = iconElement.textContent.trim();
    var likes = parseInt(currentText.split(' ')[0]); // "1 Likes"에서 숫자 부분 추출
    likes -= 1; // 좋아요 수 감소
    iconElement.textContent = ` ${likes} Likes`; // 기존 아이콘 요소의 텍스트 업데이트

    // 아이콘 클래스 변경
    iconElement.classList.remove('fas'); // 기존 꽉 찬 아이콘 클래스 제거
    iconElement.classList.add('far'); // 빈 아이콘 클래스 추가

    // onClick 이벤트 변경
    element.setAttribute('onclick', `likeQuestion(${questionId}, this)`);
}