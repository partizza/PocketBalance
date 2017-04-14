$(document).ready(function () {
    $('#accountsTable').dataTable({
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "ajax": {
            "url": "/data/account/book/1/all",
            "dataSrc": ""
        },
        "columns": [
            {"data": "bsCategory"},
            {"data": "name"},
            {"data": "desc"},
            {"data": "accId"},
            {"data": "enable"}
        ]
    });

});