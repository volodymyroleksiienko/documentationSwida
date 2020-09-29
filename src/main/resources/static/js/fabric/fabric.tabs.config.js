$(document).ready( function () {
    // Distribution table start
    $('#distribution-tab-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            { className: "display-none", "targets": [ -1, -2, -3 ] }
        ]
    });
    // Distribution table end
    console.log("load on ready");
    // Modal inner table start
    let tableOfDistribution = $('#tableOfDistribution').DataTable({
        "autoWidth": false,
        "bSort": false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false

    });
    // Modal inner table end

    // ADD ELEMENTS TO INNER TABLE LIST START
    $("#buttonForAddingDistributionRow").click(function () {
        let company = $("#addDistributionCompanyInner").val();
        let amount = parseFloat($("#addDistributionAmountInner").val());
        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

        if( company=="" || amount=="" ) {
            alert("Заполните все поля!");
        } else if (amount<=0.000){
            alert("Значение не может быть отрицательным либо равным 0!")
        } else {
            var row = [company, amount, button];

            console.log(row);

            $("#addDistributionCompanyInner").val("");
            $("#addDistributionAmountInner").val("");

            tableOfDistribution.row.add(row).draw();
        }
    });
    // ADD ELEMENTS TO INNER TABLE LIST END

    // DELETE BUTTON START
    $('#tableOfDistribution tbody').on( 'click', 'button', function () {
        tableOfDistribution.row( $(this).parents('tr') ).remove().draw();
    } );
    // DELETE BUTTON END

    $('#treestoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },

        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],

        // columns width
        "autoWidth": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ 5 ] },
        ]

    });

    // TOOGLE SELECTED START
    $('#treestoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END



    let rawstoragetable =  $('#rawstoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "135px"
            },
            { className: "display-none", "targets": [ -3 ] },
        ]
    });

    // TOOGLE SELECTED START
    $('#rawstoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END


    // ADD MATERIAL
    $('#sendRequestToAddInputButton').on('click',function() {
        let breedID = $("#breedId").val();
        let userID = $("#userId").val();

        let tree =          $("#addToRawStorageMaterial").val();

        let code =          $("#addToRawStorageCode").val();
        let breedDescr =    $("#addToRawStorageBreedDescription").val();
        let supplier =      $("#addToRawStorageSupplier").val();
        let supplierId =    $("#addToRawStorageSupplier-hidden").val();
        let thickness =     $("#addToRawStorageThickness").val();
        let width =         $("#addToRawStorageWidth").val();
        let length =        $("#addToRawStorageLength").val();
        let count =         $("#addToRawStorageCount").val();

        let button = "<button th:if=\"${btnConfig?.equals('btnON')}\" type=\"button\" class=\"btn btn-primary btn-sm\"  data-toggle=\"modal\" onclick=\"sendForDrying(this)\"><i class=\"fas fa-th\" data-toggle=\"tooltip\" title=\"В сушку\"></i></button> <button th:if=\"${btnConfig?.equals('btnON')}\" type=\"button\" class=\"btn btn-primary btn-sm\"  data-toggle=\"modal\" onclick=\"editRawStorage(this)\"><i class=\"fas fa-pen\" data-toggle=\"tooltip\" title=\"Редактировать\"></i></button> <button th:if=\"${btnConfig?.equals('btnON')}\" type=\"button\" class=\"btn btn-primary btn-sm\"  data-toggle=\"modal\" onclick=\"sendForPackagesStorage(this)\"><i class=\"fas fa-cubes\" data-toggle=\"tooltip\" title=\"Формирование пачек\"></i></button> <button th:if=\"${btnConfig?.equals('btnON')}\" type=\"button\" class=\"btn btn-primary btn-sm\" data-toggle=\"modal\" onclick=\"returnToIncome(this)\"><i class=\"fas fa-undo-alt\" data-toggle=\"tooltip\" title=\"Возвратить в приход\"></i></button>";


        if (code !== "" && supplier !== "" && thickness !== "" && width !== "" && length !== "" && count !== "" ) {
            $.ajax({
                method: "post",
                url: "/fabric/addDeskFromProvider-"+userID+'-'+breedID,
                contextType: "application/json",
                data: {
                    codeOfProduct: code,
                    breedDescription: breedDescr,
                    contrAgentId: supplierId,
                    sizeOfHeight: thickness,
                    sizeOfWidth: width,
                    sizeOfLong: length,
                    countOfDesk: count
                },
                traditional: true,
                success: function (obj) {
                    console.log(obj);
                    let id =            obj["rawStorageId"];
                    let treeStorageExt= obj["treeStorageExtent"];
                    let rawStorageExt = obj["rawStorageExtent"];

                    let row = [id, code, tree, breedDescr, thickness, width, length, count, rawStorageExt, treeStorageExt, supplier, button];

                    rawstoragetable.row.add(row).draw();

                    alert("Запись успешно добавлена!");
                },
                error: function () {
                    alert("Заполните все поля!");
                }
            });
        } else {
            alert("Заполните все поля!");
        }
    });







    let dryingtable = $('#dryingtable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,

        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ -2 ] },
        ]
    });


    // TOOGLE SELECTED START
    $('#dryingtable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END


    $('#drystoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,

        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ -2 ] }
        ]
    });

    // TOOGLE SELECTED START
    $('#drystoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END


    let drystoragetableOak = $('#drystoragetableOak').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,

        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "100px"
            },
            { className: "display-none", "targets": [ -2 ] }
        ]
    });


    // TOOGLE SELECTED START
    $('#drystoragetableOak tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END

    //    DRYING OAK START
    var tableForTransportationOak = $('#tableForTransportationOak').DataTable({
        // columns width
        "autoWidth": false,
        "order": [ 0, "desc" ],
        // "bSort": false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false,
        "columnDefs": [
            {
                "targets": -1,
                "orderable": false,
                "width": "30px"
            }
        ]
    });

    var tableForPackagesOak = $('#tableForPackagesOak').DataTable({
        // columns width
        "autoWidth": false,
        "order": [ 0, "desc" ],
        // "bSort": false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false,
        "columnDefs": [
            {
                "targets": -1,
                "orderable": false,
                "width": "30px"
            }
        ]
    });

    $( "#clearTableButton" ).click(function() {
        clearTable(tableForTransportationOak);
        $("#sendForPackageModalExtentOak").val('');
        $("#sendForPackageModalLengthOak").removeAttr("disabled");
        $("#sendForPackageModalSizeOak").removeAttr("disabled");
    })

    $( "#clearInitialPackageButton" ).click(function() {
        tableForPackagesOak.clear().draw();

        $("#addDeliveryPackageModalExtentOak").val('');
        $("#addDeliveryPackageModalSizeOak").val('');
        $("#addDeliveryPackageModalLengthOak").val('');
        $("#addDeliveryPackageModalWidthOak").val('');
        $("#addDeliveryPackageModalLengthOak").removeAttr("disabled");
        $("#addDeliveryPackageModalSizeOak").removeAttr("disabled");
    })


    function clearTable(tableName){
        tableName.clear()
                 .draw();
        sumWidth = 0;
        sumCount = 0;
        let extent =        $("#sendForPackageModalExtentOak");
        let length =        $("#sendForPackageModalLengthOak");
        let height =        $("#sendForPackageModalSizeOak");
        $('#sendForPackageModalWidthOak').val('');
        createPackageExtentCalc(tableForTransportationOak, extent, length, height);
        console.log("Erased!");
    }

    let sumWidth =      0;
    let sumCount =      0;


    $("#buttonForAddingOakRow").click(function () {
        let width =         $("#sendForPackageModalWidthOak").val();
        let count =         $("#sendForPackageModalCountOak").val();
        let extent =        $("#sendForPackageModalExtentOak");
        let length =        $("#sendForPackageModalLengthOak");
        let height =        $("#sendForPackageModalSizeOak");

        let maxExtent =     parseFloat($("#sendForPackagesMaxExtent").val());

        // console.log("Max extent: "+maxExtent);

        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

        let resExtent = (parseFloat(length.val())/1000) * (parseFloat(height.val())/1000) * (parseFloat(width)/1000) * parseInt(count);

        console.log("Extent of added row: "+resExtent);

        if( width==="" || count==="" ) {
            alert("Заполните ширину и количество досок!");
        } else if (width<0 || count<0) {
            alert("Значение не может быть отрицательным!");
        }else if(length.val()==="" || height.val()==="") {
            alert("Заполните толщину и длину!");
        }else if(resExtent>maxExtent){
            if (((resExtent-maxExtent)>=0.000) && ((resExtent-maxExtent)<=1)){
                console.log("Added row - max extent: "+resExtent-maxExtent);
                $('#maxExtentDiffAlert').removeClass("display-none");
                $('#maxExtentDiffAlert').addClass("warning-btn");

                $('#maxExtentDiff').text((resExtent - maxExtent).toFixed(5));
            }else {
                $('#maxExtentDiffAlert').addClass("display-none");
                $('#maxExtentDiffAlert').removeClass("warning-btn");
                alert("Объём превышает допустимый на " + (resExtent - maxExtent).toFixed(5) + " m3! Максимальное количество досок: " + Math.floor(maxExtent / ((parseFloat(width) / 1000) * (parseFloat(length.val()) / 1000) * (parseFloat(height.val()) / 1000))));
                $("#sendForPackageModalCountOak").val("");
            }
        } else {
            if (parseInt(count)===0) {
                $('#sendForPackageModalWidthOak').val(parseInt(width) + 10);
                $("#sendForPackageModalCountOak").val("");
            }else {
                $('#maxExtentDiffAlert').addClass("display-none");
                $('#maxExtentDiffAlert').removeClass("warning-btn");

                let d = [width, count, button];

                $("#sendForPackageModalLengthOak").attr("disabled", "disabled");
                $("#sendForPackageModalSizeOak").attr("disabled", "disabled");

                $("#sendForPackageModalWidthOak").val("");
                $("#sendForPackageModalCountOak").val("");

                tableForTransportationOak.row.add(d).draw();

                $('#sendForPackageModalCountOak').focus();

                createPackageExtentCalc(tableForTransportationOak, extent, length, height);

                $("#sendForPackagesMaxExtent").val(maxExtent - resExtent);
                console.log("Max ext afer adding row: " + parseFloat($("#sendForPackagesMaxExtent").val()));

                $('#sendForPackageModalWidthOak').val(parseInt(width) + 10);
            }
        }
    });


    $("#buttonForAddingExtraOakRow").click(function () {
        let width =         $("#sendForPackageModalWidthOak").val();
        let count =         $("#sendForPackageModalCountOak").val();
        let extent =        $("#sendForPackageModalExtentOak");
        let length =        $("#sendForPackageModalLengthOak");
        let height =        $("#sendForPackageModalSizeOak");

        let maxExtent =     parseFloat($("#sendForPackagesMaxExtent").val());
        // console.log("Max extent extra: "+maxExtent);

        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

        let resExtent = (parseFloat(length.val())/1000) * (parseFloat(height.val())/1000) * (parseFloat(width)/1000) * parseInt(count);
        console.log("Extent of extra row: "+resExtent);

        let d = [width, count, button];

        tableForTransportationOak.row.add(d).draw();

        createPackageExtentCalc(tableForTransportationOak, extent, length, height);

        $("#sendForPackagesMaxExtent").val(maxExtent-resExtent);
        console.log("Max ext after adding extra row: "+ parseFloat($("#sendForPackagesMaxExtent").val()));

        $('#maxExtentDiffAlert').removeClass("warning-btn");
        $('#maxExtentDiffAlert').addClass("display-none");

        $("#sendForPackageModalWidthOak").attr("disabled", "disabled");
        $("#sendForPackageModalCountOak").attr("disabled", "disabled");

        $("#sendForPackageModalLengthOak").attr("disabled", "disabled");
        $("#sendForPackageModalSizeOak").attr("disabled", "disabled");

        $("#addDeliveryPackageModalWidthOak").attr("disabled", "disabled");
        $("#addDeliveryPackageModalCountOak").attr("disabled", "disabled");

        $("#addDeliveryPackageModalLengthOak").attr("disabled", "disabled");
        $("#addDeliveryPackageModalSizeOak").attr("disabled", "disabled");

        $("#sendForPackageModalWidthOak").val("0");
        $("#sendForPackageModalCountOak").val("0");

        $("#addDeliveryPackageModalWidthOak").val("0");
        $("#addDeliveryPackageModalCountOak").val("0");
    });


////////////////////////////////////////////////////////////////////////////
    // ADD Package OAK
    $("#createPackageOak").click(function () {
    // function sendRequestCreatePackageOak(btnObj) {
        let breedID = $("#breedId").val();
        let userID = $("#userId").val();
        let idOfDryStorageOak = $("#sendForDryingModalIdOak").val();
        console.log("id " + idOfDryStorageOak);
        let codeOfPackage1 = $('#sendForPackageModalCodeOak').val();
        let quality1 = $('#sendForPackageModalQualityOak').val();
        let sizeOfHeight1 = $('#sendForPackageModalSizeOak').val();
        let long1 = $('#sendForPackageModalLengthOak').val();

        let tableBody = document.getElementById('listOfPackageId');
        let listTr = tableBody.getElementsByTagName('tr');

        let arrOfDesk = [];

        //fix one dimension array on controller
        arrOfDesk[0] = [];
        arrOfDesk[0][0] = "test";
        arrOfDesk[0][1] = "test";



        if (codeOfPackage1 != "" && quality1 != "" && sizeOfHeight1 != "" && long1 != "" ) {
            for (let i = 1; i <= listTr.length; i++) {
                let width = $(listTr[i - 1]).find('td:eq(0)').text();
                let count = $(listTr[i - 1]).find('td:eq(1)').text();

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
                    if (extent>0) {
                        console.log(extent);
                        let trObj = document.getElementById(idOfDryStorageOak);
                        console.log(trObj);

                        $(trObj).find('td:eq(3)').text(extent);

                        sendForPackagesStorageOak(idOfDryStorageOak);

                        $('#sendForPackageModalCodeOak').val('');
                        $('#sendForPackageModalCodeOak').focus();
                        // $('#sendForPackageModalQualityOak').val('');
                        $('#sendForPackageModalWidthOak').val('');
                        $('#sendForPackageModalCountOak').val('');
                        $('#sendForPackageModalExtentOak').val('0.000');
                        $("#sendForPackageModalLengthOak").removeAttr("disabled");
                        $("#sendForPackageModalSizeOak").removeAttr("disabled");
                        tableForTransportationOak.clear().draw();
                    }else {
                        location.reload();
                    }
                },
                error: function () {
                    alert("Заполните все поля!");
                }
            });
        } else {
            alert("Заполните все поля!");
        }
    });
    //////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    // ADD Package OAK
    $("#createPackageFromRawOak").click(function () {
    // function sendRequestCreatePackageOak(btnObj) {
        var breedID = $("#breedId").val();
        var userID = $("#userId").val();
        var idOfRawOak = $("#addOakInitialPackageCodeAdditionalExtentModalIdOak").val();
        console.log("id " + idOfRawOak);
        var codeOfPackage1 = $('#sendForPackageModalCodeOak').val();
        var quality1 = $('#sendForPackageModalQualityOak').val();
        var sizeOfHeight1 = $('#sendForPackageModalSizeOak').val();
        var long1 = $('#sendForPackageModalLengthOak').val();

        var extent1 = $('#sendForPackageModalExtentOak').val();

        var tableBody = document.getElementById('listOfPackageId');
        var listTr = tableBody.getElementsByTagName('tr');

        var arrOfDesk = [];

        //fix one dimension array on controller
        arrOfDesk[0] = [];
        arrOfDesk[0][0] = "test";
        arrOfDesk[0][1] = "test";

        if ( parseFloat(extent1) !== 0.00 ) {
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
                url: "/createRawPackageOakObject-" + userID + "-" + breedID,
                contextType: "application/json",
                data: {
                    idOfRawStorage: idOfRawOak,
                    codeOfPackage: codeOfPackage1,
                    quality: quality1,
                    sizeOfHeight: sizeOfHeight1,
                    length: long1,
                    extent: extent1,
                    arrayOfDesk: arrOfDesk
                },
                traditional: true,
                success: function (extent) {

                    console.log("Extent: "+extent);
                    let trObj = document.getElementById(idOfRawOak);
                    console.log(trObj);

                    $(trObj).find('td:eq(3)').text(extent);

                    // $('#sendForPackageModalExtentOak').val(extent);

                    $('#sendForPackageModalCodeOak').val('');

                    $("#sendForPackageModalLengthOak").removeAttr("disabled");
                    $("#sendForPackageModalSizeOak").removeAttr("disabled");

                    clearTable(tableForTransportationOak);

                        // location.reload();
                },
                error: function () {
                    alert("Заполните все поля!");
                }
            });
        } else {
            alert("Заполните все поля!");
        }
    });
    //////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    // requestCreatePackageOak
    $("#createInitialPackageFromRawOak").click(function () {
        // function sendRequestCreatePackageOak(btnObj) {
        let breedID =               $("#breedIdPack").val();
        let userID =                $("#userIdPack").val();
        let codeOfInitialPackage1 = $('#addOakInitialPackageCode').val();
        let breedDescription1 =     $('#addOakInitialPackageDescr').val();
        let supplier1 =             $('#addOakInitialPackageSupplier-hidden').val();
        let sizeOfHeight1 =         $('#addDeliveryPackageModalSizeOak').val();
        let length1 =               $('#addDeliveryPackageModalLengthOak').val();

        let tableBody = document.getElementById('listOfOakPackageRowsId');
        let listTr = tableBody.getElementsByTagName('tr');

        let treeStorageId =  $('#addOakInitialPackageTreeStorage-hidden').val();

        let arrOfDesk = [];

        var extent1 = $('#addDeliveryPackageModalExtentOak').val();

        //fix one dimension array on controller
        arrOfDesk[0] = [];
        arrOfDesk[0][0] = "test";
        arrOfDesk[0][1] = "test";



        if (codeOfInitialPackage1 !== "" && sizeOfHeight1 !== "" && length1 !== "" && (treeStorageId !== "" ||  supplier1 !== "")) {
            for (let i = 1; i <= listTr.length; i++) {
                let width = $(listTr[i - 1]).find('td:eq(0)').text();
                let count = $(listTr[i - 1]).find('td:eq(1)').text();

                arrOfDesk[i] = [];
                arrOfDesk[i][0] = width;
                arrOfDesk[i][1] = count;
            }

            console.log(arrOfDesk);

            $.ajax({
                method: "post",
                url: "/createInitialPackageOakObject-" + userID + "-" + breedID,
                contextType: "application/json",
                data: {
                    codeOfPackage: codeOfInitialPackage1,
                    breedDescription: breedDescription1,
                    supplier: supplier1,
                    treeStorageId:treeStorageId,
                    sizeOfHeight: sizeOfHeight1,
                    length: length1,
                    extent: extent1,
                    arrayOfDesk: arrOfDesk
                },
                traditional: true,
                success: function () {
                        location.reload();
                },
                error: function () {
                    alert("Заполните все поля!");
                }
            });
        } else {
            alert("Заполните все поля!");
        }
    });
    /////////////////////////////////////////////////////////////////////


    $("#buttonForAddingDeliveryOakRow").click(function () {
        let width =         $("#addDeliveryPackageModalWidthOak").val();
        let count =         $("#addDeliveryPackageModalCountOak").val();
        let extent =        $("#addDeliveryPackageModalExtentOak");
        let length =        $("#addDeliveryPackageModalLengthOak");
        let height =        $("#addDeliveryPackageModalSizeOak");
        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить'></i></button>";

        if( width==="" || count==="" ) {
            alert("Заполните ширину и количество досок!");
        } else if (width<0 || count<0){
            alert("Значение не может быть отрицательным!")
        }else if(length.val()==="" || height.val()==="") {
            alert("Заполните размер и длину!");
        } else {
            if (parseInt(count)===0) {
                $('#addDeliveryPackageModalWidthOak').val(parseInt(width) + 10);
                $("#sendForPackageModalCountOak").val("");
            }else {
                let d = [width, count, button];
                console.log(d);

                $("#addDeliveryPackageModalLengthOak").attr("disabled", "disabled");
                $("#addDeliveryPackageModalSizeOak").attr("disabled", "disabled");

                $("#addDeliveryPackageModalWidthOak").val("");
                $("#addDeliveryPackageModalCountOak").val("");

                tableForTransportationOak.row.add(d).draw();

                $('#addDeliveryPackageModalCountOak').focus();

                createPackageExtentCalc(tableForTransportationOak, extent, length, height);
                $('#addDeliveryPackageModalWidthOak').val(parseInt(width) + 10);
            }
        }
    });

    $("#buttonForAddingInitialPackageOakRow").click(function () {
        let width =         $("#addDeliveryPackageModalWidthOak").val();
        let count =         $("#addDeliveryPackageModalCountOak").val();
        let extent =        $("#addDeliveryPackageModalExtentOak");
        let length =        $("#addDeliveryPackageModalLengthOak");
        let height =        $("#addDeliveryPackageModalSizeOak");
        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить'></i></button>";

        if( width==="" || count==="" ) {
            alert("Заполните ширину и количество досок!");
        } else if (width<0 || count<0){
            alert("Значение не может быть отрицательным!")
        }else if(length.val()==="" || height.val()==="") {
            alert("Заполните размер и длину!");
        } else {
            if (parseInt(count)===0) {
                $('#addDeliveryPackageModalWidthOak').val(parseInt(width) + 10);
                $("#sendForPackageModalCountOak").val("");
            }else {
                let d = [width, count, button];
                console.log(d);

                $("#addDeliveryPackageModalLengthOak").attr("disabled", "disabled");
                $("#addDeliveryPackageModalSizeOak").attr("disabled", "disabled");

                $("#addDeliveryPackageModalWidthOak").val("");
                $("#addDeliveryPackageModalCountOak").val("");

                tableForPackagesOak.row.add(d).draw();

                $('#addDeliveryPackageModalCountOak').focus();

                createPackageExtentCalc(tableForPackagesOak, extent, length, height);
                $('#addDeliveryPackageModalWidthOak').val(parseInt(width) + 10);
            }
        }
    });


    $('#tableForTransportationOak tbody').on( 'click', 'button', function () {
        tableForTransportationOak.row( $(this).parents('tr') ).remove().draw();

        if ( ! tableForTransportationOak.data().any() ) {
            $("#sendForPackageModalLengthOak").removeAttr("disabled");
            $("#sendForPackageModalSizeOak").removeAttr("disabled");

            $("#addDeliveryPackageModalLengthOak").removeAttr("disabled");
            $("#addDeliveryPackageModalSizeOak").removeAttr("disabled");
        }

        let maxInitExtent = parseFloat($("#sendForPackagesMaxInitExtent").val());

        let extent = $("#sendForPackageModalExtentOak");
        let length = $("#sendForPackageModalLengthOak");
        let height = $("#sendForPackageModalSizeOak");

        let extentD = $("#addDeliveryPackageModalExtentOak");
        let lengthD = $("#addDeliveryPackageModalLengthOak");
        let heightD = $("#addDeliveryPackageModalSizeOak");

        let maxExtent =  parseFloat($("#sendForPackagesMaxExtent").val());

        let befDel =  parseFloat($("#sendForPackageModalExtentOak").val());
        console.log("Before delete: "+befDel);

        createPackageExtentCalc(tableForTransportationOak, extent, length, height);
        createPackageExtentCalc(tableForTransportationOak, extentD, lengthD, heightD);

        let aftDel =  parseFloat($("#sendForPackageModalExtentOak").val());
        console.log("After delete: "+aftDel);

        $("#sendForPackagesMaxExtent").val(maxExtent+(befDel-aftDel));
        console.log("max after delete: "+  $("#sendForPackagesMaxExtent").val());

        if (aftDel<maxInitExtent){
            $("#sendForPackageModalWidthOak").removeAttr("disabled");
            $("#sendForPackageModalCountOak").removeAttr("disabled");

            $("#addDeliveryPackageModalWidthOak").removeAttr("disabled");
            $("#addDeliveryPackageModalCountOak").removeAttr("disabled");

            $("#sendForPackageModalWidthOak").val("");
            $("#sendForPackageModalCountOak").val("");

            $("#addDeliveryPackageModalWidthOak").val("");
            $("#addDeliveryPackageModalCountOak").val("");
        }else {

        }
        console.log("Comparison : aftDel="+(aftDel)+"; maxInit="+maxInitExtent);
        console.log("Max extent after check: "+parseFloat($("#sendForPackagesMaxExtent").val()));
    } );

    $('#tableForPackagesOak tbody').on( 'click', 'button', function () {
        tableForPackagesOak.row( $(this).parents('tr') ).remove().draw();

        if ( ! tableForPackagesOak.data().any() ) {
            $("#addDeliveryPackageModalLengthOak").removeAttr("disabled");
            $("#addDeliveryPackageModalSizeOak").removeAttr("disabled");
        }

        let extentD = $("#addDeliveryPackageModalExtentOak");
        let lengthD = $("#addDeliveryPackageModalLengthOak");
        let heightD = $("#addDeliveryPackageModalSizeOak");

        createPackageExtentCalc(tableForPackagesOak, extentD, lengthD, heightD);
    } );
    //    DRYING OAK END

    //PACKAGES 1 START
    let table = $('#packagedproducttable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "iDisplayLength": 50,
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,
        // "select": {
        //     "style": 'os'
        // },
        // 'select': true,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "100px"
            },
            { className: "display-none", "targets": [ -3 ] }
        ]

    });


    //            SELECT
    $('#packagedproducttable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //            SELECT


    $( "#buttonForTransportation" ).click(function() {

        modalPackagesTable.clear().draw();
        let newData = ( table.rows( '.selected' ).data() );
        let array = [];

        let extentS = 0.0;

        for (let i = newData.length - 1; i >= 0; i--) {
            var id =			newData[i][0];
            var code = 			newData[i][1];
            var material = 		newData[i][2];
            var description = 	newData[i][3];
            var thickness = 	newData[i][4];
            var width = 		newData[i][5];
            var length = 		newData[i][6];
            var count = 		newData[i][7];
            var extent = 		newData[i][8];
            extentS =           extentS+parseFloat(extent);
            var heightPc = 		newData[i][9];
            var widthPc = 		newData[i][10];
            var wh = heightPc.toString()+"/"+widthPc.toString();
            var button =        "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

            var d=[id, code, material, description, thickness, width, length, count, extent, wh, button];

            array[i] = id;

            modalPackagesTable.row.add(d).draw( false );
        }
        console.log("arr: "+array);

        $("#deliveryExtent").val(extentS.toFixed(3));
        $('#sendForTransportationModal').modal('show');
    });


    var modalPackagesTable = $('#tableForTransportation').DataTable({
        // columns width
        "autoWidth": false,
        "bSort" : false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": true,
                "searchable": true
            },
            {
             className: "display-none", "targets": [ 0 ]
            }
        ]

    });


    $('#tableForTransportation tbody').on( 'click', 'button', function () {
        modalPackagesTable.row( $(this).parents('tr') ).remove().draw();

        let newData = (modalPackagesTable.rows( ).data() );

        let extentS = 0.0;

        for (let i = newData.length - 1; i >= 0; i--) {
            var extent = 		newData[i][8];
            extentS = extentS+parseFloat(extent);
            console.log(extentS);
        }

        console.log(extentS);

        $("#deliveryExtent").val(extentS.toFixed(3));
    } );
    //PACKAGES 1 END



    //PACKS2 start
    var oakTable = $('#packageOakTableId').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        // columns width
        "iDisplayLength": 50,
        // "select": {
        //     "style": 'os'
        // },
        "autoWidth": false,
        "bSort":false,
        "info": false,
        // 'select': true,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 1 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -2,
                "orderable": false,
                "width": "140px"
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            }
        ]

    });


    // TOOGLE SELECTED START
    $('#packageOakTableId tbody').on( 'click', '.outer-table', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END


    var modalOakPackagesTable = $('#tableForOakTransportation').DataTable({
        // columns width
        "autoWidth": false,
        "bSort" : false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 1 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "30px"
            }

        ]

    });


    $('#tableForOakTransportation tbody').on( 'click', 'button', function () {
        var idOfParent = 'parent'+this.parentElement.parentElement.getElementsByClassName("details-control")[0].getAttribute("id");
        this.parentElement.parentElement.remove();
        document.getElementById(idOfParent).remove();

        let extentS = 0;
        let extentR = 0;

        $("#listOfPackagesOakId").find("tr").each(function() {
            extentR = parseFloat($(this).find('td.extentValue').text());
            console.log(extentR);
            if (!Number.isNaN(extentR)){
                extentS = extentS +extentR;
            }
        });

        console.log(extentS);

        $("#deliveryOakExtent").val(extentS.toFixed(3));

    } );


    $( "#buttonForOakTransportation" ).click(function() {
        modalOakPackagesTable.clear().draw();

        let newData =           document.getElementsByClassName("selected");
        let tBody =             document.getElementById('listOfPackagesOakId');

        tBody.deleteRow(0);

        let extentS = 0;
        let extentR = 0;

        for(let i=0;i<newData.length;i++){
            let tmp =            newData[i];
            let clone =          tmp.cloneNode(true);
            let tdId =           clone.getElementsByClassName("details-control")[0];

            let delBtn =         clone.getElementsByClassName("delete-btns")[0];
            let visibleBtn =     clone.getElementsByClassName("invisible-btn")[0];
            delBtn.setAttribute("style", "display:none;");
            visibleBtn.setAttribute("style", "display:block;");

            let idOfPackage=tdId.getAttribute("id");
            let idOfPackageParent="parent"+idOfPackage;
            tdId.setAttribute("id",'modal'+idOfPackage);

            console.log(clone);
            console.log("parent: "+idOfPackage+" packageparent: "+idOfPackageParent);

            let insideTr =       $(clone).closest('tr').html();
            let trObj =          $.parseHTML("<tr role='row' class='selectMainTrPackage'>"+insideTr+"</tr>");

            let trParent = document.getElementById(idOfPackageParent);
            let trParentClone = trParent.cloneNode(true);

            trParentClone.setAttribute("id",'parentmodal'+idOfPackage);
            console.log(trParentClone);

            let delInnerBtn1 =         trParentClone.getElementsByClassName("delete-btns");

            Array.prototype.filter.call(delInnerBtn1, function(testElement){
                return testElement.setAttribute("style", "display:none;");
            });


            $('#listOfPackagesOakId').append(trObj);
            $('#listOfPackagesOakId').append(trParentClone);
        }

        $("#listOfPackagesOakId").find("tr").each(function() {
            extentR = parseFloat($(this).find('td.extentValue').text());
            console.log(extentR);
            if (!Number.isNaN(extentR)){
                extentS = extentS +extentR;
            }
        });

        console.log(extentS);

        $("#deliveryOakExtent").val(extentS.toFixed(3));

        $('#sendOakListForTransportationModal').modal('show');
    });

    //PACKS 2 end



    $('#transportationtable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        // columns width
        "autoWidth": false,
        "bSort": false,
        "info": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [  ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "130px"
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            },

            { className: "display-none", "targets": [ 11 ] }

        ]

    });


    $('#transportationTableOak').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        // columns width
        "autoWidth": false,
        "bSort": false,
        "info": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [  ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "orderable": false,
                "width": "130px"
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            },
            { className: "display-none", "targets": [ 11 ] }
        ]

    });

    function showPage() {
        document.getElementById("L2").style.display = "none";
        document.getElementById("myTabContent").style.display = "block";
    }

    setTimeout(showPage, 500);
})