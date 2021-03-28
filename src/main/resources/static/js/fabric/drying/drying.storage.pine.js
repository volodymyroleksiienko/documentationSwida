function sendForDrying(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForDryingModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForDryingModalHeader').text("Отправить на сухой склад "+codeOfRawStorage);

    var code = $(trObj).find('th:eq(0)').text();
    var material = $(trObj).find('td:eq(0)').text();
    var description = $(trObj).find('td:eq(1)').text();
    var thickness = $(trObj).find('td:eq(2)').text();
    var width = $(trObj).find('td:eq(3)').text();
    var length = $(trObj).find('td:eq(4)').text();
    var count = $(trObj).find('td:eq(5)').text();
    var extent = $(trObj).find('td:eq(6)').text();


    $('#sendForDryingModalCode').val(code);
    $('#sendForDryingModalMaterial').val(material);
    $('#sendForDryingModalMaterialDescr').val(description);
    $('#sendForDryingModalThickness').val(thickness);
    $('#sendForDryingModalWidth').val(width);
    $('#sendForDryingModalLength').val(length);
    $('#sendForDryingModalCount').val(count);
    $('#sendForDryingModalVolume').val(extent);

    $('#sendForDryingModal').modal('show');
}

function editRawStorage(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#editDryingModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#editDryingModalHeader').text("Редактировать "+codeOfRawStorage);

    var code = $(trObj).find('th:eq(0)').text();
    var material = $(trObj).find('td:eq(0)').text();
    var description = $(trObj).find('td:eq(1)').text();
    var thickness = $(trObj).find('td:eq(2)').text();
    var width = $(trObj).find('td:eq(3)').text();
    var length = $(trObj).find('td:eq(4)').text();
    var count = $(trObj).find('td:eq(5)').text();
    var extent = $(trObj).find('td:eq(6)').text();
    var cell = $(trObj).find('td:eq(7)').text();
    var incomeDate = $(trObj).find('td:eq(8)').text();
    var exitDate = $(trObj).find('td:eq(9)').text();
    var initial = $(trObj).find('td:eq(5)').text();
    var rawDescsCount = $(trObj).find('td:eq(10)').text();

    $('#editDryingModalCode').val(code);
    $('#editDryingModalMaterial').val(material);
    $('#editDryingModalMaterialDescr').val(description);
    $('#editDryingModalThickness').val(thickness);
    $('#editDryingModalWidth').val(width);
    $('#editDryingModalLength').val(length);
    $('#editDryingModalCount').val(count);
    $('#editDryingModalVolume').val(extent);
    $('#editDryingModalCell').val(cell);
    $('#editDryingStartDate').val(incomeDate);
    $('#editDryingEndDate').val(exitDate);
    $('#editDryingModalInitialDescsCount').val(initial);
    $('#editDryingModalRawDescsCount').val(rawDescsCount);

    console.log("exit:"+exitDate+"; income: "+incomeDate);

    $('#editDryingModal').modal('show');
}

function calculateExtent() {
    let thickness = parseFloat($('#editDryingModalThickness').val());
    let width =     parseFloat($('#editDryingModalWidth').val());
    let length =    parseFloat($('#editDryingModalLength').val());
    let count =     parseInt($('#editDryingModalCount').val());

    if (!Number.isNaN(thickness) && !Number.isNaN(width) && !Number.isNaN(length) && !Number.isNaN(count) ) {
        let res = (thickness / 1000) * (width / 1000) * (length / 1000) * count;
        $('#editDryingModalVolume').val(res.toFixed(3));
    }else {
        $('#editDryingModalVolume').val(0.000);
        console.log("NaN value");
    }
}

$("#editDryingStorageForm").submit(function( event ) {
    let rawCount =     parseFloat($('#editDryingModalRawDescsCount').val());
    let initialCount = parseFloat($('#editDryingModalInitialDescsCount').val());
    let count =        parseFloat($('#editDryingModalCount').val());

    console.log("raw:"+rawCount+"in:"+initialCount+"cur:"+count);

    if (count>(rawCount+initialCount)) {
        if(!confirm("Введенное Вами количество досок превышает количество на сыром складе на "+(count-(rawCount+initialCount))+" шт.! Продолжить?")){
            event.preventDefault();
        }
    }
});