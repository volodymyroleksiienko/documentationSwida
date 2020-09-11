$(document).ready( function () {

    $('#admin-users-table').DataTable({
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
                "width": "30px"
            }
        ]
    });

    $('#admin-breed-of-tree-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        "columnDefs": [
            // {
            //     "targets": [ 0 ],
            //     "visible": false,
            //     "searchable": false,
            // },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "30px"
            }
        ]
    });

    // INNER MARKET TABLE START
    $('#inner-market-buyers-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [  ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            {
                // "width": "20%",
                // "targets": 6
            }
        ]

    });
    // INNER MARKET TABLE END

    // OUTER MARKET TABLE START
    $('#outer-market-buyers-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        "columnDefs": [
            {
                "targets": [  ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            {
                // "width": "20%",
                // "targets": 6
            }
        ]

    });
    // OUTER MARKET TABLE END

    //ORDER INFO START

    // Contracts table start
    $('#contracts-tab-table').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "lengthMenu": [ [25, 50, -1], [25, 50, "Все"] ],
        "order": [ 0, "desc" ],
        "autoWidth": false,
        "columnDefs": [
            {
                // "targets": [ 0, -1 ],
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -2,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            }
        ]
    });
    // Contracts table end

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
                // "targets": [ 0, -2, -1 ],
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -2,
                "orderable": false,
                "searchable": false,
                "width": "30px"
            }
        ]
    });
    // Distribution table end

    // Modal inner table start
    var tableOfDistribution = $('#tableOfDistribution').DataTable({
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
            },
            { className: "display-none", "targets": [ 3 ] }
        ]

    });
    // Modal inner table end

    // ADD ELEMENTS TO INNER TABLE LIST START
    $("#buttonForAddingDistributionRow").click(function () {
        let code =               $("#addDistributionCode").val();
        let company =            $("#addDistributionCompanyInner").val();
        let amount =             parseFloat($("#addDistributionAmountInner").val());
        let button =             "<button type='button' class='btn btn-primary btn-sm'><i class='fa fa-times' title='Удалить''></i></button>";
        let companyId =          $("#addDistributionCompanyInner-hidden").val();
        const mainOrderCode =    $('#sendDistributionModalCode').val();
        let maxExtent =          parseFloat($('#maxExtentValue').val());

        let arrOfTr = document.getElementById("listOfDistributionsId").getElementsByTagName("tr");
        console.log("arr length = "+arrOfTr.length);

        let arrayOfCodes     = [];

        let res = false;

        for (let i = 0; i<arrOfTr.length;i++){
            arrayOfCodes[i] =   $(arrOfTr[i]).find('td:eq(0)').text();
            if (code == arrayOfCodes[i]){
                res = true;
            }
        }

        if( code == "" || company == "" || Number.isNaN(amount)) {
            alert("Заполните все поля!");
        } else if (amount<=0.000){
            alert("Значение не может быть отрицательным либо равным 0!")
        } else if(code == mainOrderCode){
            alert("Значение не может быть идентичным главному заказу!");
        } else if(res == true){
            alert("Дубликация кода!");
            $("#addDistributionCode").val(mainOrderCode);
        }else {
            let row = [code, company, amount.toFixed(3), companyId, button];

            console.log(row);

            $("#addDistributionCode")           .val(mainOrderCode);
            $("#addDistributionCompanyInner")   .val("");
            $("#addDistributionAmountInner")    .val((maxExtent-amount).toFixed(3));
            $("#addDistributionAmountInner")    .attr({"max" : (maxExtent-amount).toFixed(3), "min": 0.000});
            $('#maxExtentValue').val(maxExtent-amount);

            tableOfDistribution.row.add(row).draw();
        }
    });
    // ADD ELEMENTS TO INNER TABLE LIST END

    // DELETE BUTTON START
    $('#tableOfDistribution tbody').on( 'click', 'button', function () {
        let maxExtent =          parseFloat($('#maxExtentValue').val());

        let  extent =            parseFloat($(this).parents('tr').find('td:eq(2)').text());
        console.log("ext: "+ extent);
        let rowExtent =          parseFloat($('#addDistributionAmountInner').val());

        $("#addDistributionAmountInner")    .val((rowExtent+extent).toFixed(3));
        $('#maxExtentValue')                .val((maxExtent+extent).toFixed(3));

        tableOfDistribution.row( $(this).parents('tr') ).remove().draw();
    } );
    // DELETE BUTTON END

    // ORDER INFO END

});