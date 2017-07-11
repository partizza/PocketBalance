var dataSet = [
    ["1", "Assets", 107000.2, 20],
    ["2", "Liabilities", 0, -20.05],
    ["3", "Equity", 0, 0],
    ["5", "Incomes", 8060, 0],
    ["6", "Expenses", -7060.25, 0.0],
];

var columnMap = [
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
];

$(document).ready(function () {
    // $('.table.balance').DataTable({
    //     paging: false,
    //     info: false,
    //     ordering: true,
    //     searching: false,
    //     select: true,
    //     data: dataSet,
    //     columns: columnMap
    // });

    showBalance();
});

function showBalance() {
    var bookNumber = sessionStorage.getItem("bookId");

    $.ajax({
        url: '/data/balance/book/' + bookNumber + '/short',
        type: 'GET',
        dataType: 'json',
        success: function (data) {

            data.columns.forEach(function (entry) {
                if (entry.number) {
                    entry.render = function (data, type, row) {
                        return accounting.formatMoney(Number(data), {
                            symbol: "",
                            precision: 2,
                            format: {
                                pos: "%s %v",
                                neg: "%s (%v)",
                                zero: "%s  --"
                            }
                        });
                    };
                }
            });

            $('.table.balance').DataTable({
                paging: false,
                info: false,
                ordering: true,
                searching: false,
                select: true,
                data: data.data,
                columns: data.columns
            });

        },
        error: function () {
        }
    });
}
