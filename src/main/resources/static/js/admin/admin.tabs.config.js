$(document).ready( function () {

    $('.userStatistic').DataTable({
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
        },
        "bSort": true,
        "info": false,
        "searching": false,
        "paging": false,
        // "order": [ 0, "desc" ],
        "autoWidth": false,
    });

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
            },
            { className: "display-none", "targets": [ -2 ] }
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
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ 0 ] }
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
                "targets": -1,
                "orderable": false,
                "searchable": false,
                "width": "70px"
            },
            { className: "display-none", "targets": [ 0 ] }
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
                "targets": [ 0 ],
                "visible": false,
                "searchable": false,
            },
            {
                "targets": -3,
                "orderable": false,
                "searchable": false,
                "width": "100px"
            },
            { className: "display-none", "targets": [ -1, -2 ] }
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
                "targets": -4,
                "orderable": false,
                "searchable": false,
                "width": "30px"
            },
            { className: "display-none", "targets": [ -1, -2, -3 ] }
        ]
    });
    // Distribution table end

 tableOfInformation = $('#tableOfInformation').DataTable({
        "autoWidth": false,
        "bSort": false,
        "searching": false,
        "paging": false,
        "info": false,
        'select': false,
    });


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

    $('#createDistributionModal').on('show.bs.modal', function(){
        tableOfDistribution.clear().draw();
    });

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
            if (code === arrayOfCodes[i]){
                res = true;
            }
        }

        if( Number.isNaN(code) || Number.isNaN(company) || Number.isNaN(amount)) {
            alert("Заполните все поля!");
        } else if(code === mainOrderCode){
            alert("Значение не может быть идентичным главному заказу!");
        } else if(res === true) {
            alert("Дубликация кода!");
            $("#addDistributionCode").val(mainOrderCode);
        } else if (amount<=0.000){
            alert("Значение не может быть отрицательным либо равным 0!")
            $("#addDistributionAmountInner").val(maxExtent.toFixed(3));
        }else if(amount>maxExtent){
            alert("Значение кубатуры больше максимально возможной!");
            console.log("max: "+maxExtent+"; my: "+amount);
            $("#addDistributionAmountInner").val(maxExtent.toFixed(3));
        }else {
            let row = [code, company, amount.toFixed(3), companyId, button];

            console.log(row);

            $("#addDistributionCode")           .val(mainOrderCode);
            $("#addDistributionCompanyInner")   .val("");
            $("#addDistributionAmountInner")    .val((maxExtent-amount).toFixed(3))
                                                .attr({"max" : (maxExtent-amount).toFixed(3), "min": 0.000});
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

        $("#addDistributionAmountInner")    .val((rowExtent+extent).toFixed(3))
                                            .attr({"max" : (rowExtent+extent).toFixed(3), "min": 0.000});
        $('#maxExtentValue')                .val((maxExtent+extent).toFixed(3));

        tableOfDistribution.row( $(this).parents('tr') ).remove().draw();
    } );
    // DELETE BUTTON END



    function showPage() {
        document.getElementById("L2").style.display = "none";
        document.getElementById("myTabContent").style.display = "block";
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

    }

    setTimeout(showPage, 500);
});


let summary = 0.000;

function drawChart() {

    let data = new google.visualization.DataTable();
    data.addColumn('string', 'Place');
    data.addColumn('number', 'Extent');
    data.addRow(['Выбрано', 100]);

    let options = {
        title: 'Суммарная кубатура: '+summary.toFixed(3)+" м3",
        pieHole: 0.4,
        sliceVisibilityThreshold: .00001,

    };

    let data2 = new google.visualization.DataTable();
    data2.addColumn('string', 'Place');
    data2.addColumn('number', 'Extent');
    data2.addRow(['Выбрано', 100]);

    let options2 = {
        title: 'Суммарная кубатура: '+summary.toFixed(3)+" м3",
        pieHole: 0.4,
        sliceVisibilityThreshold: .00001,
    };

    let chart = new google.visualization.PieChart(document.getElementById('piechart'));
    let chart2 = new google.visualization.PieChart(document.getElementById('piechart2'));

    chart.draw(data, options);
    chart2.draw(data2, options2);
}