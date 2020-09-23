function editOakPackage(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId = $(trObj).find('td:eq(0)').attr('id');
    $('#editPackageModalIdOak').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfPackage = $(trObj).find('th:eq(0)').text();
    $('#editOakPackageModalHeader').text("Редактировать пачку №"+codeOfPackage);

    var code =                          $(trObj).find('th:eq(0)').text();
    var quality =                       $(trObj).find('td:eq(1)').text();
    var size =                          $(trObj).find('td:eq(2)').text();
    var length =                        $(trObj).find('td:eq(3)').text();
    var sumLength =                     $(trObj).find('td:eq(4)').text();
    var count =                         $(trObj).find('td:eq(5)').text();
    var extent =                        $(trObj).find('td:eq(6)').text();

    $('#editPackageModalCodeOak')       .val(code);
    $('#editPackageModalQualityOak')    .val(quality);
    $('#editPackageModalSizeOak')       .val(size);
    $('#editPackageModalLengthOak')     .val(length);
    $('#editPackageModalSumLengthOak')  .val(sumLength);
    $('#editPackageModalDeskCountOak')  .val(count);
    $('#editPackageModalExtentOak')     .val(extent);

    $('#editOakPackageModal').modal('show');
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

    $('#editOakPackageId').val(packageId);


    var width =             $(trObj).find('td:eq(0)').text();
    var count =             $(trObj).find('td:eq(1)').text();

    $('#oakPackageContentId')            .val(trId);
    $('#editOakPackageContentWidth')     .val(width);
    $('#editOakPackageContentCount')     .val(count);

    $('#editOakPackageContentModal').modal('show');
}

function addOakPackageItems(btnObj) {
    let trObj = btnObj.parentElement.parentElement;
    let trId =              $(trObj).attr('id');
    let packageCode =       $(trObj).find('th:eq(0)').text();

    console.log("row id: "+trId);

    $('#addOakPackageItemsModalHeader').text("Добавить позицию к пачке "+ packageCode);
    $('#addOakPackageItemsPackId').val(trId);

    $('#addOakPackageContentModal').modal('show');
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
    $('#deleteOakPackItemModal').modal('show');
}

function addPackageIntoOakDelivery(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).find('td:eq(0)').attr('id');
    $('#selectedOakPackModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfSelectedPackage = $(trObj).find('th:eq(0)').text();

    $('#sendOakPackageHeader').text("Добавить пачку "+codeOfSelectedPackage+" к существующей доставке");

    $('#sendOakPackModal').modal('show');
}

function packageOakUnform(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).find('td:eq(0)').attr('id');
    $('#unformOakPackModalId').val(trId);
    console.log( trId);
    var codeOfPackage = $(trObj).find('th:eq(0)').text();

    $('#unformOakPackModalConfirmation').text("Вы уверены, что хотите расформировать пачку "+codeOfPackage+" ?");
    $('#unformOakPackModal').modal('show');
}

// listener for opening and closing
$('#transportationtable tbody').on('click', 'td.details-control', function () {
    var tr = $(this).closest('tr');
    var row = table.row( tr );

    if ( row.child.isShown() ) {
        // already open
        row.child.hide();
        tr.removeClass('shown');
    }
    else {
        // open row
        row.child( format(row.data()) ).show();
        tr.addClass('shown');
    }
});



// $('#packageOakTableId tbody').on( 'click', 'button', function () {
//     var trObj = this.parentElement.parentElement;
//     var trId =  $(trObj).attr('id');
//     $('#unformOakPackModalId').val(trId);
//     console.log(  $(trObj).attr('id'));
//     var codeOfPackage = $(trObj).find('th:eq(0)').text();
//
//     $('#unformOakPackModalConfirmation').text("Вы уверены, что хотите расформировать пачку "+codeOfPackage+" ?");
//     $('#unformOakPackModal').modal('show');
// } );


