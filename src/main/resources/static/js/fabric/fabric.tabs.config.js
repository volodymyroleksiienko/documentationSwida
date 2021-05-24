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
        "bSort": true,
        "info": false,
        // "order": [ 0, "desc" ],

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
                "width": "40px"
            },
            { className: "display-none", "targets": [ 1, 7 ] },
        ]

    });


    $('#treeStorageSubTable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },

        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": true,
        "info": false,
        // "order": [ 0, "desc" ],

        // columns width
        "autoWidth": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": -1,
                "orderable": false,
                "width": "70px"
            },
        ]
    });


    var tableForcuttingTreeStorage = $('#tableForcuttingTreeStorage').DataTable({
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

    $('#tableForcuttingTreeStorage tbody').on( 'click', 'button', function () {
        tableForcuttingTreeStorage.row( $(this).parents('tr') ).remove().draw();
        calculateCutTreStorageSummary();
    } );

    $("#buttonForAddingCutTreeStorageItem").click(function () {
        let descr =         $("#cutTreeStorageDescr").val();
        let width =         $("#cutTreeStorageWidth").val();
        let count =         $("#cutTreeStorageCount").val();
        let extent =        $("#cutTreeStorageExtent");
        let length =        $("#cutTreeStorageLength").val();
        let height =        $("#cutTreeStorageSize").val();


        let button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить'></i></button>";


        if (descr!=='' && width!=='' && count!=='' && length!=='' && height!==''){
            if (parseInt(count)<=0){
                alert("Количество не может быть отрицательным либо равным нулю!");
                $("#cutTreeStorageCount").focus();
            }else if (parseInt(width)<=0){
                alert("Ширина не может быть отрицательной либо равной нулю!");
                $("#cutTreeStorageWidth").focus();
            }else if (parseInt(length)<=0){
                alert("Длина не может быть отрицательной либо равной нулю!");
                $("#cutTreeStorageLength").focus();
            }else if (parseInt(height)<=0){
                alert("Толщина не может быть отрицательной либо равной нулю!");
                $("#cutTreeStorageSize").focus();
            }else {
                let rowExtent = parseFloat(height) / 1000 * parseFloat(width) / 1000 * parseFloat(length) / 1000 * count;
                let d = [descr, height, width, length, count, rowExtent.toFixed(3), button];
                console.log(d);

                $("#cutTreeStorageWidth").val("");
                $("#cutTreeStorageCount").val("");

                tableForcuttingTreeStorage.row.add(d).draw();

                $('#cutTreeStorageCount').focus();

                extent.val('0.00');
                calculateCutTreStorageSummary();

                $('#cutTreeStorageWidth').val(parseInt(width) + 10);
            }
        }else {
            alert("Заполните все поля!");
        }
    });


    ///////////////////////////////////////////////////////////////////

    class TreeStorageListDto{
          codeOfProduct;
          breedId;
          userId;
          extent;
          recycleExtent;
          storageItems = [];
    }

    class StorageItem{
          description;
          sizeOfHeight;
          sizeOfWidth;
          sizeOfLong;
          extent;
          countOfDesk;
    }

    $("#cutTreeStorageButton").click(function () {
        let codeOfProduct =         $("#cutTreeStorageCode").val();
        let breedID =               $("#breedIdPack").val();
        let userID =                $("#userIdPack").val();
        let extent =                $("#cutTreeStorageUsedExtent").val();
        let recycleExtent =         $("#cutTreeStorageRecycleExtent").val();


        let dto = new TreeStorageListDto();
        dto.codeOfProduct = codeOfProduct;
        dto.breedId = breedID;
        dto.userId = parseInt(userID);
        dto.extent = extent;
        dto.recycleExtent = recycleExtent;


        if (codeOfProduct !== "" && extent !== ""  && recycleExtent !== "" && parseFloat(extent) > 0 ) {

            if (!tableForcuttingTreeStorage.data().any()){
                alert("Отсутствуют записи в таблице!");
            } else if (parseFloat(extent)<parseFloat($("#cutTreeStorageTotalExtent").val())){
                alert("Введенные данные превышают кубатуру взятого материала на "+(parseFloat($("#cutTreeStorageTotalExtent").val())-parseFloat(extent)).toFixed(3)+"м3!");
            }else {
                let newData = ( tableForcuttingTreeStorage.rows().data() );
                for (let i =newData.length-1; i>=0; i--) {
                    let item=new StorageItem();
                    item.description = newData[i][0];
                    item.sizeOfHeight = newData[i][1];
                    item.sizeOfWidth = newData[i][2];
                    item.sizeOfLong = newData[i][3];
                    item.countOfDesk = newData[i][4];
                    item.extent = newData[i][5];

                    dto.storageItems.push(item);
                }

                console.log(dto);

                $.ajax({
                    method: "post",
                    url: "/cutOfTreeStorageDTO",
                    contextType: "application/json",
                    data: {
                        dto: JSON.stringify(dto)
                    },
                    traditional: true,
                    success: function () {
                        location.reload();
                    },
                    error: function () {
                        alert("Ошибка!");
                    }
                });
            }
        } else {
            alert("Заполните все поля!");
        }
    });
    /////////////////////////////////////////////////////////////////////

    function calculateCutTreStorageSummary(){
        let newData = ( tableForcuttingTreeStorage.rows( ).data() );
        let extent = 0.00;
        for (let i =newData.length-1; i>=0; i--) {

            let sumHeight = parseInt(newData[i][1]);
            let sumWidth  = parseInt(newData[i][2]);
            let sumLength = parseInt(newData[i][3]);
            let sumCount  = parseInt(newData[i][4]);

            let rowExtent = parseFloat(sumHeight) / 1000 * parseFloat(sumWidth) / 1000 * parseFloat(sumLength) / 1000 * sumCount;

            extent += rowExtent;
        }
        $("#cutTreeStorageTotalExtent").val(extent.toFixed(3));
    }


    $('#treestoragetable-single').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },

        "lengthMenu": false,
        "bSort": false,
        "info": false,
        "paging": false,
        "searching": false,
        "lengthChange": false,
        // "order": [ 0, "desc" ],

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
                "width": "40px"
            },
            { className: "display-none", "targets": [ 7 ] },
        ]

    });

    $('#recyclestoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },

        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": false,
        "info": false,
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
    // $('#treestoragetable tbody').on( 'click', 'tr', function () {
    //     $(this).toggleClass('selected');
    // } );
    //TOOGLE SELECTED END



    let rawstoragetable =  $('#rawstoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "select": {
            "style": 'os'
        },
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
            { className: "display-none", "targets": [ -6, -4, -3, -2 ] },
        ]
    });

    let tableForRawStorageGrouping = $('#tableForRawStorageGrouping').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "bSort": false,
        "info": false,
        "autoWidth": false,
        "searching": false,
        "paging": false,
        "columnDefs": [
            { className: "display-none", "targets": [ 4 ] },
        ]
    });

    // TOOGLE SELECTED START
    $('#rawstoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    // TOOGLE SELECTED END

    // GROUP SELECTED START
    $('#groupRawStorageButton').on( 'click', function () {
        tableForRawStorageGrouping.clear().draw();

        let show = true;
        let first = 0;
        let count = 0;
        let extent = 0.0;
        let exampleCode = '';
        let exampleMaterial = '';
        let exampleDescription = '';
        let exampleThickness = 0;
        let exampleWidth = 0;
        let exampleLength = 0;

        rawstoragetable.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            if (rowLoop === 0){
                first = rowIdx;
            }

            let data = this.data();
            console.log("data 14: "+ data[14]);
            let firstData = rawstoragetable.row(first).data();
            let idInput = "<input type='number' value=\""+rawstoragetable.row(rowIdx).id()+"\" readonly  name=\"idOfRow\">\n";

            exampleCode = firstData[2];
            exampleMaterial = firstData[3];
            exampleDescription = firstData[4];
            exampleThickness = firstData[5];
            exampleWidth = firstData[6];
            exampleLength = firstData[7];

            if (data[5]===firstData[5]&&data[6]===firstData[6]&&data[7]===firstData[7]  && data[14] === "false" && firstData[14] === "false"){
                console.log('ok');
                count+=parseInt(data[8]);
                extent += (parseFloat(data[9]));
                tableForRawStorageGrouping.row.add([
                    data[2],
                    data[4],
                    data[8],
                    data[9],
                    idInput,
                    "<button type=\"button\" class=\"btn btn-primary btn-sm\"><i class=\"fa fa-times\" title=\"Удалить\"></i></button>"
                ]).draw(false);
            }else {
                console.log('error');
                show = false;
            }
        });

        $('#groupDryingModalCode')          .val(exampleCode);
        $('#groupDryingModalMaterial')      .val(exampleMaterial);
        $('#groupDryingModalMaterialDescr') .val(exampleDescription);
        $('#groupDryingModalThickness')     .val(exampleThickness);
        $('#groupDryingModalWidth')         .val(exampleWidth);
        $('#groupDryingModalLength')        .val(exampleLength);
        $('#groupDryingModalCount')         .val(count);
        $('#groupDryingModalVolume')        .val(extent.toFixed(3));

        if (show===true){
            $('#groupingModal').modal('show');
        }else {
            alert("Выбрано недопустимые значения!");
        }
    });
    // GROUP SELECTED END

    //DELETE ROW
    $('#tableForRawStorageGrouping tbody').on( 'click', 'button', function () {
        let count = 0;
        let extent = 0.0;

        tableForRawStorageGrouping
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        tableForRawStorageGrouping.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            count+=parseInt(data[2]);
            extent += (parseFloat(data[3]));
        });

        $('#groupDryingModalCount')         .val(count);
        $('#groupDryingModalVolume')        .val(extent.toFixed(3));
    });


    let rawstoragetableoak =  $('#rawstoragetableoak').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": false,
        "info": false,
        "select": {
            "style": 'os'
        },
        // "order": [ 0, "desc" ],
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
            { className: "display-none", "targets": [ -5, -3, -2] },
        ]
    });

    let tableForOakRawStorageGrouping = $('#tableForOakRawStorageGrouping').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "bSort": false,
        "info": false,
        "autoWidth": false,
        "searching": false,
        "paging": false,
        "columnDefs": [
            { className: "display-none", "targets": [ 3 ] },
        ]
    });

    //DELETE ROW
    $('#tableForOakRawStorageGrouping tbody').on( 'click', 'button', function () {
        let extent = 0.0;

        tableForOakRawStorageGrouping
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        tableForOakRawStorageGrouping.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            extent += (parseFloat(data[2]));
        });

        $('#groupDryingModalVolumeOak')        .val(extent.toFixed(3));
    });

    // GROUP SELECTED START
    $('#groupRawStorageOakButton').on( 'click', function () {
        tableForOakRawStorageGrouping.clear().draw();

        let show = true;
        let first = 0;
        let extent = 0.0;
        let exampleCode = '';
        let exampleMaterial = '';
        let exampleDescription = '';
        let exampleThickness = 0;
        let exampleLength = 0;

        rawstoragetableoak.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            if (rowLoop === 0){
                first = rowIdx;
            }

            let data = this.data();
            let firstData = rawstoragetableoak.row(first).data();
            let idInput = "<input type='number' value=\""+rawstoragetableoak.row(rowIdx).id()+"\" readonly  name=\"idOfRow\">\n";

            exampleCode = firstData[3];
            exampleMaterial = firstData[4];
            exampleDescription = firstData[5];
            exampleThickness = firstData[6];
            exampleLength = firstData[7];

            console.log("data 0: "+data[2]);
            if (data[6]===firstData[6]&&data[7]===firstData[7]&&data[2].toString()==firstData[2].toString()){

                console.log('ok');
                extent += (parseFloat(data[8]));
                tableForOakRawStorageGrouping.row.add([
                    data[3],
                    data[5],
                    data[8],
                    idInput,
                    "<button type=\"button\" class=\"btn btn-primary btn-sm\"><i class=\"fa fa-times\" title=\"Удалить\"></i></button>"
                ]).draw(false);
            }else {
                console.log('error');
                show = false;
            }
        });

        $('#groupDryingModalCodeOak')          .val(exampleCode);
        $('#groupDryingModalMaterialOak')      .val(exampleMaterial);
        $('#groupDryingModalMaterialDescrOak') .val(exampleDescription);
        $('#groupDryingModalThicknessOak')     .val(exampleThickness);
        $('#groupDryingModalLengthOak')        .val(exampleLength);
        $('#groupDryingModalVolumeOak')        .val(extent.toFixed(3));

        if (show===true){
            $('#groupingOakModal').modal('show');
        }else {
            alert("Выбрано недопустимые значения!");
        }
    });
    // GROUP SELECTED END


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

                    $("#addToRawStorageCode").focus();

                    let id =            obj["rawStorageId"];
                    let treeStorageExt= obj["treeStorageExtent"];
                    let rawStorageExt = obj["rawStorageExtent"];

                    let row = [id, '', code, tree, breedDescr, thickness, width, length, count, rawStorageExt, treeStorageExt, supplier, '0.0', 'null', 'false', button];

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

    let dryingtableCell = $('#dryingtableCell').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": false,
        "info": false,
        "autoWidth": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
        ]
    });

    $('.dryingtable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        // "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "info": false,
        "searching": false,
        "paging": false,
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

    // // TOOGLE SELECTED START
    // $('#dryingtable tbody').on( 'click', 'tr', function () {
    //     $(this).toggleClass('selected');
    // } );
    // //TOOGLE SELECTED END

    let dryingtableoakCell = $('#dryingtableoakCell').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [-1], ["Все"] ],
        "bSort": false,
        "info": false,
        "autoWidth": false,
        // id column visibility
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            },
        ]
    });

    $('.dryingtableoak').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": false,
        "info": false,
        "searching": false,
        "paging": false,
        // "order": [ 0, "desc" ],
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
            { className: "display-none", "targets": [ -2 ] },
        ]
    });

    // $('.dryingtableoakInner').DataTable({
    //     "language": {
    //         "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
    //     },
    //     "bSort": false,
    //     "info": false,
    //     // "order": [ 0, "desc" ],
    //     // columns width
    //     "autoWidth": false,
    //
    //     // id column visibility
    //     "columnDefs": [
    //         {
    //             "targets": [ 0 ],
    //             "visible": false,
    //             "searchable": false
    //         },
    //         {
    //             "targets": -1,
    //             "orderable": false,
    //             "width": "100px"
    //         },
    //     ]
    // });


    let drystoragetable = $('#drystoragetable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "select": {
            "style": 'os'
        },
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
                "width": "110px"
            },
            { className: "display-none", "targets": [ -2 ] }
        ]
    });

    let tableForDryStorageGrouping = $('#tableForDryStorageGrouping').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "bSort": false,
        "info": false,
        "autoWidth": false,
        "searching": false,
        "paging": false,
        "columnDefs": [
            { className: "display-none", "targets": [ 4 ] },
        ]
    });

    //DELETE ROW
    $('#tableForDryStorageGrouping tbody').on( 'click', 'button', function () {
        let count = 0;
        let extent = 0.0;

        tableForDryStorageGrouping
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        tableForDryStorageGrouping.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            count+=parseInt(data[2]);
            extent += (parseFloat(data[3]));
        });

        $('#groupDryingModalCount')         .val(count);
        $('#groupDryingModalVolume')        .val(extent.toFixed(3));
    });


    // TOOGLE SELECTED START
    $('#drystoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END

    // GROUP SELECTED START
    $('#groupDryStorageButton').on( 'click', function () {
        tableForDryStorageGrouping.clear().draw();

        let show = true;
        let first = 0;
        let count = 0;
        let extent = 0.0;
        let exampleCode = '';
        let exampleMaterial = '';
        let exampleDescription = '';
        let exampleThickness = 0;
        let exampleWidth = 0;
        let exampleLength = 0;

        drystoragetable.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            if (rowLoop === 0){
                first = rowIdx;
            }

            let data = this.data();
            let firstData = drystoragetable.row(first).data();
            let idInput = "<input type='number' value=\""+drystoragetable.row(rowIdx).id()+"\" readonly  name=\"idOfRow\">\n";

            exampleCode = firstData[1];
            exampleMaterial = firstData[2];
            exampleDescription = firstData[3];
            exampleThickness = firstData[4];
            exampleWidth = firstData[5];
            exampleLength = firstData[6];

            if (data[4]===firstData[4]&&data[5]===firstData[5]&&data[6]===firstData[6]){
                console.log('ok');
                count+=parseInt(data[7]);
                extent += (parseFloat(data[8]));
                tableForDryStorageGrouping.row.add([
                    data[1],
                    data[3],
                    data[7],
                    data[8],
                    idInput,
                    "<button type=\"button\" class=\"btn btn-primary btn-sm\"><i class=\"fa fa-times\" title=\"Удалить\"></i></button>"
                ]).draw(false);
            }else {
                console.log('error');
                show = false;
            }
        });

        $('#groupDryingModalCode')          .val(exampleCode);
        $('#groupDryingModalMaterial')      .val(exampleMaterial);
        $('#groupDryingModalMaterialDescr') .val(exampleDescription);
        $('#groupDryingModalThickness')     .val(exampleThickness);
        $('#groupDryingModalWidth')         .val(exampleWidth);
        $('#groupDryingModalLength')        .val(exampleLength);
        $('#groupDryingModalCount')         .val(count);
        $('#groupDryingModalVolume')        .val(extent.toFixed(3));

        if (show===true){
            $('#groupingModal').modal('show');
        }else {
            alert("Выбрано недопустимые значения!");
        }
    });
    // GROUP SELECTED END



    let drystoragetableOak = $('#drystoragetableOak').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "bSort": false,
        "info": false,
        "select": {
            "style": 'os'
        },
        // "order": [ 0, "desc" ],
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
                "width": "170px"
            },
            { className: "display-none", "targets": [ -2 ] }
        ]
    });

    let tableForDryStorageOakGrouping = $('#tableForDryStorageOakGrouping').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "bSort": false,
        "info": false,
        "autoWidth": false,
        "searching": false,
        "paging": false,
        "columnDefs": [
            { className: "display-none", "targets": [ 3 ] },
        ]
    });

    //DELETE ROW
    $('#tableForDryStorageOakGrouping tbody').on( 'click', 'button', function () {
        let extent = 0.0;

        tableForDryStorageOakGrouping
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        tableForDryStorageOakGrouping.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            extent += (parseFloat(data[2]));
        });

        $('#groupDryingModalVolumeOak')        .val(extent.toFixed(3));
    });



    // GROUP SELECTED START
    $('#groupDryStorageOakButton').on( 'click', function () {
        tableForDryStorageOakGrouping.clear().draw();

        let show = true;
        let first = 0;
        let extent = 0.0;
        let exampleCode = '';
        let exampleMaterial = '';
        let exampleDescription = '';
        let exampleThickness = 0;
        let exampleLength = 0;

        drystoragetableOak.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            if (rowLoop === 0){
                first = rowIdx;
            }

            let data = this.data();
            let firstData = drystoragetableOak.row(first).data();
            let idInput = "<input type='number' value=\""+drystoragetableOak.row(rowIdx).id()+"\" readonly  name=\"idOfRow\">\n";

            exampleCode = firstData[2];
            exampleMaterial = firstData[3];
            exampleDescription = firstData[4];
            exampleThickness = firstData[5];
            exampleLength = firstData[6];

            console.log("data 0: "+data[1]);
            if (data[5]===firstData[5]&&data[6]===firstData[6]&&data[1].toString()==firstData[1].toString()){

                console.log('ok');
                extent += (parseFloat(data[7]));
                tableForDryStorageOakGrouping.row.add([
                    data[2],
                    data[4],
                    data[7],
                    idInput,
                    "<button type=\"button\" class=\"btn btn-primary btn-sm\"><i class=\"fa fa-times\" title=\"Удалить\"></i></button>"
                ]).draw(false);
            }else {
                console.log('error');
                show = false;
            }
        });

        $('#groupDryingModalCodeOak')          .val(exampleCode);
        $('#groupDryingModalMaterialOak')      .val(exampleMaterial);
        $('#groupDryingModalMaterialDescrOak') .val(exampleDescription);
        $('#groupDryingModalThicknessOak')     .val(exampleThickness);
        $('#groupDryingModalLengthOak')        .val(exampleLength);
        $('#groupDryingModalVolumeOak')        .val(extent.toFixed(3));

        if (show===true){
            $('#groupingOakModal').modal('show');
        }else {
            alert("Выбрано недопустимые значения!");
        }
    });
    // GROUP SELECTED END


    // TOOGLE SELECTED START
    // $('#drystoragetableOak tbody').on( 'click', 'tr', function () {
    //     $(this).toggleClass('selected');
    // } );
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
        $("#sendForPackageModalTotalDescdCountOak").val('');
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
        let totalDescs=     $('#sendForPackageModalTotalDescdCountOak');
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
        let totalDescs=     $('#sendForPackageModalTotalDescdCountOak');
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
                        $('#sendForPackageModalTotalDescdCountOak').val('0');
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
        let usedExtent1 = $('#addDeliveryPackageModalUsedExtentOak').val();

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
                    sizeOfLong: length1,
                    extent: extent1,
                    usedExtent: usedExtent1,
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

    ///////////////////////////////////////////////////////////////////
    // requestCreatePackageOak
    $("#createInitialPackageFromDryOak").click(function () {
        // function sendRequestCreatePackageOak(btnObj) {
        let breedID =               $("#breedIdPack").val();
        let userID =                $("#userIdPack").val();
        let codeOfInitialPackage1 = $('#addOakInitialPackageCode').val();
        console.log(codeOfInitialPackage1);
        let breedDescription1 =     $('#addOakInitialPackageDescr').val();
        let sizeOfHeight1 =         $('#addDeliveryPackageModalSizeOak').val();
        let length1 =               $('#addDeliveryPackageModalLengthOak').val();

        let tableBody = document.getElementById('listOfOakPackageRowsId');
        let listTr = tableBody.getElementsByTagName('tr');

        let arrOfDesk = [];

        var extent1 = $('#addDeliveryPackageModalExtentOak').val();
        let usedExtent1 = $('#addDeliveryPackageModalUsedExtentOak').val();

        //fix one dimension array on controller
        arrOfDesk[0] = [];
        arrOfDesk[0][0] = "test";
        arrOfDesk[0][1] = "test";


        if (codeOfInitialPackage1 !== "" && sizeOfHeight1 !== "" && length1 !== "") {
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
                url: "/createInitialPackageOakDryObject-" + userID + "-" + breedID,
                contextType: "application/json",
                data: {
                    codeOfProduct: codeOfInitialPackage1,
                    breedDescription: breedDescription1,
                    sizeOfHeight: sizeOfHeight1,
                    sizeOfLong: length1,
                    extent: extent1,
                    usedExtent: usedExtent1,
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

    // $('#addInitialRawModal').on('hide.bs.modal', function (e) {
    //     let sum = $('#sendForPackageModalTotalDescsCountOak').val();
    //     $('#addInitialRawModalSum').val(sum);
    // })
    //
    $('#sendFromRawToPackageOakModal').on('hide.bs.modal', function (e) {
        let sum = $('#sendForPackageModalTotalDescdCountOak').val();
        $('#sendFromRawToPackageOakModalSum').val(sum);
    })

    $('#addInitialRawModal').on('show.bs.modal', function (e) {
        let extent =        $("#addDeliveryPackageModalExtentOak");
        let length =        $("#addDeliveryPackageModalLengthOak");
        let height =        $("#addDeliveryPackageModalSizeOak");
        createPackageExtentCalc(tableForPackagesOak, extent, length, height);
        // let sum = $('#addInitialRawModalSum').val();
        // $('#sendForPackageModalTotalDescsCountOak').val(sum);
    })

    $('#sendFromRawToPackageOakModal').on('show.bs.modal', function (e) {
        // let extent = $("#sendForPackageModalExtentOak");
        // let length = $("#sendForPackageModalLengthOak");
        // let height = $("#sendForPackageModalSizeOak");
        // createPackageExtentCalc(tableForPackagesOak, extent, length, height);
        let sum = $('#sendFromRawToPackageOakModalSum').val();
        $('#sendForPackageModalTotalDescdCountOak').val(sum);
    })

    $("#buttonForAddingDeliveryOakRow").click(function () {
        let width =         $("#addDeliveryPackageModalWidthOak").val();
        let count =         $("#addDeliveryPackageModalCountOak").val();
        let extent =        $("#addDeliveryPackageModalExtentOak");
        let length =        $("#addDeliveryPackageModalLengthOak");
        let height =        $("#addDeliveryPackageModalSizeOak");
        createPackageExtentCalc(tableForPackagesOak, extent, length, height);
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
        createPackageExtentCalc(tableForPackagesOak, extent, length, height);
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
        "select": {
            "style": 'os'
        },
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

    $('#unformMultipleItemsBtn').on( 'click', function () {
        table.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            let input = "<input type=\"number\" class=\"form-control\" hidden=\"hidden\" autocomplete=\"off\" name=\"id\" value='"+data[0]+"'>";

            $('#listOfInputs').append(input);
        });

            $('#unformMultipleItemsModal').modal('show');
    });

    $('#unformMultipleOakItemsBtn').on( 'click', function () {
        oakTable.rows('.selected').every( function ( rowIdx, tableLoop, rowLoop ) {
            let data = this.data();
            let input = "<input type=\"number\" class=\"form-control\" hidden=\"hidden\" autocomplete=\"off\" name=\"id\" value='"+data[1]+"'>";

            $('#listOfOakInputs').append(input);
        });

        $('#unformMultipleOakItemsModal').modal('show');
    });

    $( "#buttonForTransportation" ).click(function() {
        modalPackagesTable.clear().draw();
        let newData = ( table.rows( '.selected' ).data() );
        let array = [];

        let extentS = 0.0;
        let packsCount = newData.length;

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

        $("#deliveryPackagesCount").val(packsCount);
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
        let packsCount = newData.length;

        for (let i = newData.length - 1; i >= 0; i--) {
            var extent = 		newData[i][8];
            extentS = extentS+parseFloat(extent);
            console.log(extentS);
        }

        console.log(extentS);

        $("#deliveryPackagesCount").val(packsCount);
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
        "select": {
            "style": 'os'
        },
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

        let packsCount = parseInt($("#deliveryOakPackagesCount").val())-1;

        $("#listOfPackagesOakId").find("tr").each(function() {
            extentR = parseFloat($(this).find('td.extentValue').text());
            console.log(extentR);
            if (!Number.isNaN(extentR)){
                extentS = extentS +extentR;
            }
        });

        console.log(extentS);

        $("#deliveryOakPackagesCount").val(packsCount);
        $("#deliveryOakExtent").val(extentS.toFixed(3));

    } );


    $( "#buttonForOakTransportation" ).click(function() {
        modalOakPackagesTable.clear().draw();

        let newData =           document.getElementsByClassName("outer-table odd selected");
        let tBody =             document.getElementById('listOfPackagesOakId');

        let packsCount = newData.length;

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
            trParentClone.classList.remove("selected");
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

        $("#deliveryOakPackagesCount").val(packsCount);
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

            { className: "display-none", "targets": [ 2, 13 ] }

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
            { className: "display-none", "targets": [ 2, 13 ] }
        ]

    });

    function showPage() {
        document.getElementById("L2").style.display = "none";
        document.getElementById("myTabContent").style.display = "block";
    }

    setTimeout(showPage, 500);
})