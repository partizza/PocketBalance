var table;

$(document).ready(function () {

    initAccountsDataTable();

    $('#btn-create-account').click(createBalanceAccount);

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
                "initComplete": function (settings, json) {
                    $('#accountsTable tbody').on('click', 'tr', function () {
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

                table.api().ajax.reload();
            },
            error: function () {
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

