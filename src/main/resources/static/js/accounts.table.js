var table;
$(document).ready(function () {

    initDataTable();


});

function initDataTable() {
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
        }
    });
};