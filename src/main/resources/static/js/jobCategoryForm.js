let categoryOptionToast, categoryOptionForm, customCategory;
let positionOptionForm, positionOptionToast, customPosition;

$(function(){
      // 화면 로딩 시
      categoryOptionForm = $('#categoryId');
      categoryOptionToast = new bootstrap.Toast(document.getElementById('categoryOptionToast'));
      customCategory = $('#customCategory');

      positionOptionForm = $('#positionId');
      positionOptionToast = new bootstrap.Toast(document.getElementById('positionOptionToast'));
      customPosition = $('#customPosition');

      categoryOptionForm.change(function() {
        loadPosition(this.value);
      });
});


function showCategoryToast() {
    //오류
    console.log(categoryOptionToast);
    categoryOptionToast.show();
}

function addCategory() {
    const optionElement = createOption(customCategory.val());
    categoryOptionForm.children().last().before(optionElement);
    customCategory.val('');

    positionOptionForm.empty();
    positionOptionForm.append(optionElement);

    categoryOptionToast.hide();
}

function showPositionToast() {
    positionOptionToast.show();
}

function addPosition() {
    const optionElement = createOption(customPosition.val());
    positionOptionForm.append(optionElement);
    customPosition.val('');
    positionOptionToast.hide();
}

function createOption(value) {
    // value와 text를 분리할 것.
    return `<option value="${value}" selected>${value}</option>`;
}

// 밑은 loading 관련 부분
function loadPosition(categoryId) {
    $.ajax({
        type: 'GET',
        url: `/api/category/${categoryId}/position`,
        contentType: 'application/json',
        success: function(data) {
            changePositionOptions(data);
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function changePositionOptions(positions) {
    positionOptionForm.empty();
    $.each(positions, function(index, value) {
        positionOptionForm.append($('<option>', {
            value: value.id,
            text: value.name
        }));
    });
}

