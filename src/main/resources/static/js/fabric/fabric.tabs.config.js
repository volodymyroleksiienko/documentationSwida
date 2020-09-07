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
                "targets": [ 0, -1],
                "visible": false,
                "searchable": false,
            }
        ]
    });
    // Distribution table end
    console.log("load on ready");
    // Modal inner table start
    var tableOfDistribution = $('#tableOfDistribution').DataTable({
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
        var company = $("#addDistributionCompanyInner").val();
        var amount = parseFloat($("#addDistributionAmountInner").val());
        var button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

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



    $('#rawstoragetable').DataTable({
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
            }
        ]
    });

    // TOOGLE SELECTED START
    $('#rawstoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END




    $('#dryingtable').DataTable({
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
            }
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
            }
        ]
    });

    // TOOGLE SELECTED START
    $('#drystoragetable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //TOOGLE SELECTED END


    $('#drystoragetableOak').DataTable({
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
            }
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

    $("#buttonForAddingOakRow").click(function () {
        var width = $("#sendForPackageModalWidthOak").val();
        var count = $("#sendForPackageModalCountOak").val();
        var button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

        if( width=="" || count=="" ) {
            alert("Заполните ширину и количество досок!");
        } else if (width<=0 || count<=0){
            alert("Значение не может быть отрицательным либо равным 0!")
        } else {
            var d = [width, count, button];

            console.log(d);

            $("#sendForPackageModalWidthOak").val("");
            $("#sendForPackageModalCountOak").val("");

            tableForTransportationOak.row.add(d).draw();


            $('#sendForPackageModalWidthOak').focus();
            $('#sendForPackageModalWidthOak').val(parseInt(width)+10);
        }
    });

    $("#buttonForAddingDeliveryOakRow").click(function () {
        var width = $("#addDeliveryPackageModalWidthOak").val();
        var count = $("#addDeliveryPackageModalCountOak").val();
        var button = "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить'></i></button>";

        if( width=="" || count=="" ) {
            alert("Заполните ширину и количество досок!");
        } else if (width<=0 || count<=0){
            alert("Значение не может быть отрицательным либо равным 0!")
        } else {
            var d = [width, count, button];

            console.log(d);

            $("#addDeliveryPackageModalWidthOak").val("");
            $("#addDeliveryPackageModalCountOak").val("");

            tableForTransportationOak.row.add(d).draw();
        }
    });

    $('#tableForTransportationOak tbody').on( 'click', 'button', function () {
        tableForTransportationOak.row( $(this).parents('tr') ).remove().draw();
    } );
    //    DRYING OAK END



    //PACKAGES 1 START
    let table = $('#packagedproducttable').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // columns width
        "autoWidth": false,
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
            }
        ]

    });


    //            SELECT
    $('#packagedproducttable tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
    //            SELECT


    $( "#buttonForTransportation" ).click(function() {

        modalPackagesTable.clear().draw();
        var newData = ( table.rows( '.selected' ).data() );
        var array = [];

        for (var i = newData.length - 1; i >= 0; i--) {

            var id =			newData[i][0];
            var code = 			newData[i][1];
            var material = 		newData[i][2];
            var description = 	newData[i][3];
            var thickness = 	newData[i][4];
            var width = 		newData[i][5];
            var length = 		newData[i][6];
            var count = 		newData[i][7];
            var extent = 		newData[i][8];
            var heightPc = 		newData[i][9];
            var widthPc = 		newData[i][10];
            var wh = heightPc.toString()+"/"+widthPc.toString();
            var button =        "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";

            var d=[id, code, material, description, thickness, width, length, count, extent, wh, button];

            array[i] = id;

            modalPackagesTable.row.add(d).draw( false );
        }
        console.log(array);
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
        ]

    });


    $('#tableForTransportation tbody').on( 'click', 'button', function () {
        modalPackagesTable.row( $(this).parents('tr') ).remove().draw();
    } );
    //PACKAGES 1 END



    //PACKS2 start
    var oakTable = $('#packageOakTableId').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        // columns width
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



    $( "#buttonForOakTransportation" ).click(function() {
        modalOakPackagesTable.clear().draw();

        let newData =           document.getElementsByClassName("selected");
        let tBody =             document.getElementById('listOfPackagesOakId');
        tBody.deleteRow(0);

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

            let delInnerBtn1 =         trParentClone.getElementsByClassName("delete-btns")[0];
            delInnerBtn1.setAttribute("style", "display:none;");
            let delInnerBtn2 =         trParentClone.getElementsByClassName("delete-btns")[1];
            delInnerBtn2.setAttribute("style", "display:none;");

            $('#listOfPackagesOakId').append(trObj);
            $('#listOfPackagesOakId').append(trParentClone);
        }


        $('#sendOakListForTransportationModal').modal('show');
    });


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
        document.getElementById(idOfParent).remove()
    } );

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
                "width": "100px"
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            }

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
                "width": "100px"
            },
            {
                "targets": 0,
                "orderable": false,
                "width": "30px"
            }
        ]

    });
})