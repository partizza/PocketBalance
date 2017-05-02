var table;


$(document).ready(function () {

    initTransactionsDataTable();

});

function initTransactionsDataTable() {
    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {

            table = $('#transactions-table').dataTable({
                "scrollY":        "140px",
                "scrollCollapse": true,
                "paging":         false,
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
                        }
                        else {
                            table.$('tr.active').removeClass('active');
                            $(this).addClass('active');
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