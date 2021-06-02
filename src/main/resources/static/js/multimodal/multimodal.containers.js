function countCostOfUploading() {
    let price =         parseFloat($('#addContainerUnloadingPrice').val());
    let extent =        parseFloat($('#addContainerExpotrExtent').val());
    let unloading =     parseFloat($('#addContainerUnloading').val());
    let delivery =      parseFloat($('#addContainerDelivery').val());
    let measurement =   parseFloat($('#addContainerMeasurement').val());
    let currency =      parseFloat($('#addContainerCurrency').val());

    if (!Number.isNaN(extent) && !Number.isNaN(price)) {
        $('#addContainerUnloading').val((extent*price).toFixed(2));
    }else {
        $('#addContainerUnloading').val('0.00');
        $('#addContainerUahCurrency').val('0.00');
    }

    if (!Number.isNaN(extent) && !Number.isNaN(price) && !Number.isNaN(unloading) && !Number.isNaN(currency) && !Number.isNaN(delivery) && !Number.isNaN(measurement)) {
        $('#addContainerUahCurrency').val(((price*extent*currency)+(delivery*currency)+measurement).toFixed(2));
    }else {
        $('#addContainerUahCurrency').val('0.00');
    }
}


function countCostOfUploadingEdit() {
    let price =         parseFloat($('#editContainerUnloadingPrice').val());
    let extent =        parseFloat($('#editContainerExpotrExtent').val());
    let unloading =     parseFloat($('#editContainerUnloading').val());
    let delivery =      parseFloat($('#editContainerDelivery').val());
    let measurement =   parseFloat($('#editContainerMeasurement').val());
    let currency =      parseFloat($('#editContainerCurrency').val());


    if (!Number.isNaN(extent) && !Number.isNaN(price)) {
        $('#editContainerUnloading').val((extent*price).toFixed(2));
    }else {
        $('#editContainerUnloading').val('0.00');
        $('#editUah').val('0.00');
    }

    if (!Number.isNaN(extent) && !Number.isNaN(price) && !Number.isNaN(unloading) && !Number.isNaN(currency) && !Number.isNaN(delivery) && !Number.isNaN(measurement)) {
        $('#editUah').val(((price*extent*currency)+(delivery*currency)+measurement).toFixed(2));
    }else {
        $('#editUah').val('0.00');
    }
}
function editContainer(btnObj) {

    let trObj =             btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    $('#editContainerId').val(trId);
    console.log(  $(trObj).attr('id'));
    let cantainerNumber =    $(trObj).find('td:eq(0)').text();
    $('#editContainerModalHeader').text("Редактировать контейнер №"+ cantainerNumber);

    let contractNumber =        $(trObj).find('td:eq(1)').text();
    let buyer =                 $(trObj).find('td:eq(2)').text();
    let exportExtent =          $(trObj).find('td:eq(3)').text();
    let unloadingPrice =        $(trObj).find('td:eq(4)').text();
    let unloading =             $(trObj).find('td:eq(5)').text();
    let delivery =              $(trObj).find('td:eq(6)').text();
    let measurement =           $(trObj).find('td:eq(7)').text();
    let currency =              $(trObj).find('td:eq(8)').text();
    let uah =                   $(trObj).find('td:eq(9)').text();
    let date =                  $(trObj).find('td:eq(10)').find('.original-date').text();
    let state =                 $(trObj).find('td:eq(11)').text();
    let buyerId =               $(trObj).find('td:eq(13)').text();


    $('#editContainerNumber')           .val(cantainerNumber);
    $('#editContractNumber')            .val(contractNumber);
    $('#editContainerBuyer')            .val(buyer);
    $('#editContainerBuyer-hidden')     .val(buyerId);
    $('#editContainerExpotrExtent')     .val(parseFloat(exportExtent).toFixed(2));
    $('#editContainerUnloadingPrice')   .val(parseFloat(unloadingPrice).toFixed(2));
    $('#editContainerUnloading')        .val(parseFloat(unloading).toFixed(2));
    $('#editContainerDelivery')         .val(parseInt(delivery));
    $('#editContainerMeasurement')      .val(parseInt(measurement));
    $('#editContainerCurrency')         .val(parseFloat(currency).toFixed(2));
    $('#editUah')                       .val(parseFloat(uah).toFixed(2));
    $('#editContainerDate')             .val(date);
    $('#editContainerState')            .val(state);


    $('#editContainerModal').modal('show');
}

function sendToArchive(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#archiveRecordModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    let codeOfContract = $(trObj).find('td:eq(0)').text();

    $('#archiveModalConfirmation').text("Вы уверены, что хотите отправить в архив контейнер №"+codeOfContract+" ?");
    $('#archiveConfirmationModal').modal('show');
}