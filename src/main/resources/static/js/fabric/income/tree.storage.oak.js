function showCutOakModal(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#idOfTreeStorageRow').val(trId);
    console.log(  $(trObj).attr('id'));

    let codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
    let extent =            $(trObj).find('td:eq(8)').text();

    $('#addRawStorageCode').val(codeOfTreeStorage);
    $('#maxPossibleExtent').val(extent);

    $('#cutOakModal').modal('show');
}

// function showCutOakModal(btnObj) {
//     let trObj = btnObj.parentElement.parentElement;
//     let trId =  $(trObj).attr('id');
//     $('#idOfOakTreeStorageRow').val(trId);
//     console.log("row id:" + $(trObj).attr('id'));
//
//     let codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
//     let description =       $(trObj).find('td:eq(3)').text().trim();
//     let extent =            $(trObj).find('td:eq(8)').text();
//
//
//     $('#cutOakRawStorageCode').val(codeOfTreeStorage);
//     $('#cutOakRawStorageBreedDesc').val(description);
//     $('#maxPossibleOakExtent').val(extent);
//
//     $('#cutOakModal').modal('show');
// }

$( "#cutOakModalForm" ).submit(function( event ) {

    let rawStorage =            parseFloat($('#cutOakRawStorageExtent').val());
    let used =                  parseFloat($('#cutOakRawStorageUsedExtent').val());
    let extentOfWaste =         parseFloat($('#extentOfWasteOak').val());

    let maxVal =                 parseFloat($('#maxPossibleOakExtent').val());

    console.log("calc: "+(rawStorage+extentOfWaste));
    console.log("max: "+maxVal);

    if ((rawStorage+extentOfWaste)>maxVal) {
        alert("Кубатура после распиловки и отходов превишает максимально возможную на "+((rawStorage+extentOfWaste)-maxVal)+" м3.");

        // $('#cutOakRawStorageExtent').val(0.000);
        // $('#cutOakRawStorageUsedExtent').val(0.000);
        // $('#extentOfWasteOak').val(0.000);

        return false;
    } else if (used>maxVal){
        alert("Кубатура использованого материала превишает максимально возможную на "+(used-maxVal)+" м3.");
        return false;
    } else  if ((rawStorage+extentOfWaste)>used){
        alert("Кубатура использованого материала не сходится с суммой кубатур после распиловки и отходами.");
        return false;
    } else {
        return true;
    };
    event.preventDefault();
});

function showEditOakModal(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    console.log("row id:" + $(trObj).attr('id'));
    $('#incomeOakTreeStorageId').val(trId);

    let code =          $(trObj).find('th:eq(0)').text();
    let date =          $(trObj).find('td:eq(1)').find('.original-date').text();
    let breed =         $(trObj).find('td:eq(2)').text();
    let desc =          $(trObj).find('td:eq(3)').text();
    let provider =      $(trObj).find('td:eq(4)').text();
    let providerId=     $(trObj).find('td:eq(5)').text();
    let avgDiameter=    $(trObj).find('td:eq(6)').text();
    let amount =        $(trObj).find('td:eq(7)').text();
    let extent =        $(trObj).find('td:eq(8)').text();
    let initialExtent = $(trObj).find('td:eq(9)').text();
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

function showEditCutModal(btnObj){
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#treeStorageId').val(trId);

    var teamCode =      $(trObj).find('td:eq(1)').text();
    var date =          $(trObj).find('td:eq(0)').find('.original-date').text();
    var desc =          $(trObj).find('td:eq(2)').text();

    var thickness = 	$(trObj).find('td:eq(3)').text();
    var length = 		$(trObj).find('td:eq(4)').text();

    var usedExtent =    $(trObj).find('td:eq(5)').text();
    var extent =        $(trObj).find('td:eq(6)').text();

    var addedTo =       $(trObj).find('td:eq(8)').text().trim();

    $('#editTeamCode')              .val(teamCode);
    $('#editDate')                  .val(date);
    $('#editMaterialDescr')         .val(desc);

    $('#editThickness')             .val(thickness);
    $('#editLength')                .val(length);

    $('#editUsedVolume')            .val(usedExtent);
    $('#editVolume')                .val(extent);

    $('#editAddedTo')               .val(addedTo);


    $('#editTreeStorageCutOakModal').modal('show');
}

$("#editCutMaterialForm").submit(function( event ) {
    if (parseFloat($('#editVolume').val())>parseFloat($('#editUsedVolume').val())){
        alert("Невозможно выполнить действие! Выход превышает кубатуру взятого материала на "+(parseFloat($('#editVolume').val())-parseFloat($('#editUsedVolume').val())).toFixed(3)+"м3.");
        event.preventDefault();
        return;
    }else {

    }
});

function returnToIncome(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#returnItemId').val(trId);
    console.log("row id: "+trId);

    $('#returnToIncomeModal').modal('show');
}

$('#cutTreeStorageDescr').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $('#cutTreeStorageSize').focus();
    }
});

$('#cutTreeStorageSize').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $('#cutTreeStorageLength').focus();
    }
});

$('#cutTreeStorageLength').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $('#cutTreeStorageExtent').focus();
    }
});


$('#cutTreeStorageExtent').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $("#buttonForAddingCutTreeStorageOakItem").click();
        $('#cutTreeStorageDescr').focus();
    }
});