// ========== COMPONENTS ============

class EntryValueDatePicker extends React.Component {

    componentDidMount() {

        $('#value-datepicker1').datetimepicker({
            format: 'll',
            defaultDate: new Date()
        });

        $('.open-value-datepicker1').on("click", function (event) {
            event.preventDefault();
            $('#value-datepicker1').click();
        });

        $('#select-value-date-criteria').selectpicker('refresh');
    }


    render() {
        return (
            <div>
                <div className="col-md-1">Value date</div>
                <div className="col-md-1">
                    <select className="selectpicker" id="select-value-date-criteria" data-width="fit">
                        <option value="LESS_OR_EQUAL">&lt;=</option>
                        <option value="LESS">&lt;</option>
                    </select>
                </div>
                <div className="col-md-2">
                    <div className="input-group date">
                        <input type="text" className="form-control" id="value-datepicker1" required="required"/>
                        <label className="input-group-addon btn" htmlFor="value-datepicker1">
                            <span className="glyphicon glyphicon-calendar open-value-datepicker1"></span>
                        </label>
                    </div>
                </div>
            </div>

        );
    }
}

class EntryPageContent extends React.Component {
    componentDidMount() {
        initEntryDataTable();
    }

    render() {
        return (
            <div className="content-box-large">
                <div className="row">
                    <div className="col-md-10">
                        <div className="panel-heading">
                            <div className="panel-title"><b>Entries</b></div>
                        </div>
                    </div>
                </div>
                <div className="panel-body">
                    <div className="row">
                        <EntryValueDatePicker/>
                    </div>
                    <div className="row">
                        <table className="table table-striped entries" id="t_entries"></table>
                    </div>
                </div>
            </div>
        );
    }
}


// ========== init page content ==========

$(document).ready(function () {
    ReactDOM.render(<EntryPageContent />, document.getElementById("root"));
});


// ========== functions ==================


function initEntryDataTable() {

    $('.table.entries').DataTable({
        searching: false,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/data/entry',
            data: function (params) {
                params.filters = [];
                var dateFilter = {column: "header.valueDate", criteria: "=", value: "10"};
                params.filters.push(dateFilter);
            }
        },
        columns: [
            {title: "Value date", name: "header.valueDate", data: "headerValueDate"},
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
        ],
        initComplete: function () {
            $("#t_entries_wrapper > div:nth-child(1) > div:nth-child(2)")
                .html('<div class="col-md-7 col-md-offset-5">' +
                    '<div class="row">' +
                    '<div class="col-md-4">' +
                    'Value date ' +
                    '</div>' +
                    '<div class="col-md-8">' +
                    '<div class="input-group date">' +
                    '<input type="text" class="form-control" id="value-datepicker" required="required"/>' +
                    '<label class="input-group-addon btn" htmlFor="value-datepicker">' +
                    '<span class="glyphicon glyphicon-calendar open-value-datepicker"></span>' +
                    '</label>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>');

            $('#value-datepicker').datetimepicker({
                format: 'll',
                defaultDate: new Date()
            });


            // $("<b>Custom tool bar! Text/images etc.</b>").insertBefore("#t-entries_processing");
            // this.api().columns().every(function () {
            //     var column = this;
            //     var select = $('<select><option value=""></option></select>')
            //         .appendTo($(column.header()));
            //
            //
            // });
        }
    });
};
