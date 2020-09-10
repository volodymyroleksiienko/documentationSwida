function showCutOakModal(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#idOfOakTreeStorageRow').val(trId);
    console.log("row id:" + $(trObj).attr('id'));
    var codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
    var description = $(trObj).find('td:eq(1)').text();


    $('#cutOakRawStorageCode').val(codeOfTreeStorage);
    $('#cutOakRawStorageBreedDesc').val(description);

    $('#cutOakModal').modal('show');
}

function showEditOakModal(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    console.log("row id:" + $(trObj).attr('id'));
    $('#incomeOakTreeStorageId').val(trId);

    var code = $(trObj).find('th:eq(0)').text();
    var breed = $(trObj).find('td:eq(0)').text();
    var desc = $(trObj).find('td:eq(1)').text();
    var provider = $(trObj).find('td:eq(2)').text();
    var providerId = $(trObj).find('td:eq(3)').text();
    var extent = $(trObj).find('td:eq(4)').text();

    $('#editOakIncomeCode').val(code);
    $('#editOakIncomeMaterial').val(breed);
    $('#editOakIncomeMaterialDesc').val(desc);
    $('#editOakIncomeSupplier').val(provider);
    $('#editOakIncomeSupplier-hidden').val(providerId);
    $('#editOakIncomeVolume').val(extent);

    $('#editOakIncome').modal('show');
}