var table;

$(document).ready(function () {

    initAccountsDataTable();

    $('#btn-create-account').click(createBalanceAccount);
    $('#btn-edit-account').click(updateBalanceAccount);

    $("#new-acc-modal").on('hidden.bs.modal', function () {
        $('#new-account-category').val('NA');
        $('#new-account-category').selectpicker('refresh')
        $('#new-account-name').val('');
        $('#new-account-desc').val('');
        $('#new-account-number').val('');
        $('#new-account-active').prop('checked', true);
    });

    $("#edit-acc-modal").on('hidden.bs.modal', function () {
        $('#edit-account-category').val('NA');
        $('#edit-account-category').selectpicker('refresh')
        $('#edit-account-name').val('');
        $('#edit-account-desc').val('');
        $('#edit-account-number').val('');
        $('#edit-account-active').prop('checked', false);
    });

    window.Parsley.addValidator('freeAccountId', {
        requirementType: 'string',
        validateString: function (value, requirement) {
            var response = false;
            var bookNumber = sessionStorage.getItem("bookId");

            $.ajax({
                url: "/data/account/book/" + bookNumber + "/" + value,
                dataType: 'json',
                type: 'GET',
                async: false,
                success: function (data) {
                    response = false;
                },
                error: function () {
                    response = true;
                }
            });

            return response;
        },
        messages: {en: "This number isn't available"}
    });

    window.Parsley.addValidator('existsAccountId', {
        requirementType: 'string',
        validateString: function (value, requirement) {
            var response = false;
            var bookNumber = sessionStorage.getItem("bookId");

            $.ajax({
                url: "/data/account/book/" + bookNumber + "/" + value,
                dataType: 'json',
                type: 'GET',
                async: false,
                success: function (data) {
                    response = true;
                },
                error: function () {
                    response = false;
                }
            });

            return response;
        },
        messages: {en: "Can't update not existing balance account"}
    });

});


function initAccountsDataTable() {
    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {

            table = $('#accountsTable').dataTable({
                "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                "ajax": {
                    "url": "/data/account/book/" + data.bookId + "/all",
                    "dataSrc": ""
                },
                "columns": [
                    {"data": "bsCategory"},
                    {"data": "name"},
                    {"data": "desc"},
                    {"data": "accId"},
                    {"data": "enable"}
                ],
                "order": [
                    [ 0, "asc" ],
                    [ 1, "asc" ],
                ],
                "initComplete": function (settings, json) {
                    $('#accountsTable tbody').on('click', 'tr', function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');
                            $('#btn-show-edit-modal').prop('disabled', true);
                        }
                        else {
                            table.$('tr.active').removeClass('active');
                            $(this).addClass('active');
                            $('#btn-show-edit-modal').prop('disabled', false);
                        }
                    });
                }
            });

            $("#edit-acc-modal").on('show.bs.modal', getSelectedRowData);
        },
        error: function () {
            $('#accounts-table-message').append('' +
                '<div class="alert alert-danger alert-dismissable">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                '<strong>Error!</strong> Data is not available.' +
                '</div>' +
                '');
        }
    });
};


function createBalanceAccount(event) {
    if ($('#form-new-account').parsley().validate()) {
        var category = $("#new-account-category").val();
        var name = $("#new-account-name").val();
        var desc = $("#new-account-desc").val();
        var id = $("#new-account-number").val();
        var active = $("#new-account-active").is(':checked');

        var bookNumber = sessionStorage.getItem("bookId");

        var dataObject = {
            'bsCategory': category,
            'name': name,
            'desc': desc,
            'accId': id,
            'enable': active
        };

        $.ajax({
            url: '/data/account/book/' + bookNumber,
            type: 'POST',
            data: JSON.stringify(dataObject),
            contentType: 'application/json',
            success: function () {
                $('#new-acc-modal').modal('hide');

                $('#accounts-table-message').append('' +
                    '<div class="alert alert-success alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Success!</strong> Balance account has been created.' +
                    '</div>' +
                    '');

                $('#btn-show-edit-modal').prop('disabled', true);
                table.api().ajax.reload();
            },
            error: function () {
                $('#new-acc-modal').modal('hide');

                $('#accounts-table-message').append('' +
                    '<div class="alert alert-danger alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Error!</strong> Can not create a new balance account.' +
                    '</div>' +
                    '');
            }
        });
    }
}

function getSelectedRowData() {
    var values = table.api().row('.active').data();
    $('#edit-account-category').val(values.bsCategory);
    $('#edit-account-category').selectpicker('refresh')
    $('#edit-account-name').val(values.name);
    $('#edit-account-desc').val(values.desc);
    $('#edit-account-number').val(values.accId);
    $('#edit-account-active').prop('checked', values.enable);
}


function updateBalanceAccount(event) {
    if ($('#form-edit-account').parsley().validate()) {
        var category = $("#edit-account-category").val();
        var name = $("#edit-account-name").val();
        var desc = $("#edit-account-desc").val();
        var id = $("#edit-account-number").val();
        var active = $("#edit-account-active").is(':checked');

        var bookNumber = sessionStorage.getItem("bookId");

        var dataObject = {
            'bsCategory': category,
            'name': name,
            'desc': desc,
            'accId': id,
            'enable': active
        };

        $.ajax({
            url: '/data/account/book/' + bookNumber + '/' + id,
            type: 'PUT',
            data: JSON.stringify(dataObject),
            contentType: 'application/json',
            success: function () {
                $('#edit-acc-modal').modal('hide');

                $('#accounts-table-message').append('' +
                    '<div class="alert alert-success alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Success!</strong> Balance account has been updated.' +
                    '</div>' +
                    '');

                $('#btn-show-edit-modal').prop('disabled', true);
                table.api().ajax.reload();
            },
            error: function () {
                $('#edit-acc-modal').modal('hide');

                $('#accounts-table-message').append('' +
                    '<div class="alert alert-danger alert-dismissable">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>' +
                    '<strong>Error!</strong> Can not update the balance account.' +
                    '</div>' +
                    '');
            }
        });
    }
}
