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
    console.log(fieldOptionToast);
    fieldOptionToast.show();
}

function addField() {
    const optionElement = createOption(customField.val());
    fieldOptionForm.append(optionElement);
    customField.val('');
    fieldOptionToast.hide();
}

function createOption(value) {
    return `<option value="${value}" selected>${value}</option>`;
}