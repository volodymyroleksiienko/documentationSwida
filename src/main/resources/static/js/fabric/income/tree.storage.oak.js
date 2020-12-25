function showCutOakModal(btnObj) {

    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#idOfOakTreeStorageRow').val(trId);
    console.log("row id:" + $(trObj).attr('id'));
    let codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
    let description =       $(trObj).find('td:eq(2)').text();
    let extent =            $(trObj).find('td:eq(7)').text();


    $('#cutOakRawStorageCode').val(codeOfTreeStorage);
    $('#cutOakRawStorageBreedDesc').val(description);
    $('#maxPossibleOakExtent').val(extent);
    $('#cutOakModal').modal('show');
}

function showEditOakModal(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    console.log("row id:" + $(trObj).attr('id'));
    $('#incomeOakTreeStorageId').val(trId);

    let code =          $(trObj).find('th:eq(0)').text();
    let breed =         $(trObj).find('td:eq(1)').text();
    let desc =          $(trObj).find('td:eq(2)').text();
    let provider =      $(trObj).find('td:eq(3)').text();
    let providerId=     $(trObj).find('td:eq(4)').text();
    let avgDiameter=    $(trObj).find('td:eq(5)').text();
    let amount =        $(trObj).find('td:eq(6)').text();
    let extent =        $(trObj).find('td:eq(7)').text();
    let initialExtent = $(trObj).find('td:eq(8)').text();
    let date =          $(trObj).find('td:eq(9)').text();
    let description =   $(trObj).find('td:eq(10)').text();

    $('#editOakIncomeCode')             .val(code);
    $('#editOakIncomeMaterial')         .val(breed);
    $('#editOakIncomeMaterialDesc')     .val(desc);
    $('#editOakIncomeSupplier')         .val(provider);
    $('#editOakIncomeSupplier-hidden')  .val(providerId);
    $('#editOakAverageDiameter')        .val(avgDiameter);
    $('#editOakAmount')                 .val(amount);
    $('#editOakIncomeVolume')           .val(extent);
    $('#editOakIncomeInitialVolume')    .val(initialExtent);
    $('#editOakIncomeDate')             .val(date);
    $('#editOakIncomeDescription')      .val(description);

    $('#editOakIncome').modal('show');
}