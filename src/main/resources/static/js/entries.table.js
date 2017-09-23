$(document).ready(function () {
    // $('.table.entries').DataTable( {
    //     processing: true,
    //     serverSide: true,
    //     ajax: {
    //         url: '/data/entry'
    //     }
    // } );

    $('.table.entries').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: '/data/entry'
        },
        columns: [
            {title: "Date", name: "header.valueDate", data: "headerValueDate"},
            {title: "Category", name:"account.bsCategory", data: "accountBsCategory"},
            {title: "accId", name:"account.accId", data: "accountAccId", visible: false},
            {title: "Account", name:"account.name", data: "accountName"},
            {title: "Comment", name:"header.desc", data: "headerDesc"},
            {title: "curId", name:"currency.id", data: "currencyId", visible: false},
            {title: "Currency", name:"currency.code", data: "currencyCode"},
            {title: "Amount", name:"trnAmount", data: "trnAmount"}
        ]
    });
});