function createOakPackageForm() {
    var arrOfTr = document.getElementById("listOfOakPackagesId").getElementsByTagName("tr");
    console.log("arr length = "+arrOfTr.length);
    var packNumber1 =   $("#sendForPackageModalCodeOak").val();
    var quality1 =      $("#sendForPackageModalQualityOak").val();
    var size1 =         $("#sendForPackageModalSizeOak").val();
    var length1 =       $("#sendForPackageModalLengthOak").val();

    var breedID =       $("#breedID").val();
    var userID =        $("#userID").val();

    var arrOfDesk = [];

    var tableBody = document.getElementById('listOfOakPackagesId');
    var listTr = tableBody.getElementsByTagName('tr');
    //fix one dimension array on controller
    arrOfDesk[0] = [];
    arrOfDesk[0][0] = "test";
    arrOfDesk[0][1] = "test";

    for (let i = 1; i <= listTr.length; i++) {
        let width = $(listTr[i - 1]).find('td:eq(0)').text();
        let count = $(listTr[i - 1]).find('td:eq(1)').text();

        arrOfDesk[i] = [];
        arrOfDesk[i][0] = width;
        arrOfDesk[i][1] = count;
    }

    if (arrOfDesk[1][0]!="No data available in table" && packNumber1 != "" && quality1 != "" && size1 != "" && length1 != ""){

        $.ajax({
            method: "post",
            url: "/createPackageOakObject-"+userID+"-"+breedID,
            contextType: "application/json",
            data: {
                arrayOfDesk: arrOfDesk,
                codeOfPackage: packNumber1,
                quality: quality1,
                sizeOfHeight: size1,
                length: length1,
            },
            traditional: true,
            success: function () {
                location.reload();
            },
            error: function () {
                alert("Error");
            }
        })
        ;} else {
        alert("Заполните все поля!");
    }

}


function sendRequestToCreateDeleviryOak() {
    var arrOfTr = document.getElementsByClassName("selectMainTrPackage");
    console.log("arr length = "+arrOfTr.length);
    var idOfTruck1 = $("#addIdOfTruck").val();
    var name1 = $("#driverCredentionals").val();
    var phone1 = $("#driverPhone").val();
    var numberOfTruck1 = $("#numberOfTruck").val();
    var numberOfTrailer1 = $("#numberOfTrailer").val();
    var date1 = $("#packDateInput").val();
    var time1 = $("#packTimeInput").val();
    var contractName1 = $("#packContractId-hidden").val();
    var deliveryDestination1 = $("#packDeliveryDestination").val();
    var description1 = $("#addDeliveryDescription").val();
    var breedID = $("#breedID").val();
    var userID = $("#userID").val();


    var arrayOfId = [];

    for (var i = 0; i<arrOfTr.length;i++){
        var td = arrOfTr[i].getElementsByClassName("details-control")[0];
        arrayOfId[i] = td.getAttribute("id").replace('modal','');
        console.log(arrayOfId[i]);
    }

    if (arrayOfId[0]!="No data available in table" && idOfTruck1 != "" && name1 != "" && phone1 != "" && numberOfTruck1 != "" && numberOfTrailer1 != "" && date1 != "" && time1 != "" && contractName1 != "" && deliveryDestination1!="" && description1!=""){

        $.ajax({
            method: "post",
            url: "/createDeliveryDoc-"+userID+"-"+breedID,
            contextType: "application/json",
            data: {
                list: arrayOfId,
                name: name1,
                phone: phone1,
                idOfTruck: idOfTruck1,
                numberOfTruck: numberOfTruck1,
                numberOfTrailer: numberOfTrailer1,
                dateOfUnloading: date1,
                timeOfUnloading: time1,
                contractName: contractName1,
                deliveryDestination: deliveryDestination1,
                description: description1
            },
            traditional: true,
            success: function () {
                location.reload();
            },
            error: function () {
                alert("Error");
            }
        });}
    else {
        alert("Заполните все поля!");
    }
}