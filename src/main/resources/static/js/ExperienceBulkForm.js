let experienceForm;

$(function(){
      // 화면 로딩 시
      experienceForm = $('#experienceBulkForm');
});

function addExperienceField() {
    const experienceTextField = createExperienceTextField();
    experienceForm.prepend(experienceTextField);
}


function createExperienceTextField() {
    return `<div>
                <div class="my-3 col">
                    <div for="intro" class="form-floating">
                        <textarea class="form-control" placeholder="여기에 적어주세요." name="experience" style="height: 200px"></textarea>
                        <label>자소서 or 이력서 내용 (500자 이하)</label>
                    </div>
                </div>
            </div>`;
}
