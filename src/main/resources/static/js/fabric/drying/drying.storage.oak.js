function sendForDryingOak(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForDryStorageModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForDryStorageModalHeader').text("Отправить на сухой склад "+codeOfRawStorage);

    var code =              $(trObj).find('th:eq(0)').text();
    var material =          $(trObj).find('td:eq(0)').text();
    var breedDescription =  $(trObj).find('td:eq(1)').text();
    var thickness =         $(trObj).find('td:eq(2)').text();
    var extent =            $(trObj).find('td:eq(3)').text();
    var description =       $(trObj).find('td:eq(4)').text();

    $('#sendForDryStorageModalCode').val(code);
    $('#sendForDryStorageModalMaterial').val(material);
    $('#sendForDryStorageModalMaterialDescr').val(breedDescription);
    $('#sendForDryStorageModalThickness').val(thickness);
    $('#sendForDryStorageModalVolume').val(extent);
    $('#sendForDryStorageModalDescription').val(description);


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
    var material =          $(trObj).find('td:eq(0)').text();
    var breedDescription =  $(trObj).find('td:eq(1)').text();
    var thickness =         $(trObj).find('td:eq(2)').text();
    var extent =            $(trObj).find('td:eq(3)').text();
    var description =       $(trObj).find('td:eq(4)').text();

    $('#editDryingModalCode').val(code);
    $('#editDryingModalMaterial').val(material);
    $('#editDryingModalMaterialDescr').val(breedDescription);
    $('#editDryingModalThickness').val(thickness);
    $('#editDryingModalVolume').val(extent);
    $('#editForDryingModalDescription').val(description);


    $('#editDryingModalOak').modal('show');
}