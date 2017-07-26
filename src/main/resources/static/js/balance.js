class ButtonsPanel extends React.Component {
    componentDidMount() {
    }

    render() {
        return (
            <div className="navbar">
                <div className="navbar-inner">
                    <div className="container">
                        <ul className="nav nav-pills">
                            <li className={this.props.isShort ? "active" : ""}>
                                <a href="#" onClick={() => this.props.onBalanceTypeClick(true)}>Short Balance</a>
                            </li>
                            <li className={this.props.isShort ? "" : "active"}>
                                <a href="#" onClick={() => this.props.onBalanceTypeClick(false)}>Balance</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }

}

class BalanceSheet extends React.Component {

    componentDidMount() {
        var valueDate = new Date();
        initShortBalanceTable(moment(valueDate).format('YYYY-MM-DD'));
    }

    render() {
        return (
            <div className="panel-body">
                <table className="table table-striped balance"></table>
            </div>
        );
    }
}

class BalancePanel extends React.Component {
    constructor() {
        super();
        this.state = {
            isShort: true
        };

        this.handleBalanceTypeClick = this.handleBalanceTypeClick.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
    }

    componentDidMount() {
        $('#value-datepicker').datetimepicker({
            format: 'll',
            defaultDate: new Date()
        });

        $('#value-datepicker').on("dp.change", this.handleDateChange);

        $('.open-value-datepicker').click(function (event) {
            event.preventDefault();
            $('#value-datepicker').click();
        });
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-12">
                    <div className="content-box-large">
                        <div className="row">
                            <div className="col-md-9">
                                <ButtonsPanel isShort={this.state.isShort}
                                              onBalanceTypeClick={this.handleBalanceTypeClick}/>
                            </div>
                            <label className="col-md-1 control-label"><b>Reporting date</b></label>
                            <div className="col-md-2">
                                <div className='input-group date'>
                                    <input type='text' className="form-control" id='value-datepicker'
                                           required="required"/>
                                    <label className="input-group-addon btn" htmlFor="value-datepicker">
                                        <span className="glyphicon glyphicon-calendar open-value-datepicker"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <BalanceSheet/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }


    handleBalanceTypeClick(setShort) {

        this.setState({
            isShort: setShort
        });
        var valueDate = $("#value-datepicker").data("DateTimePicker").date().format('YYYY-MM-DD');

        if (setShort === true) {
            initShortBalanceTable(valueDate);
        } else {
            initBalanceTable(valueDate);
        }
    }

    handleDateChange(event){
        var valueDate = event.date.format('YYYY-MM-DD');

        const isShort = this.state.isShort;
        if(isShort) {
            initShortBalanceTable(valueDate);
        }else {
            initBalanceTable(valueDate);
        }
    }
}

// ====================================================================
$(document).ready(function () {
    ReactDOM.render(<BalancePanel />, document.getElementById("root"));
});

function initShortBalanceTable(valueDate) {
    var bookNumber = sessionStorage.getItem("bookId");

    $.ajax({
        url: '/data/balance/book/' + bookNumber + '/short?date=' + valueDate,
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

                if (entry.title === 'Article') {
                    entry.render = function (data, type, row) {
                        return getAccountCategoryText(data);
                    };

                }
            });

            // destroy and empty previous dataTable
            if ($.fn.DataTable.isDataTable('.table.balance')) {
                $('.table.balance').dataTable().fnDestroy();
                $('.table.balance').empty();
            }

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


function initBalanceTable(valueDate) {
    var bookNumber = sessionStorage.getItem("bookId");

    $.ajax({
        url: '/data/balance/book/' + bookNumber + '?date=' + valueDate,
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

                if (entry.hidden) {
                    entry.visible = false;
                }

                if (entry.title === 'Category') {
                    entry.render = function (data, type, row) {
                        return getAccountCategoryText(data);
                    };

                }

            });

            // destroy and clear previous dataTable
            if ($.fn.DataTable.isDataTable('.table.balance')) {
                $('.table.balance').dataTable().fnDestroy();
                $('.table.balance').empty();
            }

            $('.table.balance').DataTable({
                paging: false,
                info: false,
                ordering: true,
                searching: true,
                select: true,
                data: data.data,
                columns: data.columns
            });

        },
        error: function () {
        }
    });
}
