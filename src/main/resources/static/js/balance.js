var dataSet = [
    ["1", "Assets", 107000.2, 20],
    ["2", "Liabilities", 0, -20.05],
    ["3", "Equity", 0, 0],
    ["5", "Incomes", 8060, 0],
    ["6", "Expenses", -7060.25, 0.0],
];

$(document).ready(function () {
    $('.table.balance').DataTable({
        paging: false,
        info: false,
        ordering: true,
        searching: false,
        select: true,
        data: dataSet,
        columns: [
            {title: "#"},
            {title: "Article"},
            {
                title: "UAH",
                render: $.fn.dataTable.render.number(',', '.', 2)
            },
            {
                title: "USD",
                render: $.fn.dataTable.render.number(',', '.', 2)
            }
        ]
    });
});
