function showCutModal(btnObj) {

    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#idOfTreeStorageRow').val(trId);
    console.log(  $(trObj).attr('id'));

    let codeOfTreeStorage = $(trObj).find('th:eq(0)').text();
    let extent =            $(trObj).find('td:eq(8)').text();

    $('#addRawStorageCode').val(codeOfTreeStorage);
    $('#maxPossibleExtent').val(extent);


    $('#cutModal').modal('show');
}

$( "#cutModalForm" ).submit(function( event ) {

    let addTreeStorageLeft = parseFloat($('#addTreeStorageLeft').val());
    let extentOfWaste      = parseFloat($('#extentOfWaste').val());
    let sum = addTreeStorageLeft+extentOfWaste;

    let maxVal             = parseFloat($('#maxPossibleExtent').val());

    console.log("calc: "+sum);
    console.log("max: "+maxVal);

    if (sum>maxVal) {
        alert("Результат превишает допустимую кубатуру на "+(sum-maxVal)+" м3");
        $('#addTreeStorageLeft').val(0.000);
        $('#extentOfWaste').val(0.000);
        return false;
    } else {
        return true;
    };
    event.preventDefault();
});

function showEditModal(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#incomeTreeStorageId').val(trId);

    var code =          $(trObj).find('th:eq(0)').text();
    var date =          $(trObj).find('td:eq(1)').text();
    var breed =         $(trObj).find('td:eq(2)').text();
    var desc =          $(trObj).find('td:eq(3)').text();
    var provider =      $(trObj).find('td:eq(4)').text();
    var providerId =    $(trObj).find('td:eq(5)').text();
    var avgDiameter =   $(trObj).find('td:eq(6)').text();
    var amount =        $(trObj).find('td:eq(7)').text();
    var extent =        $(trObj).find('td:eq(8)').text();
    var initialExtent = $(trObj).find('td:eq(9)').text();
    // var date =          $(trObj).find('td:eq(9)').text();
    var description =   $(trObj).find('td:eq(10)').text();

    $('#editIncomeCode')            .val(code);
    $('#editIncomeMaterial')        .val(breed);
    $('#editIncomeMaterialDesc')    .val(desc);
    $('#editIncomeSupplier')        .val(provider);
    $('#editIncomeSupplier-hidden') .val(providerId);
    $('#editAverageDiameter')       .val(avgDiameter);
    $('#editAmount')                .val(amount);
    $('#editIncomeVolume')          .val(extent);
    $('#editIncomeInitialVolume')   .val(initialExtent);
    $('#editIncomeDate')            .val(date);
    $('#editIncomeDescription')     .val(description);

    $('#editIncome').modal('show');
}

function showEditCutModal(btnObj){
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#treeStorageId').val(trId);

    var teamCode =      $(trObj).find('td:eq(1)').text();
    var date =          $(trObj).find('td:eq(0)').text();
    var desc =          $(trObj).find('td:eq(2)').text();

    var thickness = 	$(trObj).find('td:eq(3)').text();
    var width = 		$(trObj).find('td:eq(4)').text();
    var length = 		$(trObj).find('td:eq(5)').text();
    var count = 		$(trObj).find('td:eq(6)').text();

    var usedExtent =    $(trObj).find('td:eq(7)').text();
    var extent =        $(trObj).find('td:eq(8)').text();

    var addedTo =       $(trObj).find('td:eq(10)').text().trim();

    $('#editTeamCode')              .val(teamCode);
    $('#editDate')                  .val(date);
    $('#editMaterialDescr')         .val(desc);

    $('#editThickness')             .val(thickness);
    $('#editWidth')                 .val(width);
    $('#editLength')                .val(length);
    $('#editDescCount')             .val(count);

    $('#editUsedVolume')            .val(usedExtent);
    $('#editVolume')                .val(extent);

    $('#editAddedTo')               .val(addedTo);


    $('#editTreeStorageCutModal').modal('show');
}


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
        $('#cutTreeStorageWidth').focus();
    }
});

$('#cutTreeStorageWidth').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $('#cutTreeStorageCount').focus();
    }
});

$('#cutTreeStorageCount').on('keyup', function(event){
    if(event.keyCode == 13){
        event.preventDefault();
        $("#buttonForAddingCutTreeStorageItem").click();
        $('#cutTreeStorageDescr').focus();
    }
});

