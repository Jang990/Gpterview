function review(messageId) {
    console.log(interviewId + "," + messageId);
    $.ajax({
            type: 'POST',
            url: `/api/interview/${interviewId}/conversation/${messageId}/review`,
            contentType: 'application/json',
            success: function(data) {
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
                console.log(errorMessage);
            }
        });
}