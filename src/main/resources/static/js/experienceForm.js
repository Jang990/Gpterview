let experienceForm;

$(function(){
      // 화면 로딩 시
      experienceForm = $('#experienceForm');
});

function addExperienceField() {
    const experienceTextField = createExperienceTextField();
    experienceForm.prepend(experienceTextField);
}


function createExperienceTextField() {
    return `<div>
                <div class="my-3 col">
                    <div for="intro" class="form-floating">
                        <textarea class="form-control" placeholder="여기에 적어주세요." name="experiences" style="height: 200px"></textarea>
                        <label>직무 관련 경험(프로젝트) (300자 이하) - 생략가능</label>
                    </div>
                </div>
            </div>`;
}
