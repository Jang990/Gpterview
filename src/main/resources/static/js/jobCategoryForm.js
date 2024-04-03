let categoryOptionToast, categoryOptionForm, customCategory;
let positionOptionForm, positionOptionToast, customPosition;
const CREATE_CATEGORY_API = '/api/category';

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
    const categoryName = customCategory.val();
    const response = requestCategory(categoryName);
}

function showPositionToast() {
    positionOptionToast.show();
}

function addPosition() {
    const selectedCategoryId = getSelectedCategoryId();
    console.log(selectedCategoryId);
    const positionName = customPosition.val();

    requestPosition(selectedCategoryId, positionName);
}

function getSelectedCategoryId() {
    return categoryOptionForm.find('option:selected').val();
}

function createOption(response) {
    // value와 text를 분리할 것.
    return `<option value="${response.id}" selected>${response.name}</option>`;
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

// 카테고리 생성
function requestCategory(categoryName) {
    $.ajax({
        type: 'POST',
        url: CREATE_CATEGORY_API,
        data: JSON.stringify({"name": categoryName}),
        contentType: 'application/json',
        success: function(data) {
            const optionElement = createOption({"id": data, "name": categoryName});
            categoryOptionForm.children().last().before(optionElement);
            customCategory.val('');

            positionOptionForm.empty();
            categoryOptionToast.hide();

            loadPosition(data);
        },
        error: function(error) {
            console.log(error);
        }
    });
}

//Position 생성
function requestPosition(categoryId, positionName) {
    $.ajax({
        type: 'POST',
        url: `/api/category/${categoryId}/position`,
        data: JSON.stringify({"name": positionName}),
        contentType: 'application/json',
        success: function(data) {
            const optionElement = createOption({"id": data, "name": positionName});
            positionOptionForm.append(optionElement);
            customPosition.val('');
            positionOptionToast.hide();
        },
        error: function(error) {
            console.log(error);
        }
    });
}

