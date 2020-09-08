function sendForPackagesStorageOak(btnObj) {

    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#sendForDryingModalIdOak').val(trId);
    console.log("Row id: "+$(trObj).attr('id'));

    var codeOfRawStorage =      $(trObj).find('th:eq(0)').text();
    var size =                  $(trObj).find('td:eq(2)').text();
    var quality =               $(trObj).find('td:eq(5)').text();
    var length =                $(trObj).find('td:eq(6)').text();


    $('#sendForPackageModalOakHeader').text("Формирование пачек из  "+codeOfRawStorage);

    $('#sendForPackageModalSizeOak')        .val(size);
    $('#sendForPackageModalQualityOak')     .val(quality);
    $('#sendForPackageModalLengthOak')      .val(length);

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
    var codeOfRawStorage = $(trObj).find('th:eq(0)').text();
    $('#editDryStorageModalHeader').text("Редактировать "+codeOfRawStorage);

    var code = $(trObj).find('th:eq(0)').text();
    var material = $(trObj).find('td:eq(0)').text();
    var description = $(trObj).find('td:eq(1)').text();
    var thickness = $(trObj).find('td:eq(2)').text();
    var extent = $(trObj).find('td:eq(3)').text();
    // var length = $(trObj).find('td:eq(4)').text();
    var desc = $(trObj).find('td:eq(4)').text();
    // var extent = $(trObj).find('td:eq(6)').text();

    $('#editDryStorageModalCode').val(code);
    $('#editDryStorageModalMaterial').val(material);
    $('#editDryStorageModalMaterialDescr').val(description);
    $('#editDryStorageModalThickness').val(thickness);
    // $('#editDryStorageModalWidth').val(width);
    // $('#editDryStorageModalLength').val(length);
    $('#editDryStorageModalVolume').val(extent);
    $('#editDryStorageModalDesc').val(desc);


    $('#editDryStorageModal').modal('show');
}

// ADD Package OAK
function sendRequestCreatePackageOak(btnObj) {
    var breedID = $("#breedId").val();
    var userID = $("#userId").val();
    var idOfDryStorageOak = $("#sendForDryingModalIdOak").val();
    console.log("id " + idOfDryStorageOak);
    var codeOfPackage1 = $('#sendForPackageModalCodeOak').val();
    var quality1 = $('#sendForPackageModalQualityOak').val();
    var sizeOfHeight1 = $('#sendForPackageModalSizeOak').val();
    var long1 = $('#sendForPackageModalLengthOak').val();

    var tableBody = document.getElementById('listOfPackageId');
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
            url: "/createPackageOakObject-" + userID + "-" + breedID,
            contextType: "application/json",
            data: {
                idOfDryStorage: idOfDryStorageOak,
                codeOfPackage: codeOfPackage1,
                quality: quality1,
                sizeOfHeight: sizeOfHeight1,
                length: long1,
                arrayOfDesk: arrOfDesk
            },
            traditional: true,
            success: function (extent) {
                console.log(extent);
                // location.reload();
            },
            error: function () {
                alert("Error");
            }
        });
    } else {
        alert("Заполните все поля!");
    }
}