function sendForDrying(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId = $(trObj).attr('id');
    $('#sendForDryingModalId').val(trId);
    console.log($(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForDryingModalHeader').text("Отправить в сушку " + codeOfRawStorage);

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


    $("#sendForDryingModalCount").change(function () {
        if ((parseInt($('#sendForDryingModalCount').val())) <= 0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalPacksCount').val("1");
        } else {
            var maxAmount = count;
            var amount = parseInt($('#sendForDryingModalCount').val());
            var luck = amount - maxAmount;
            if (amount > maxAmount) {
                alert("Не хватает досок: " + luck + "!");
                $('#sendForDryingModalCount').val("");
            }
        }
    });
    // CHECK
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
    var treeStExt = $(trObj).find('td:eq(7)').text();

    $('#editDryingModalCode').val(code);
    $('#editDryingModalMaterial').val(material);
    $('#editDryingModalMaterialDescr').val(description);
    $('#editDryingModalThickness').val(thickness);
    $('#editDryingModalWidth').val(width);
    $('#editDryingModalLength').val(length);
    $('#editDryingModalCount').val(count);
    $('#editDryingModalVolume').val(extent);
    $('#editDryingModalMaxExtent').val(treeStExt);
    $('#editDryingModalInitialExtent').val(extent);


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

$("#submitFormForEditRawStorage").submit(function( event ) {
    let treeStExt =     parseFloat($('#editDryingModalMaxExtent').val());
    let initialExtent = parseFloat($('#editDryingModalInitialExtent').val());
    let extent =        parseFloat($('#editDryingModalVolume').val());
    console.log("ts:"+treeStExt+"in:"+initialExtent+"cur:"+extent);

    if (extent>(treeStExt+initialExtent)) {
        if(!confirm("Введенная Вами кубатура превышает кубатуру на складе кругляка на "+(extent-(treeStExt+initialExtent)).toFixed(3)+" м3! Продолжить?")){
            event.preventDefault();
        }
    }
});



function sendForPackagesStorage(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForPackagesModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForPackageModalHeader').text("Формирование пачек из  "+codeOfRawStorage);

    var code = 			$(trObj).find('th:eq(0)').text();
    var material = 		$(trObj).find('td:eq(0)').text();
    var description = 	$(trObj).find('td:eq(1)').text();
    var thickness = 	parseInt($(trObj).find('td:eq(2)').text());
    var width = 		parseInt($(trObj).find('td:eq(3)').text());
    var length = 		parseInt($(trObj).find('td:eq(4)').text());
    var count = 		parseInt($(trObj).find('td:eq(5)').text());
    var extent = 		$(trObj).find('td:eq(6)').text();

    $('#sendForPackageModalCode')			.val(code);
    $('#sendForPackageModalMaterial')		.val(material);
    $('#sendForPackageModalMaterialDescr')	.val(description);
    $('#sendForPackageModalThickness')		.val(thickness);
    $('#sendForPackageModalWidth')			.val(width);
    $('#sendForPackageModalLength')			.val(length);
    $('#sendForPackageModalCount')			.val(count);
    $('#sendForPackageModalVolume')			.val(extent);

    $('#sendFromRawToPackageModal').modal('show');
    $('#sendForPackageHeightPc').focus();

    // CHECK
    $("#sendForPackageHeightPc").change(function(){
        if ((parseInt($('#sendForPackageHeightPc').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageHeightPc').val("1");
        }else{

        }
    });

    $("#sendForPackageModalWidthPc").change(function(){
        if ((parseInt($('#sendForPackageModalWidthPc').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalWidthPc').val("1");
        }else{

        }
    });

    $("#sendForPackageModalLengthFact").change(function(){
        if ((parseInt($('#sendForPackageModalLengthFact').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalLengthFact').val("1");
        }else{

        }
    });

    $("#sendForPackageModalPacksCount").change(function(){
        if ((parseInt($('#sendForPackageModalPacksCount').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalPacksCount').val("1");
        }else{
            var heightPc = 		parseInt($('#sendForPackageHeightPc').val());
            var widthPc = 		parseInt($('#sendForPackageModalWidthPc').val());
            var amount = 		heightPc*widthPc*(parseInt($('#sendForPackageModalPacksCount').val()));
            var maxAmount =		count;
            var packsCt = 		parseInt($('#sendForPackageModalPacksCount').val());
            var luck =			amount-maxAmount;
            if (amount>maxAmount) {
                alert("Не хватает досок: "+luck+"!");
                $('#sendForPackageModalPacksCount').val("");
            }
        }
    });
    // CHECK
}


// function returnToIncome(btnObj) {
//     let trObj = btnObj.parentElement.parentElement;
//     let trId =  $(trObj).attr('id');
//     $('#returnItemId').val(trId);
//     console.log("row id: "+trId);
//
//     let code = $(trObj).find('th:eq(0)').text();
//
//     $('#returnItemModalConfirmation').text(" Вы уверены, что хотите возвратить партию №"+code+" на склад прихода?");
//
//
//
//     $('#returnToIncomeModal').modal('show');
// }

function returnToIncome(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#returnItemId').val(trId);
    console.log("row id: "+trId);

    let code =          $(trObj).find('th:eq(0)').text();
    let rsMinusMax =    $(trObj).find('td:eq(9)').text();
    let recMinusMax =   $(trObj).find('td:eq(10)').text();

    console.log("rs: " + rsMinusMax+"; rec: "+ recMinusMax);
    console.log(recMinusMax.localeCompare("null"));

    if (((parseFloat(rsMinusMax) === 0.0)&&(parseFloat(recMinusMax) === 0.0))||((parseFloat(rsMinusMax) === 0.0)&&(recMinusMax === "null")) ){
        $('#submitReturnBtnRS').css("display", "block");
        $('#returnItemModalConfirmation').text(" Вы уверены, что хотите возвратить партию №"+code+" на склад прихода?");
    }
    else if ((parseFloat(rsMinusMax) !== 0.0)&&((parseFloat(recMinusMax) !== 0.0)&&(recMinusMax.localeCompare("null")!==0))){
        $('#submitReturnBtnRS').css("display", "none");
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на сыром складе не совпадает на "+rsMinusMax+ " м3., кубатура на складе отходов не совпадает на "+recMinusMax+ " м3.");
    }
    else if ((parseFloat(rsMinusMax) !== 0.0)){
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на сыром складе не совпадает на "+rsMinusMax+ " м3.");
        $('#submitReturnBtnRS').css("display", "none");
    }
    else if ((parseFloat(recMinusMax) !== 0.0)||(recMinusMax !== "null")){
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на складе отходов не совпадает на "+recMinusMax+ " м3.");
        $('#submitReturnBtnRS').css("display", "none");
    }

    $('#returnToIncomeModal').modal('show');
}


