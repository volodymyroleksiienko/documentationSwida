function selectButton(selectId){
    $('#'+selectId+' option').prop('selected', true);
    $('#'+selectId).trigger('chosen:updated');
}

function deselectButton(selectId){
    $('#'+selectId+' option:selected').prop("selected",false);
    $('#'+selectId).trigger('chosen:updated');
}

function selectAll() {
    console.log("click");
    // $('#selectDescription option').prop('selected', true); // Selects all options
    var options = document.getElementById('selectDescription_chosen').getElementsByTagName('li');
    for(var i = 0; i<options.length;i++){
        options[i].click();
        console.log(options[i]);
    }
}


function editPackage(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#editPackagesModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfPackage = $(trObj).find('th:eq(0)').text();
    $('#editPackageModalHeader').text("Редактировать пачку №"+codeOfPackage);

    var code =                          $(trObj).find('th:eq(0)').text();
    var material =                      $(trObj).find('td:eq(0)').text();
    var description =                   $(trObj).find('td:eq(1)').text();
    var thickness =                     $(trObj).find('td:eq(2)').text();
    var width =                         $(trObj).find('td:eq(3)').text();
    var length =                        $(trObj).find('td:eq(4)').text();
    var count =                         $(trObj).find('td:eq(5)').text();
    var extent =                        $(trObj).find('td:eq(6)').text();
    var widthPc =                       $(trObj).find('td:eq(7)').text();
    var heightPc =                      $(trObj).find('td:eq(8)').text();
    var lengthFact =                    $(trObj).find('td:eq(9)').text();
    var heightWidth =                   $(trObj).find('td:eq(10)').text();
    var dryDescsCount =                 $(trObj).find('td:eq(11)').text();

    $('#editPackageModalCode')          .val(code);
    $('#editPackageModalMaterial')      .val(material);
    $('#editPackageModalMaterialDescr') .val(description);
    $('#editPackageHeight')             .val(thickness);
    $('#editPackageWidth')              .val(width);
    $('#editPackageLength')             .val(length);
    $('#editPackageModalCount')         .val(count);
    $('#editPackageModalExtent')        .val(extent);
    $('#editPackageModalWidthPc')       .val(widthPc);
    $('#editPackageHeightPc')           .val(heightPc);
    $('#editPackageModalLengthFact')    .val(lengthFact);
    $('#editPackageModalHeightWidth')   .val(heightWidth);

    $('#editPackageModalInitialDescsCount') .val(count);
    $('#editPackageModalDryDescsCount')     .val(dryDescsCount);


    $('#editPackageModal').modal('show');
}

function calculateExtent() {
    let thickness = parseFloat($('#editPackageHeight').val());
    let width =     parseFloat($('#editPackageWidth').val());
    let length =    parseFloat($('#editPackageLength').val());
    // let count =     parseInt($('#editPackageModalWidthPc').val())*parseInt($('#editPackageHeightPc').val());
    let count =        parseFloat($('#editPackageModalCount').val());

    if (!Number.isNaN(thickness) && !Number.isNaN(width) && !Number.isNaN(length) && !Number.isNaN(count)) {
        let res = (thickness / 1000) * (width / 1000) * (length / 1000) * count;
        $('#editPackageModalExtent').val(res.toFixed(3));
        $('#editPackageModalCount').val(count);
    }else {
        $('#editPackageModalExtent').val(0.000);
        $('#editPackageModalCount').val(0);
        console.log("NaN value");
    }
}

$("#editPackageForm").submit(function( event ) {
    let dryCount =     parseFloat($('#editPackageModalDryDescsCount').val());
    let initialCount = parseFloat($('#editPackageModalInitialDescsCount').val());
    let count =        parseFloat($('#editPackageModalCount').val());

    console.log("raw:"+dryCount+"in:"+initialCount+"cur:"+count);

    if (count>(dryCount+initialCount)) {
        if(!confirm("Введенное Вами количество досок превышает количество на сухом складе на "+(count-(dryCount+initialCount))+" шт.! Продолжить?")){
            event.preventDefault();
        }
    }
    // event.preventDefault();
});


// $("#addPackagedProductWithoutHistoryForm").on('submit', function( event ) {
//     event.preventDefault();
//     // return false;
//     console.log("submit");
// });


function addPackageIntoDelivery(btnObj) {
    var trObj = btnObj.parentElement.parentElement;
    var trId =  $(trObj).attr('id');
    $('#selectedPackModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfSelectedPackage = $(trObj).find('th:eq(0)').text();

    $('#sendPackageHeader').text("Добавить пачку "+codeOfSelectedPackage+" к существующей доставке");

    $('#sendPackModal').modal('show');
}

function packageUnform(btnObj) {
    var trObj =     btnObj.parentElement.parentElement;
    var trId =      $(trObj).attr('id');
    $('#unformPackModalId').val(trId);
    console.log(  $(trObj).attr('id'));
    var codeOfPackage = $(trObj).find('th:eq(0)').text();

    $('#unformPackModalConfirmation').text("Вы уверены, что хотите расформировать пачку "+codeOfPackage+" ?");
    $('#unformPackModal').modal('show');
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
} );

function sendRequestToCreateDeleviry() {
    var arrOfTr = document.getElementById("listOfPackageId").getElementsByTagName("tr");
    console.log("arr length = "+arrOfTr.length);

    var idOfTruck1 =            $("#addIdOfTruck").val();
    var name1 =                 $("#driverCredentionals").val();
    var phone1 =                $("#driverPhone").val();
    var numberOfTruck1 =        $("#numberOfTruck").val();
    var numberOfTrailer1 =      $("#numberOfTrailer").val();
    var date1 =                 $("#packDateInput").val();
    var time1 =                 $("#packTimeInput").val();
    var breedID =               $("#breedID").val();
    var userID =                $("#userID").val();
    var contractName1 =         $("#packContractId-hidden").val();
    var deliveryDestination1 =  $("#packDeliveryDestination").val();
    var description1 =          $("#addDeliveryDescription").val();


    var arrayOfId = [];

    for (var i = 0; i<arrOfTr.length;i++){
        arrayOfId[i] = $(arrOfTr[i]).find('td:eq(0)').text();
        // console.log(arrayOfId[i]);
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
        });
    } else {
        alert("Заполните все поля!");
    }

}