

function editDriverInfo(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    // let trId =  $(trObj).attr('id');


    var trId =              $(trObj).find('td:eq(0)').attr('id');
    var truck =             $(trObj).find('td:eq(1)').text();
    var truckNum =          $(trObj).find('td:eq(2)').text();
    var trailerNum  =       $(trObj).find('td:eq(3)').text();
    var packDate  =         $(trObj).find('td:eq(4)').text();
    var packTime  =         $(trObj).find('td:eq(5)').text();
    var driver =            $(trObj).find('td:eq(6)').text();
    var phone =             $(trObj).find('td:eq(7)').text();
    var contract =          $(trObj).find('td:eq(8)').text();
    var destination =       $(trObj).find('td:eq(9)').text();
    var delDescription =    $(trObj).find('td:eq(10)').text();
    var contractId =        $(trObj).find('td:eq(11)').text();


    $('#driverId')                      .val(trId);
    $('#editIdOfTruck')                 .val(truck);
    $('#editDriverCredentionals')       .val(driver);
    $('#editDriverPhone')               .val(phone);
    $('#editNumberOfTruck')             .val(truckNum);
    $('#editNumberOfTrailer')           .val(trailerNum);
    $('#editPackDateInput')             .val(packDate);
    $('#editPackTimeInput')             .val(packTime);
    $('#editPackContractId')            .val(contract);
    $('#editPackDeliveryDestination')   .val(destination);
    $('#editDeliveryDescription')       .val(delDescription);
    $('#editPackContractId-hidden')       .val(contractId);

    $('#editDriverInfoModal').modal('show');
}

function editDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#packageId').val(trId);

    var pack =              $(trObj).find('th:eq(0)').text();
    var breedOfTree =       $(trObj).find('td:eq(0)').text();
    var breedDescription =  $(trObj).find('td:eq(1)').text();
    var thickness =         $(trObj).find('td:eq(2)').text();
    var width =             $(trObj).find('td:eq(3)').text();
    var length =            $(trObj).find('td:eq(4)').text();
    var packCount =         $(trObj).find('td:eq(5)').text();
    var extent =            $(trObj).find('td:eq(6)').text();
    var heightPc =          $(trObj).find('td:eq(7)').text();
    var widthPc =           $(trObj).find('td:eq(8)').text();
    var lengthFact =        $(trObj).find('td:eq(9)').text();
    var widthHeight =       $(trObj).find('td:eq(10)').text();


    $('#editIdOfPack')                  .val(pack);
    $('#editDeliveryBreedOfTree')       .val(breedOfTree);
    $('#editBreedDescriptionDelivery')  .val(breedDescription);
    $('#editDeliveryPackThickness')     .val(thickness);
    $('#editDeliveryPackWidth')         .val(width);
    $('#editDeliveryPackLength')        .val(length);
    $('#editDeliveryPackCount')         .val(packCount);
    $('#editDeliveryPackExtent')        .val(extent);
    $('#editDeliveryPackHeightPc')      .val(heightPc);
    $('#editDeliveryPackWidthPc')       .val(widthPc);
    $('#editDeliveryPackLengthFact')    .val(lengthFact);
    $('#editDeliveryPackWidthHeight')    .val(widthHeight);

    $('#editDriverPackagesModal').modal('show');
}

function addDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).find('td:eq(0)').attr('id');
    $('#driverDeliveryId').val(trId);

    $('#addDriverPackagesModal').modal('show');
}

function deleteDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#deletePackModalId').val(trId);
    console.log(  $(trObj).attr('id'));

    let expandedObj = trObj.parentElement.parentElement.parentElement.parentElement;
    let expandedId = $(expandedObj).attr('id');
    console.log("fullId: "+expandedId);
    let deliveryId = expandedId.replace(/\D+/g, "");
    console.log("Id: "+deliveryId);

    $('#deleteDeliveryId').val(deliveryId);

    var codeOfPackage = $(trObj).find('th:eq(0)').text();

    $('#deletePackModalConfirmation').text(" Вы уверены, что хотите окончательно удалить пачку "+codeOfPackage+" ?");
    $('#deletePackModal').modal('show');
}


function deleteDriverInfo(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#deleteRecordModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    let carNumber = $(trObj).find('td:eq(1)').text();

    $('#deleteModalConfirmation').text("Вы уверены, что хотите удалить выгрузку №"+carNumber+"?");
    $('#deleteConfirmationModal').modal('show');
}