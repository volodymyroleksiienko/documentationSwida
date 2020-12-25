$(document).ready( function () {

    $('#multimodal-truck-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        // "bSort": false,
        "info": false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ -3, -4 ] }
        ]
    });


    let containersTab = $('#multimodal-container-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        // "bSort": false,
        "select": {
            "style": 'os'
        },
        "info": false,
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "80px"
            },
            { className: "display-none", "targets": [ 14 ] }
        ]
    });

    containersTab.on( 'select', function ( e, dt, type, indexes ) {
        if ( type === 'row' ) {
            let newData = containersTab.rows( '.selected' ).data();

            if (newData.length>0) {
                let summary = 0;
                for (let i = newData.length - 1; i >= 0; i--) {
                    summary = summary + parseFloat(newData[i][10]);
                }
                document.getElementsByClassName('summary')[0].innerHTML = (summary.toFixed(2) + " &#8372;")
                document.getElementsByClassName('summary')[1].innerHTML = (summary.toFixed(2) + " &#8372;")
                console.log(summary);
            }else {
                document.getElementsByClassName('summary')[0].innerHTML = "Эквивалент &#8372;"
                document.getElementsByClassName('summary')[1].innerHTML = "Эквивалент &#8372;"
            }
        }
    } );

    containersTab.on( 'deselect', function ( e, dt, type, indexes ) {
        if ( type === 'row' ) {
            let newData = containersTab.rows( '.selected' ).data();
            if (newData.length>0) {
                let summary = 0;
                for (let i = newData.length - 1; i >= 0; i--) {
                    summary = summary + parseFloat(newData[i][10]);
                }
                document.getElementsByClassName('summary')[0].innerHTML = (summary.toFixed(2) + " &#8372;")
                document.getElementsByClassName('summary')[1].innerHTML = (summary.toFixed(2) + " &#8372;")
                console.log(summary);
            }else {
                document.getElementsByClassName('summary')[0].innerHTML = "Эквивалент &#8372;"
                document.getElementsByClassName('summary')[1].innerHTML = "Эквивалент &#8372;"
            }
        }
    } );

    // Selecting rows
    $('#multimodal-container-table tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );

    // Contracts table config
    let infoDetailedTab = $('#info-detailed-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 3, "asc" ],
        "select": {
            "style": 'os'
        },
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ 16 ] }
        ]
    });

    // Selecting rows
    $('#info-detailed-table tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );

     $('#setContainerButton').click(function () {
         let newData = ( infoDetailedTab.rows( '.selected' ).data() );
             let extentS = 0.0;

             for (let i = newData.length - 1; i >= 0; i--) {
                 var extent = 		newData[i][8];
                 extentS = extentS+parseFloat(extent);
                 console.log(extentS);
             }

             console.log(extentS);

             $("#selectContainerExtent").val(extentS.toFixed(3));


             $('#setContainer').modal('show');

     })

    $('#setContainerCurrencyButton').click(function () {
        $('#setContainerCurrency').modal('show');
        let summary = 0;
        let newData = containersTab.rows( '.selected' ).data();
        for (let i = newData.length - 1; i >= 0; i--) {
            summary = summary + parseFloat(newData[i][10]);
        }
        $('#selectedContainersEquivalent').val(summary.toFixed(2));
        $('#summaryIfNaN').val(summary.toFixed(2));
    })

    $('#selectContainerCurrency').on('input', function() {
        let newData = containersTab.rows( '.selected' ).data();
        let ifNaN =   parseFloat($('#summaryIfNaN').val());
        console.log("ifNan: "+ifNaN);
        let summary = 0;
        for (let i = newData.length - 1; i >= 0; i--) {
            ///////////////////////////////////////////////
            let price =         parseFloat(newData[i][5]);
            let extent =        parseFloat(newData[i][4]);
            let unloading =     parseFloat(newData[i][6]);
            let delivery =      parseFloat(newData[i][7]);
            let measurement =   parseFloat(newData[i][8]);
            let currency =      parseFloat($('#selectContainerCurrency').val());

            if(Number.isNaN(currency)) {
                $('#selectedContainersEquivalent').val(ifNaN);
            }else if (!Number.isNaN(extent) && !Number.isNaN(price) && !Number.isNaN(currency) &&!Number.isNaN(unloading) && !Number.isNaN(delivery) && !Number.isNaN(measurement)) {
                summary = summary + ((price * extent * currency) + (delivery * currency) + measurement);
                $('#selectedContainersEquivalent').val(summary.toFixed(2));
            }else {
                alert("Не заполнено поле в таблице!")
            }
            //////////////////////////////////////////////
        }
    })

    $('#setContainerRequest').on( 'click', function () {
        let newData = document.getElementsByClassName("selected");
        let container = document.getElementById("selectContainer-hidden").value;
        let containerName = document.getElementById("selectContainer").value;
        console.log("id of container: "+container);

        let arrayOfId = [];
        for(let i=0;i<newData.length;i++){
            arrayOfId[i] = newData[i].getAttribute("id");
            console.log("id: "+newData[i].getAttribute("id"));
        }

        $.ajax({
            method: "post",
            url: "/multimodal/setContainer",
            contextType: "application/json",
            data: {
                arrayOfPackagesId: arrayOfId,
                containerId: container,
                containerName:containerName
            },
            traditional: true,
            success: function () {
                location.reload();
            },
            error: function () {
                alert("Error");
            }
        })
    });

    $('#setContainerCurrencyRequest').on( 'click', function () {
        let containers = ( containersTab.rows( '.selected' ).data() );
        let arrayOfContainerId = [];
        let currencyValue = $('#selectContainerCurrency').val();

        for (let i = containers.length - 1; i >= 0; i--) {
            arrayOfContainerId[i] = containers[i][0];
        }

        console.log("ids of containers: "+arrayOfContainerId);
        console.log("currency: "+currencyValue);

        $.ajax({
            method: "post",
            url: "/multimodal/setContainersCurrency",
            contextType: "application/json",
            data: {
                arrayOfContainerIds: arrayOfContainerId,
                currency: currencyValue,
            },
            traditional: true,
            success: function () {
                location.reload();
            },
            error: function () {
                alert("Введите курс доллара либо нажмите \"Отмена\" !");
            }
        })
    });

    // Check if excel document
    $( "#importForm" ).submit(function( event ) {
        var validExts = new Array(".xlsx", ".xls");

        var fileInput = document.getElementById('inputFile');
        var fileExt = fileInput.files[0].name;

        fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
        if (validExts.indexOf(fileExt) < 0) {
            alert("Неправильный формат файла, доступные форматы: " +
                validExts.toString());
            return false;
        } else {
            return true
        };
        event.preventDefault();
    });



    // Contracts table config
    $('#contracts-table').DataTable({
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
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "140px"
            },
            { className: "display-none", "targets": [ -2 ] }
        ]
    });


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
                "searchable": false,
                "width": "130px"
            },
            { className: "display-none", "targets": [ 12 ] }
        ]

    });

    // Distribution table start
    $('#distribution-tab-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 9, "asc" ],
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            }
        ]
    });
    // Distribution table end


    //    Delivery Ukraine and Ports start
    var tableForTransportationOak = $('#tableForTransportationOak').DataTable({
        // columns width
        "autoWidth": false,
        "bSort": false,
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
    //    Delivery Ukraine and Ports end

    function showPage() {
        document.getElementById("L2").style.display = "none";
        document.getElementById("myTabContent").style.display = "block";
    }

    setTimeout(showPage, 500);

});