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
var date = $(trObj).find('td:eq(7)').text();

$('#editDryingModalCode').val(code);
$('#editDryingModalMaterial').val(material);
$('#editDryingModalMaterialDescr').val(description);
$('#editDryingModalThickness').val(thickness);
$('#editDryingModalWidth').val(width);
$('#editDryingModalLength').val(length);
$('#editDryingModalCount').val(count);
$('#editDryingModalVolume').val(extent);
$('#editDryingDate').val(date);


$('#editDryingModal').modal('show');
}