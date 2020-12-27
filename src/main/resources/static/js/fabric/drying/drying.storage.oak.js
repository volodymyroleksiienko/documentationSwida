function sendForDryingOak(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForDryStorageModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForDryStorageModalHeader').text("Отправить на сухой склад "+codeOfRawStorage);

    var code =              $(trObj).find('th:eq(0)').text();
    var material =          $(trObj).find('td:eq(1)').text();
    var breedDescription =  $(trObj).find('td:eq(2)').text();
    var thickness =         $(trObj).find('td:eq(3)').text();
    var length =            $(trObj).find('td:eq(4)').text();
    var extent =            $(trObj).find('td:eq(5)').text();
    var description =       $(trObj).find('td:eq(6)').text();
    var date =              $(trObj).find('td:eq(7)').text();

    $('#sendForDryStorageModalCode').val(code);
    $('#sendForDryStorageModalMaterial').val(material);
    $('#sendForDryStorageModalMaterialDescr').val(breedDescription);
    $('#sendForDryStorageModalThickness').val(thickness);
    $('#sendForDryStorageModalLength').val(length);
    $('#sendForDryStorageModalVolume').val(extent);
    $('#sendForDryStorageModalDescription').val(description);
    $('#editDryingDateOak').val(date);


    $('#sendForDryStorageModal').modal('show');
}


function editRawStorageOak(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#editDryingModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#editDryingModalHeader').text("Редактировать "+codeOfRawStorage);

    var code =              $(trObj).find('th:eq(0)').text();
    var material =          $(trObj).find('td:eq(1)').text();
    var breedDescription =  $(trObj).find('td:eq(2)').text();
    var thickness =         $(trObj).find('td:eq(3)').text();
    var length =            $(trObj).find('td:eq(4)').text();
    var extent =            $(trObj).find('td:eq(5)').text();
    var description =       $(trObj).find('td:eq(6)').text();
    var date =              $(trObj).find('td:eq(7)').text();
    var rawExtent =         $(trObj).find('td:eq(8)').text();
    var initial =           $(trObj).find('td:eq(9)').text();

    $('#editDryingModalCode').val(code);
    $('#editDryingModalMaterial').val(material);
    $('#editDryingModalMaterialDescr').val(breedDescription);
    $('#editDryingModalThickness').val(thickness);
    $('#editDryingModalLength').val(length);
    $('#editDryingModalVolume').val(extent);
    $('#editForDryingModalDescription').val(description);
    $('#editDryingDateOak').val(date);
    $('#editDryingModalRawExtent').val(rawExtent);
    $('#editDryingModalInitialExtent').val(initial);


    $('#editDryingModalOak').modal('show');
}

$("#editDryingOakForm").submit(function( event ) {
    let rawStExt =      parseFloat($('#editDryingModalRawExtent').val());
    let initialExtent = parseFloat($('#editDryingModalInitialExtent').val());
    let extent =        parseFloat($('#editDryingModalVolume').val());

    console.log("ts:"+rawStExt+"in:"+initialExtent+"cur:"+extent);

    if (extent>(rawStExt+initialExtent)) {
        if(!confirm("Введенная Вами кубатура превышает кубатуру на сыром складе на "+(extent-(rawStExt+initialExtent)).toFixed(3)+" м3! Продолжить?")){
            event.preventDefault();
        }
    }
});

function addOakDryingStorageItem(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    let packageCode =       $(trObj).find('th:eq(0)').text();

    console.log("row id: "+trId);

    $('#addOakPackageItemsModalHeader').text("Добавить позицию к  "+ packageCode);
    $('#addRawStorageId').val(trId);

    $('#addOakDryingStrorageItemModal').modal('show');
}

function editOakPackageItem(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    console.log("row id: "+trId);

    let expandedObj = trObj.parentElement.parentElement.parentElement.parentElement;
    let expandedId = $(expandedObj).attr('id');
    console.log("string-id: "+expandedId);

    let packageId = expandedId.replace(/\D+/g, "");
    console.log("parent-id: "+packageId);

    let rowObj = document.getElementById(packageId);

    $('#editOakPackageId').val(packageId);

    let width =             $(trObj).find('td:eq(0)').text();
    let count =             $(trObj).find('td:eq(1)').text();

    $('#oakPackageContentId')            .val(trId);
    $('#editOakPackageContentWidth')     .val(width);
    $('#editOakPackageContentCount')     .val(count);

    $('#editOakPackageContentInitWidth')     .val(width);
    $('#editOakPackageContentInitCount')     .val(count);

    $('#editOakDryingStorageItemModal').modal('show');
}

function deleteOakPackageItem(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#deleteOakPackItemModalId').val(trId);
    console.log("row id: "+trId);

    let expandedObj = trObj.parentElement.parentElement.parentElement.parentElement;
    let expandedId = $(expandedObj).attr('id');
    console.log("string-id: "+expandedId);
    let packageId = expandedId.replace(/\D+/g, "");
    console.log("id: "+packageId);

    $('#deleteOakPackId').val(packageId);

    let width = $(trObj).find('td:eq(0)').text();
    let count = $(trObj).find('td:eq(1)').text();

    $('#deleteOakPackItemModalConfirmation').text(" Вы уверены, что хотите удалить позицию: "+width+"мм / "+count+"шт?");
    $('#deleteOakDryingStorageItemModal').modal('show');
}