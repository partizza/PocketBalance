var generalTable;
var detailTable;

$(document).ready(function () {

    initTransactionsDataTable();
    initDetailsDataTable();

});

function initTransactionsDataTable() {
    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {

            generalTable = $('#transactions-table').dataTable({
                "scrollY": "140px",
                "scrollCollapse": true,
                "paging": false,
                "ajax": {
                    "url": "/data/transaction/book/" + data.bookId + "/all",
                    "dataSrc": ""
                },
                "columns": [
                    {"data": "name"},
                    {"data": "desc"}
                ],
                "order": [
                    [0, "asc"]
                ],
                "initComplete": function (settings, json) {
                    $('#transactions-table tbody').on('click', 'tr', function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');
                            hideTransactionDetails();
                        }
                        else {
                            generalTable.$('tr.active').removeClass('active');
                            $(this).addClass('active');
                            showTransactionDetails()
                        }
                    });
                }
            });

        },
        error: function () {
            $('.table-message').append('' +
                '<div class="alert alert-danger alert-dismissable">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                '<strong>Error!</strong> Data is not available.' +
                '</div>' +
                '');
        }
    });
};

function initDetailsDataTable() {
    detailTable = $('#transaction-details-table').dataTable({
        "data" : [],
        "scrollY": "140px",
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        "columns": [
            {"data": "entrySide"},
            {"data": "enable"},
            {"data": "accountName"},
            {"data": "accountDesc"},
            {"data": "accountAccId"},
            {"data": "accountEnable"},
            {"data": "accountBsCategory"}
        ]
    });
}

function showTransactionDetails() {
    var transaction = generalTable.api().row('.active').data();

    detailTable.api().clear();
    $('#transaction-details').show();
    detailTable.api().rows.add(transaction.details);
    detailTable.api().draw();

}

function hideTransactionDetails() {
    $('#transaction-details').hide();
}