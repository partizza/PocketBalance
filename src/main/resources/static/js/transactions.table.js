var generalTable;
var detailTable;

$(document).ready(function () {

    initTransactionsDataTable();
    initDetailsDataTable();
    initModalBalanceAccountsSelectors();

    $('#btn-create-tran').click(createTransaction);
    $('#btn-edit-tran').click(updateTransaction);

    $("#new-tran-modal").on('hidden.bs.modal', function () {
        $('#new-tran-select-dr').val('NA');
        $('#new-tran-select-cr').val('NA');
        $('#new-tran-select-type').val('NA');
        $('#new-tran-select-dr').selectpicker('refresh');
        $('#new-tran-select-cr').selectpicker('refresh');
        $('#new-tran-select-type').selectpicker('refresh');
        $('#new-tran-name').val('');
        $('#new-tran-desc').val('');
    });

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
                        "data": "type",
                        "render": function (data, type, row) {
                            return getTransactionTypeText(data);
                        }

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
                        "defaultContent": "<button class='btn btn-primary btn-xs edit' data-toggle='modal' data-target='#edit-tran-modal'><i class='glyphicon glyphicon-edit'></i> Edit</button>"
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
                        $('#edit-tran-id').val(data.id);
                        $('#edit-tran-name').val(data.name);
                        $('#edit-tran-desc').val(data.desc);
                        $('#edit-tran-select-dr').val(data.details[0].entrySide === 'D' ? data.details[0].accountAccId : data.details[1].accountAccId);
                        $('#edit-tran-select-cr').val(data.details[0].entrySide === 'C' ? data.details[0].accountAccId : data.details[1].accountAccId);
                        $('#edit-tran-select-type').val(data.type);
                        $('#edit-tran-select-type').selectpicker('refresh');
                        $('#edit-tran-select-dr').selectpicker('refresh');
                        $('#edit-tran-select-cr').selectpicker('refresh');
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
            {"data": "accountName"},
            {"data": "accountDesc"},
            {"data": "accountAccId"},
            {"data": "accountEnable"},
            {"data": "accountBsCategory"}
        ]
    });
}

function initModalBalanceAccountsSelectors() {

    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {
            $.ajax({
                type: 'GET',
                url: '/data/transaction/book/' + data.bookId + '/accounts/all',
                dataType: 'json',
                success: function (data) {

                    if (data.hasOwnProperty('ASSET')) {
                        var str = '<optgroup label="Assets">';
                        for (var e in data.ASSET) {
                            str = str + '<option value="' + data.ASSET[e].accId + '">' + data.ASSET[e].name + '</option>';
                        }
                        str += '</optgroup>';
                        $(".selectpicker.tran").append(str);
                    }

                    if (data.hasOwnProperty('LIABILITY')) {
                        var str = '<optgroup label="Liabilities">';
                        for (var e in data.LIABILITY) {
                            str = str + '<option value="' + data.LIABILITY[e].accId + '">' + data.LIABILITY[e].name + '</option>';
                        }
                        str += '</optgroup>';
                        $(".selectpicker.tran").append(str);
                    }

                    if (data.hasOwnProperty('PROFIT')) {
                        var str = '<optgroup label="Profit">';
                        for (var e in data.PROFIT) {
                            str = str + '<option value="' + data.PROFIT[e].accId + '">' + data.PROFIT[e].name + '</option>';
                        }
                        str += '</optgroup>';
                        $(".selectpicker.tran").append(str);
                    }

                    if (data.hasOwnProperty('LOSS')) {
                        var str = '<optgroup label="Loss">';
                        for (var e in data.LOSS) {
                            str = str + '<option value="' + data.LOSS[e].accId + '">' + data.LOSS[e].name + '</option>';
                        }
                        str += '</optgroup>';
                        $(".selectpicker.tran").append(str);
                    }

                    if (data.hasOwnProperty('EQUITY')) {
                        var str = '<optgroup label="Equity">';
                        for (var e in data.EQUITY) {
                            str = str + '<option value="' + data.EQUITY[e].accId + '">' + data.EQUITY[e].name + '</option>';
                        }
                        str += '</optgroup>';
                        $(".selectpicker.tran").append(str);
                    }

                    $('.selectpicker.tran').selectpicker('refresh');

                }
            });

        }
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

function createTransaction(event) {
    if ($('#form-create-tran').parsley().validate()) {
        var name = $("#new-tran-name").val();
        var desc = $("#new-tran-desc").val();
        var drId = $("#new-tran-select-dr").val();
        var crId = $("#new-tran-select-cr").val();
        var type = $("#new-tran-select-type").val();

        var bookNumber = sessionStorage.getItem("bookId");

        var dataObject = {
            'name': name,
            'desc': desc,
            'bookId': bookNumber,
            'type': type,
            'details': [{'accountAccId': drId, 'entrySide': 'D'},
                {'accountAccId': crId, 'entrySide': 'C'}]
        };

        $.ajax({
            url: '/data/transaction/book/' + bookNumber,
            type: 'POST',
            data: JSON.stringify(dataObject),
            contentType: 'application/json',
            success: function () {
                $('#new-tran-modal').modal('hide');
                hideTransactionDetails();
                generalTable.api().ajax.reload();

                $('.table-message').append('' +
                    '<div class="alert alert-success alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Success!</strong> Transaction has been created.' +
                    '</div>' +
                    '');

            },
            error: function () {
                $('#new-tran-modal').modal('hide');

                $('.table-message').append('' +
                    '<div class="alert alert-danger alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Error!</strong> Can not create a new transaction.' +
                    '</div>' +
                    '');
            }
        });
    }
}

function updateTransaction() {
    if ($('#form-edit-tran').parsley().validate()) {
        var id = $("#edit-tran-id").val();
        var name = $("#edit-tran-name").val();
        var desc = $("#edit-tran-desc").val();
        var drId = $("#edit-tran-select-dr").val();
        var crId = $("#edit-tran-select-cr").val();
        var type = $("#edit-tran-select-type").val();

        var dataObject = {
            'id': id,
            'name': name,
            'desc': desc,
            'type': type,
            'details': [{'accountAccId': drId, 'entrySide': 'D'},
                {'accountAccId': crId, 'entrySide': 'C'}]
        };

        $.ajax({
            url: '/data/transaction/' + id,
            type: 'PUT',
            data: JSON.stringify(dataObject),
            contentType: 'application/json',
            success: function () {
                $('#edit-tran-modal').modal('hide');
                hideTransactionDetails();
                generalTable.api().ajax.reload();

                $('.table-message').append('' +
                    '<div class="alert alert-success alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Success!</strong> Transaction has been updated.' +
                    '</div>' +
                    '');

            },
            error: function () {
                $('#edit-tran-modal').modal('hide');

                $('.table-message').append('' +
                    '<div class="alert alert-danger alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Error!</strong> Can not update a transaction.' +
                    '</div>' +
                    '');
            }
        });
    }
}
