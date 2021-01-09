function sendForDryingOak(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForDryingModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#sendForDryingModalHeader').text("Отправить в сушку "+codeOfRawStorage);

    var code =              $(trObj).find('th:eq(0)').text();
    var material =          $(trObj).find('td:eq(1)').text();
    var breedDescription =  $(trObj).find('td:eq(2)').text();
    var thickness =         $(trObj).find('td:eq(3)').text();
    var length =            $(trObj).find('td:eq(4)').text();
    var extent =            $(trObj).find('td:eq(5)').text();
    var description =       $(trObj).find('td:eq(6)').text();


    $('#sendForDryingModalCode').val(code);
    $('#sendForDryingModalMaterial').val(material);
    $('#sendForDryingModalMaterialDescr').val(breedDescription);
    $('#sendForDryingModalThickness').val(thickness);
    $('#sendForDryingModalLength').val(length);
    $('#sendForDryingModalVolume').val(extent);
    $('#sendForDryingModalDescription').val(description);


    $('#sendForDryingModalOak').modal('show');

    $( "#sendForDryingModalFormOak" ).submit(function( event ) {
        // alert( "Handler for .submit() called." );
        let maxAmount =		parseFloat(extent);
        let amount = 		parseFloat($('#sendForDryingModalVolume').val());
        let luck =			amount-maxAmount;

        if (((parseFloat($('#sendForDryingModalVolume').val()))<=0.0)) {
            alert("Введите правильное значение");
            $('#sendForDryingModalVolume').val("0.0");
        }else if (amount>maxAmount) {
            alert("Не хватает: "+luck+" м3 !");
        } else {
            return;
        }
        event.preventDefault();
    });
}



function editRawStorageOak(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#editDryingModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#editDryingModalHeader').text("Редактировать "+codeOfRawStorage);

    var code =          $(trObj).find('th:eq(0)').text();
    var material =      $(trObj).find('td:eq(1)').text();
    var description =   $(trObj).find('td:eq(2)').text();
    var thickness =     $(trObj).find('td:eq(3)').text();
    var length =        $(trObj).find('td:eq(4)').text();
    var extent =        $(trObj).find('td:eq(5)').text();
    var desc =          $(trObj).find('td:eq(6)').text();
    var treeStExt =     $(trObj).find('td:eq(7)').text();

    $('#editDryingModalCode').val(code);
    $('#editDryingModalMaterial').val(material);
    $('#editDryingModalMaterialDescr').val(description);
    $('#editDryingModalThickness').val(thickness);
    $('#editDryingModalLength').val(length);
    $('#editDryingModalVolume').val(extent);
    $('#editForDryingModalDescription').val(desc);
    $('#editDryingModalMaxExtent').val(treeStExt);
    $('#editDryingModalInitialExtent').val(extent);

    $('#editDryingModal').modal('show');
}


