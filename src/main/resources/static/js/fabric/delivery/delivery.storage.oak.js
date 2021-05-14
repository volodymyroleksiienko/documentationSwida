

function addOakDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    // let trId =  $(trObj).attr('id');
    var trId =  $(trObj).find('td:eq(0)').attr('id');
    console.log("row id: "+trId);
    $('#driverOakDeliveryId').val(trId);

    $('#addDeliveryPackageModalCountOak')       .val('');
    $('#addDeliveryPackageModalWidthOak')       .val(100);

    $('#addDriverPackageModalOak').modal('show');

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

// Ajax
function sendRequestCreateDeliveryPackageOak(btnObj) {
    var deliveryId1 = $("#driverOakDeliveryId").val();
    var codeOfPackage1 = $('#addDeliveryPackageModalCodeOak').val();
    var quality1 = $('#addDeliveryPackageModalQualityOak').val();
    var sizeOfHeight1 = $('#addDeliveryPackageModalSizeOak').val();
    var long1 = $('#addDeliveryPackageModalLengthOak').val();
    var breedID1 = $("#breedId").val();


    console.log("delivery id:" + deliveryId1+"\npackage: "+codeOfPackage1+"\nqual: "+quality1+ "\nheight: "+sizeOfHeight1+"\nlength: "+long1);


    var tableBody = document.getElementById('listOfOakPackageId');
    var listTr = tableBody.getElementsByTagName('tr');

    var arrOfDesk = [];

    //fix one dimension array on controller
    arrOfDesk[0] = [];
    arrOfDesk[0][0] = "test";
    arrOfDesk[0][1] = "test";



    if (codeOfPackage1 != "" && quality1 != "" && sizeOfHeight1 != "" && long1 != "" ) {
        for (var i = 1; i <= listTr.length; i++) {
            var width = $(listTr[i - 1]).find('td:eq(0)').text();
            var count = $(listTr[i - 1]).find('td:eq(1)').text();

            arrOfDesk[i] = [];
            arrOfDesk[i][0] = width;
            arrOfDesk[i][1] = count;
        }

        console.log(arrOfDesk);

        $.ajax({
            method: "post",
            url: "/createPackageOakObjectForExistDeliveryDoc",
            contextType: "application/json",
            data: {
                breedID: breedID1,
                arrayOfDesk: arrOfDesk,
                deliveryId: deliveryId1,
                codeOfPackage: codeOfPackage1,
                quality: quality1,
                sizeOfHeight: sizeOfHeight1,
                length: long1
            },
            traditional: true,
            success: function () {
                location.reload();
            },
            error: function () {
                alert("Error");
            }
        });
    } else {
        alert("Заполните все поля!");
    }
}

function editOakDriverInfo(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    // let trId =  $(trObj).attr('id');
    let trId =              $(trObj).find('td:eq(0)').attr('id');
    console.log("row id: "+trId);
    let packDate  =         $(trObj).find('td:eq(2)').text();
    let truck =             $(trObj).find('td:eq(3)').text();
    let truckNum =          $(trObj).find('td:eq(4)').text();
    let trailerNum  =       $(trObj).find('td:eq(5)').text();
    let packTime  =         $(trObj).find('td:eq(6)').text();
    let driver =            $(trObj).find('td:eq(7)').text();
    let phone =             $(trObj).find('td:eq(8)').text();
    let contract =          $(trObj).find('td:eq(9)').text();
    let destination =       $(trObj).find('td:eq(10)').text();
    let listOfWith =        $(trObj).find('td:eq(11)').text();
    let delDescription =    $(trObj).find('td:eq(12)').text();
    let contractId =        $(trObj).find('td:eq(13)').text();

    $('#driverOakId')                      .val(trId);
    $('#editOakIdOfTruck')                 .val(truck);
    $('#editOakDriverCredentionals')       .val(driver);
    $('#editOakDriverPhone')               .val(phone);
    $('#editOakNumberOfTruck')             .val(truckNum);
    $('#editOakNumberOfTrailer')           .val(trailerNum);
    $('#editOakPackDateInput')             .val(packDate);
    $('#editOakPackTimeInput')             .val(packTime);
    $('#editOakPackContractId')            .val(contract);
    $('#editOakPackDeliveryDestination')   .val(destination);
    $('#editOakPackDeliveryListOfWith')    .val(listOfWith);
    $('#editOakDeliveryDescription')       .val(delDescription);
    $('#editOakPackContractId-hidden')     .val(contractId);

    $('#editOakDeliveryModal').modal('show');
}

function editOakDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    // let trId =  $(trObj).attr('id');
    var trId =              $(trObj).find('td:eq(0)').attr('id');
    console.log("row id: "+trId);
    var packCode =          $(trObj).find('th:eq(0)').text();
    var quality =           $(trObj).find('td:eq(1)').text();
    var packSize  =         $(trObj).find('td:eq(2)').text();
    var packLen  =          $(trObj).find('td:eq(3)').text();
    var summaryWidth  =     $(trObj).find('td:eq(4)').text();
    var descsCount =        $(trObj).find('td:eq(5)').text();
    var extent =            $(trObj).find('td:eq(6)').text();

    $('#editOakPackModalHeader')         .text("Редактировать пачку "+packCode);

    $('#driverOakPackageId')             .val(trId);
    $('#editOakIdOfPack')                .val(packCode);
    $('#editOakQuality')                 .val(quality);
    $('#editOakPackSize')                .val(packSize);
    $('#editOakPackLength')              .val(packLen);
    $('#editOakPackSummaryLen')          .val(summaryWidth);
    $('#editOakPackDescCount')           .val(descsCount);
    $('#editOakPackExtent')              .val(extent);

    $('#editOakDeliveryPackageModal').modal('show');
}

function editOakDriverPackageItem(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    // let trId =  $(trObj).attr('id');
    let trId =              $(trObj).attr('id');
    console.log("row id: "+trId);

    let expandedObj = trObj.parentElement.parentElement.parentElement.parentElement;
    let expandedId = $(expandedObj).attr('id');
    console.log("string-id: "+expandedId);
    let packageId = expandedId.replace(/\D+/g, "");
    console.log("parent-id: "+packageId);

    $('#deleteOakPackageId').val(packageId);


    var width =             $(trObj).find('td:eq(0)').text();
    var count =             $(trObj).find('td:eq(1)').text();

    $('#driverOakPackageContentId')              .val(trId);
    $('#editOakDeliveryPackageContentWidth')     .val(width);
    $('#editOakDeliveryPackageContentCount')     .val(count);

    $('#editOakDeliveryPackageContentModal').modal('show');
}

function addOakDriverPackageItems(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    let packageCode =       $(trObj).find('th:eq(0)').text();

    console.log("row id: "+trId);

    $('#addOakDriverPackageItemsModalHeader').text("Добавить позицию к пачке "+ packageCode);
    $('#addOakDriverPackageItemsPackId').val(trId);

    $('#addOakDeliveryPackageContentModal').modal('show');
}

function deleteOakDriverPackage(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#deleteOakPackModalId').val(trId);
    console.log(  $(trObj).attr('id'));

    let expandedObj = trObj.parentElement.parentElement.parentElement.parentElement;
    let expandedId = $(expandedObj).attr('id');
    console.log("string-id: "+expandedId);
    let deliveryId = expandedId.replace(/\D+/g, "");
    console.log("id: "+deliveryId);

    $('#deleteOakDeliveryId').val(deliveryId);

    var codeOfPackage = $(trObj).find('th:eq(0)').text();

    $('#deleteOakPackModalConfirmation').text(" Вы уверены, что хотите окончательно удалить пачку "+codeOfPackage+" ?");
    $('#deleteOakPackModal').modal('show');
}

function deleteOakDriverPackageItem(btnObj) {
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
    $('#deleteOakPackItemModal').modal('show');
}

function deleteDriverInfoOak(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =  $(trObj).attr('id');
    $('#deleteRecordModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    let carNumber = $(trObj).find('td:eq(1)').text();

    $('#deleteModalConfirmation').text("Вы уверены, что хотите удалить выгрузку №"+carNumber+"?");
    $('#deleteConfirmationModal').modal('show');
}