$(document).ready(function () {

    $('.table.entries').DataTable({
        searching: false,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/data/entry'
        },
        columns: [
            {title: "Date", name: "header.valueDate", data: "headerValueDate"},
            {
                title: "Category",
                name: "account.bsCategory",
                data: "accountBsCategory",
                render: function (data, type, row) {
                    return getAccountCategoryText(data);
                }
            },
            {title: "accId", name: "account.accId", data: "accountAccId", visible: false},
            {title: "Account", name: "account.name", data: "accountName"},
            {
                title: "Amount",
                name: "trnAmount",
                data: "trnAmount",
                render: function (data, type, row) {
                    return accounting.formatMoney(Number(data), {
                        symbol: "",
                        precision: 2,
                        format: {
                            pos: "%s %v",
                            neg: "%s (%v)",
                            zero: "%s  --"
                        }
                    });
                }
            },
            {title: "curId", name: "currency.id", data: "currencyId", visible: false},
            {title: "Currency", name: "currency.code", data: "currencyCode"},
            {title: "Comment", name: "header.desc", data: "headerDesc"}
        ]
    })
    ;

});