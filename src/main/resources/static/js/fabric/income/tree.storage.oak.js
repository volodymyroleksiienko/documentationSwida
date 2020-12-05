function showCutOakModal(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#idOfOakTreeStorageRow').val(trId);
    console.log("row id:" + $(trObj).attr('id'));
    var codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
    var description = $(trObj).find('td:eq(1)').text();
    var extent = parseFloat($(trObj).find('td:eq(4)').text());


    $('#cutOakRawStorageCode').val(codeOfTreeStorage);
    $('#cutOakRawStorageBreedDesc').val(description);
    $('#cutOakRawStorageUsedExtent').val(extent.toFixed(3));

    $('#cutOakModal').modal('show');
}

function showEditOakModal(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    console.log("row id:" + $(trObj).attr('id'));
    $('#incomeOakTreeStorageId').val(trId);

    var code =      $(trObj).find('th:eq(0)').text();
    var breed =     $(trObj).find('td:eq(1)').text();
    var desc =      $(trObj).find('td:eq(2)').text();
    var provider =  $(trObj).find('td:eq(3)').text();
    var providerId= $(trObj).find('td:eq(4)').text();
    var avgDiameter=$(trObj).find('td:eq(5)').text();
    var extent =    $(trObj).find('td:eq(6)').text();
    var date =      $(trObj).find('td:eq(7)').text();

    $('#editOakIncomeCode')             .val(code);
    $('#editOakIncomeMaterial')         .val(breed);
    $('#editOakIncomeMaterialDesc')     .val(desc);
    $('#editOakIncomeSupplier')         .val(provider);
    $('#editOakIncomeSupplier-hidden')  .val(providerId);
    $('#editOakAverageDiameter')        .val(avgDiameter);
    $('#editOakIncomeVolume')           .val(extent);
    $('#editOakIncomeDate')             .val(date);

    $('#editOakIncome').modal('show');
}