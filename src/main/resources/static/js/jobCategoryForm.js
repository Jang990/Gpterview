let departmentOptionToast, departmentOptionForm, customDepartment;
let fieldOptionForm, fieldOptionToast, customField;

$(function(){
      // 화면 로딩 시
      departmentOptionForm = $('#department');
      departmentOptionToast = new bootstrap.Toast(document.getElementById('departmentOptionToast'));
      customDepartment = $('#customDepartment');

      fieldOptionForm = $('#field');
      fieldOptionToast = new bootstrap.Toast(document.getElementById('fieldOptionToast'));
      customField = $('#customField');

      departmentOptionForm.change(function() {
        loadField(this);
      });

      if($('#loadField').val() !== "" && typeof $('#loadField').val() !== "undefined") {
        loadProfileField();
      }
});

// loadField 함수 정의
function loadProfileField() {
    const loadFieldValue = $('#loadField').val();
    $.get(`/api/job/category/field/${loadFieldValue}`, function(data) {
        $('#department option:selected').prop('selected', false);
        $('#department').prepend($('<option>', {
            value: data.department.id,
            text: data.department.name
        })).prop('selected', true);

        $('#field option:selected').prop('selected', false);
        $('#field').prepend($('<option>', {
            value: data.field.id,
            text: data.field.name
        })).prop('selected', true);

    });
}


function showDepartmentToast() {
    //오류
    console.log(departmentOptionToast);
    departmentOptionToast.show();
}

function addDepartment() {
    const optionElement = createOption(customDepartment.val());
    departmentOptionForm.children().last().before(optionElement);
    customDepartment.val('');

    fieldOptionForm.empty();
    fieldOptionForm.append(optionElement);

    departmentOptionToast.hide();
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
function loadField(departmentElement) {
    $.ajax({
        type: 'GET',
        url: `/api/job/category/department/${departmentElement.value}/field`,
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

