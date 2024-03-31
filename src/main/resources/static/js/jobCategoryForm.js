let categoryOptionToast, categoryOptionForm, customcategory;
let fieldOptionForm, fieldOptionToast, customField;

$(function(){
      // 화면 로딩 시
      categoryOptionForm = $('#category');
      categoryOptionToast = new bootstrap.Toast(document.getElementById('categoryOptionToast'));
      customcategory = $('#customcategory');

      fieldOptionForm = $('#field');
      fieldOptionToast = new bootstrap.Toast(document.getElementById('fieldOptionToast'));
      customField = $('#customField');

      categoryOptionForm.change(function() {
        loadPosition(this);
      });

      if($('#loadedPosition').val() !== "" && typeof $('#loadedPosition').val() !== "undefined") {
        loadProfileField();
      }
});

// loadedPosition 함수 정의
function loadProfileField() {
    const loadedPositionValue = $('#loadedPosition').val();

    $.get(`/api/job/category/${loadedPositionValue}`, function(data) {
            $('#category option:selected').prop('selected', false);
            $('#category').prepend($('<option>', {
                value: data.category.id,
                text: data.category.name
            })).prop('selected', true);

            $('#field option:selected').prop('selected', false);
            $('#field').prepend($('<option>', {
                value: data.field.id,
                text: data.field.name
            })).prop('selected', true);

        });

    $.get(`/api/job/category/${loadedPositionValue}`, function(data) {
        $('#category option:selected').prop('selected', false);
        $('#category').prepend($('<option>', {
            value: data.category.id,
            text: data.category.name
        })).prop('selected', true);

        $('#field option:selected').prop('selected', false);
        $('#field').prepend($('<option>', {
            value: data.field.id,
            text: data.field.name
        })).prop('selected', true);

    });
}


function showcategoryToast() {
    //오류
    console.log(categoryOptionToast);
    categoryOptionToast.show();
}

function addcategory() {
    const optionElement = createOption(customcategory.val());
    categoryOptionForm.children().last().before(optionElement);
    customcategory.val('');

    fieldOptionForm.empty();
    fieldOptionForm.append(optionElement);

    categoryOptionToast.hide();
}

function showFieldToast() {
    fieldOptionToast.show();
}

function addField() {
    const optionElement = createOption(customField.val());
    fieldOptionForm.append(optionElement);
    customField.val('');
    fieldOptionToast.hide();
}

function createOption(value) {
    // value와 text를 분리할 것.
    return `<option value="${value}" selected>${value}</option>`;
}

// 밑은 loading 관련 부분
function loadPosition(categoryElement) {
    $.ajax({
        type: 'GET',
        url: `/api/category/${categoryElement.value}/position`,
        contentType: 'application/json',
//        data: JSON.stringify(interviewInfo),
        success: function(data) {
            changeFieldOptions(data);
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function changeFieldOptions(fields) {
    fieldOptionForm.empty();
    $.each(fields, function(index, value) {
        fieldOptionForm.append($('<option>', {
            value: value.id,
            text: value.name
        }));
    });
}

