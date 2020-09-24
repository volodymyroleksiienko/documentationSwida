function sendForPackagesStorageOak(btnObj) {

    let trObj = document.getElementById(btnObj);

    // var trObj = btnObj.parentElement.parentElement;
    var trId =  btnObj;
    $('#sendForDryingModalIdOak').val(trId);
    console.log("Row id: "+$(trObj).attr('id'));

    var codeOfDryStorage =      $(trObj).find('th:eq(0)').text();
    var size =                  $(trObj).find('td:eq(2)').text();
    var quality =               $(trObj).find('td:eq(5)').text();
    var length =                $(trObj).find('td:eq(6)').text();
    let maxExtent =             $(trObj).find('td:eq(3)').text();


    $('#sendForPackageModalOakHeader').text("Формирование пачек из  "+codeOfDryStorage);
    $('#sendForPackageModalSizeOak')        .val(size);

    $('#sendForPackagesMaxExtent')          .val(maxExtent);

    $('#sendForPackageModalQualityOak')     .val(quality);
    $('#sendForPackageModalLengthOak')      .val(length);

    $('#sendForPackageModalWidthOak')       .val(100);
    $('#sendForPackageModalCountOak')       .val(0);
    $('#sendForPackageModalCountOak')       .focus();



    $('#sendForPackageModalOak').modal('show');


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

function editDryStorageOak(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#editDryStorageModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfDryStorage = $(trObj).find('th:eq(0)').text();
    $('#editDryStorageModalHeader').text("Редактировать "+codeOfDryStorage);

    var code =          $(trObj).find('th:eq(0)').text();
    var material =      $(trObj).find('td:eq(0)').text();
    var description =   $(trObj).find('td:eq(1)').text();
    var thickness =     $(trObj).find('td:eq(2)').text();
    var extent =        $(trObj).find('td:eq(3)').text();
    // var length = $(trObj).find('td:eq(4)').text();
    var desc =          $(trObj).find('td:eq(4)').text();
    var dryingExtent =  $(trObj).find('td:eq(7)').text();
    var initialExt =    $(trObj).find('td:eq(3)').text();

    console.log(code, material,description,thickness,extent,desc,dryingExtent,initialExt);

    $('#editDryStorageModalCode').val(code);
    $('#editDryStorageModalMaterial').val(material);
    $('#editDryStorageModalMaterialDescr').val(description);
    $('#editDryStorageModalThickness').val(thickness);
    // $('#editDryStorageModalWidth').val(width);
    // $('#editDryStorageModalLength').val(length);
    $('#editDryStorageModalVol').val(extent);
    $('#editDryStorageModalDesc').val(desc);
    $('#editDryStorageModalDryingExtent').val(dryingExtent);
    $('#editDryStorageModalInitialOakExtent').val(initialExt);


    $('#editDryStorageModal').modal('show');
}

$("#editDryStorageOakForm").submit(function( event ) {
    let dryingStExt =   parseFloat($('#editDryStorageModalDryingExtent').val());
    let initExtent =    parseFloat($('#editDryStorageModalInitialOakExtent').val());
    let extent =        parseFloat($('#editDryStorageModalVol').val());

    console.log("ts:"+dryingStExt+"in:"+initExtent+"cur:"+extent);

    if (extent>(dryingStExt+initExtent)) {
        if(!confirm("Введенная Вами кубатура превышает кубатуру в сушке на "+(extent-(dryingStExt+initExtent)).toFixed(3)+" м3! Продолжить?")){
            event.preventDefault();
        }
    }
});



function resetDryExtent(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#resetItemId').val(trId);
    console.log("row id: "+trId);

    let code = $(trObj).find('th:eq(0)').text();

    $('#resetItemModalConfirmation').text(" Вы уверены, что хотите обнулить кубатуру партии №"+code+"? Это действие не возвращает материал на предыдущий склад!");
    $('#resetModal').modal('show');
}

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
//             url: "/createPackageOakObject-" + userID + "-" + breedID,
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
//                     $('#sendForPackageModalQualityOak').val('');
//                     $('#sendForPackageModalWidthOak').val('');
//                     $('#sendForPackageModalCountOak').val('');
//                     // clearTableForTransportationOak();
//                     tableForTransportationOak.clear().draw();
//                 }else {
//                     location.reload();
//                 }
//             },
//             error: function () {
//                 alert("Error");
//             }
//         });
//     } else {
//         alert("Заполните все поля!");
//     }
// }