$("#submitFormForEditOakRawStorage").submit(function( event ) {
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


function sendForPackagesStorageOak(btnObj) {
    let trObj = document.getElementById(btnObj);
    let trId =  btnObj;
    $('#addOakInitialPackageCodeAdditionalExtentModalIdOak').val(trId);
    console.log("Row id: "+$(trObj).attr('id'));

    let codeOfRawStorage =      $(trObj).find('th:eq(0)').text();
    let size =                  $(trObj).find('td:eq(3)').text();
    let maxExtent =             $(trObj).find('td:eq(4)').text();



    $('#sendForPackageModalOakHeader').text("Расщитать и добавить кубатуру к пачке №"+codeOfRawStorage);

    $('#sendFromRawToPackageOakModal').modal('show');

    $('#sendForPackageModalSizeOak')    .val(size);
    $('#sendForPackagesMaxExtent')      .val(maxExtent);

    $('#sendForPackageModalWidthOak')       .val(100);
    $('#sendForPackageModalCountOak')       .val('');


    // CHECK
    $("#sendForPackageModalSizeOak").change(function(){
        if ((parseInt($('#sendForPackageModalSizeOak').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalSizeOak').val("1");
        }else{

        }
    });


    $("#sendForPackageModalLengthOak").change(function(){
        if ((parseInt($('#sendForPackageModalLengthOak').val()))<=0) {
            alert("Введите правильное значение");
            $('#sendForPackageModalLengthOak').val("1");
        }else{

        }
    });
    // CHECK
}

function addOakRawStorageItem(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    let packageCode =       $(trObj).find('th:eq(0)').text();

    console.log("row id: "+trId);

    $('#addOakPackageItemsModalHeader').text("Добавить позицию к  "+ packageCode);
    $('#addRawStorageId').val(trId);

    $('#addOakRawStorageItemModal').modal('show');
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

    $('#editOakRawStorageItemModal').modal('show');
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
    $('#deleteOakRawStorageItemModal').modal('show');
}
// function sendForPackagesStorageOak(btnObj) {
//
//     var trObj = btnObj.parentElement.parentElement;
//     var trId =  $(trObj).attr('id');
//     $('#sendForDryingModalIdOak').val(trId);
//     console.log("Row id: "+$(trObj).attr('id'));
//
//     var codeOfRawStorage =      $(trObj).find('th:eq(0)').text();
//     var size =                  $(trObj).find('td:eq(2)').text();
//     var quality =               $(trObj).find('td:eq(5)').text();
//     var length =                $(trObj).find('td:eq(6)').text();
//
//
//     $('#sendForPackageModalOakHeader').text("Формирование пачек из  "+codeOfRawStorage);
//
//     $('#sendForPackageModalSizeOak')        .val(size);
//     $('#sendForPackageModalQualityOak')     .val(quality);
//     $('#sendForPackageModalLengthOak')      .val(length);
//
//     $('#sendForPackageModalOak').modal('show');
//
//
//     // CHECK
//
//     $("#sendForPackageModalSizeOak").change(function(){
//         if ((parseInt($('#sendForPackageModalSizeOak').val()))<=0) {
//             alert("Введите правильное значение");
//             $('#sendForPackageModalSizeOak').val("1");
//         }else{
//
//         }
//     });
//
//     $("#sendForPackageModalLengthOak").change(function(){
//         if ((parseInt($('#sendForPackageModalLengthOak').val()))<=0) {
//             alert("Введите правильное значение");
//             $('#sendForPackageModalLengthOak').val("1");
//         }else{
//
//         }
//     });
//     // CHECK
//
// }




// // ADD Package OAK
// function sendRequestCreatePackageOak(btnObj) {
//     var breedID = $("#breedId").val();
//     var userID = $("#userId").val();
//     var idOfDryStorageOak = $("#sendForDryingModalIdOak").val();
//     console.log("id " + idOfDryStorageOak);
//     var codeOfPackage1 = $('#sendForPackageModalCodeOak').val();
//     var quality1 = $('#sendForPackageModalQualityOak').val();
//     var sizeOfHeight1 = $('#sendForPackageModalSizeOak').val();
//     var long1 = $('#sendForPackageModalLengthOak').val();
//
//     var tableBody = document.getElementById('listOfPackageId');
//     var listTr = tableBody.getElementsByTagName('tr');
//
//     var arrOfDesk = [];
//
//     //fix one dimension array on controller
//     arrOfDesk[0] = [];
//     arrOfDesk[0][0] = "test";
//     arrOfDesk[0][1] = "test";
//
//
//
//     if (codeOfPackage1 != "" && quality1 != "" && sizeOfHeight1 != "" && long1 != "" ) {
//         for (var i = 1; i <= listTr.length; i++) {
//             var width = $(listTr[i - 1]).find('td:eq(0)').text();
//             var count = $(listTr[i - 1]).find('td:eq(1)').text();
//
//             arrOfDesk[i] = [];
//             arrOfDesk[i][0] = width;
//             arrOfDesk[i][1] = count;
//         }
//
//         console.log(arrOfDesk);
//
//         $.ajax({
//             method: "post",
//             url: "/createRawPackageOakObject-" + userID + "-" + breedID,
//             contextType: "application/json",
//             data: {
//                 idOfDryStorage: idOfDryStorageOak,
//                 codeOfPackage: codeOfPackage1,
//                 quality: quality1,
//                 sizeOfHeight: sizeOfHeight1,
//                 length: long1,
//                 arrayOfDesk: arrOfDesk
//             },
//             traditional: true,
//             success: function (extent) {
//                 if (extent>0) {
//                     console.log(extent);
//                     let trObj = document.getElementById(idOfDryStorageOak);
//                     console.log(trObj);
//
//                     $(trObj).find('td:eq(3)').text(extent);
//
//                     sendForPackagesStorageOak(idOfDryStorageOak);
//
//                     $('#sendForPackageModalCodeOak').val('');
//                     $('#sendForPackageModalCodeOak').focus();
//                     // $('#sendForPackageModalQualityOak').val('');
//                     $('#sendForPackageModalWidthOak').val('');
//                     $('#sendForPackageModalCountOak').val('');
//                     $('#sendForPackageModalExtentOak').val('0.000');
//                     $("#sendForPackageModalLengthOak").removeAttr("disabled");
//                     $("#sendForPackageModalSizeOak").removeAttr("disabled");
//                     tableForTransportationOak.clear().draw();
//                 }else {
//                     location.reload();
//                 }
//             },
//             error: function () {
//                 alert("Заполните все поля!");
//             }
//         });
//     } else {
//         alert("Заполните все поля!");
//     }
// }

function resetRawExtent(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#resetItemId').val(trId);
    console.log("row id: "+trId);

    let code = $(trObj).find('th:eq(0)').text();

    $('#resetItemModalConfirmation').text(" Вы уверены, что хотите обнулить кубатуру партии №"+code+"? Это действие не возвращает материал на склад кругляка!");
    $('#resetModal').modal('show');
}

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
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на сыром складе не совпадает на "+parseFloat(rsMinusMax).toFixed(3)+ " м3., кубатура на складе отходов не совпадает на "+parseFloat(recMinusMax).toFixed(3)+ " м3.");
    }
    else if ((parseFloat(rsMinusMax) !== 0.0)){
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на сыром складе не совпадает на "+parseFloat(rsMinusMax).toFixed(3)+ " м3.");
        $('#submitReturnBtnRS').css("display", "none");
    }
    else if ((parseFloat(recMinusMax) !== 0.0)||(recMinusMax !== "null")){
        $('#returnItemModalConfirmation').text("Невозможно возвратить на сухой склад, потому что кубатура на складе отходов не совпадает на "+parseFloat(recMinusMax).toFixed(3)+ " м3.");
        $('#submitReturnBtnRS').css("display", "none");
    }

    $('#returnToIncomeModal').modal('show');
}

$('#addOakInitialPackageSupplier').on('input', function(){
    if ($('#addOakInitialPackageSupplier').val() === '') {
        $('#addOakInitialPackageTreeStorage').removeAttr('disabled');
        $('#addOakInitialPackageSupplier-hidden').val(null);
    }else {
        $('#addOakInitialPackageTreeStorage').attr('disabled', 'disabled');
    }
});

$('#addOakInitialPackageTreeStorage').on('input', function(){
    if ($('#addOakInitialPackageTreeStorage').val() === '') {
        $('#addOakInitialPackageSupplier').removeAttr('disabled');
        $('#addOakInitialPackageTreeStorage-hidden').val(null);
    }else {
        $('#addOakInitialPackageSupplier').attr('disabled', 'disabled');
    }
});







