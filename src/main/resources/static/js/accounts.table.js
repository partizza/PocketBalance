$(document).ready(function () {
    $('#accountsTable').dataTable({
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "ajax": {
            "url": "/data/account/book/999888777/all?size=10000",
            "dataSrc": "content"
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