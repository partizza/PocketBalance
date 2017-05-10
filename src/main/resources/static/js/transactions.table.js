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
                    {
                        "className": "clickable",
                        "data": "name"
                    },
                    {
                        "className": "clickable",
                        "data": "desc"
                    },
                    {
                        "data": "id",
                        "visible": false,
                        "searchable": false
                    },
                    {
                        "className": "text-center",
                        "width": "15%",
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary btn-xs edit'><i class='glyphicon glyphicon-edit'></i> Edit</button>"
                        + " "
                        + "<button class='btn btn-danger btn-xs remove'><i class='glyphicon glyphicon-remove'></i> Delete</button>"
                    }
                ],
                "order": [
                    [0, "asc"]
                ],
                "initComplete": function (settings, json) {
                    $('#transactions-table tbody').on('click', '.clickable', function () {
                        if ($(this).parent().hasClass('active')) {
                            $(this).parent().removeClass('active');
                            hideTransactionDetails();
                        }
                        else {
                            generalTable.$('tr.active').removeClass('active');
                            $(this).parent().addClass('active');
                            showTransactionDetails()
                        }
                    });

                    $('#transactions-table tbody').on('click', 'button.edit', function () {
                        var data = generalTable.api().row($(this).parents('tr')).data();
                        alert(data.name + " (id = " + data.id + ") " + " - will be processed (not implemented yet)");
                    });

                    $('#transactions-table tbody').on('click', 'button.remove', function () {
                        var data = generalTable.api().row($(this).parents('tr')).data();
                        removeTransaction(data.id);
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
        "data": [],
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

    $('html, body').animate({scrollTop: $(document).height()}, 'slow');
}

function hideTransactionDetails() {
    $('#transaction-details').hide();
}

function removeTransaction(tranId) {

    $.ajax({
        url: '/data/transaction/' + tranId,
        type: 'DELETE',
        success: function () {
            hideTransactionDetails();
            generalTable.api().ajax.reload();

            $('.table-message').append('' +
                '<div class="alert alert-success alert-dismissable">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                '<strong>Success!</strong> Transaction has been deleted.' +
                '</div>' +
                '');

        },
        error: function () {
            $('.table-message').append('' +
                '<div class="alert alert-danger alert-dismissable">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                '<strong>Error!</strong> Can not delete the transaction.' +
                '</div>' +
                '');
        }
    })
}