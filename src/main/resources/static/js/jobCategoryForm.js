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
});


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
    console.log(departmentElement);
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

