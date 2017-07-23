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
        initShortBalanceTable();
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
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-12">
                    <div className="content-box-large">
                        <div className="row">
                            <ButtonsPanel isShort={this.state.isShort}
                                          onBalanceTypeClick={this.handleBalanceTypeClick}/>
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

        if (setShort === true) {
            initShortBalanceTable();
        } else {
            initBalanceTable();
        }
    }
}

// ====================================================================
$(document).ready(function () {
    ReactDOM.render(<BalancePanel />, document.getElementById("root"));
});

function initShortBalanceTable() {
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


function initBalanceTable() {
    var bookNumber = sessionStorage.getItem("bookId");

    $.ajax({
        url: '/data/balance/book/' + bookNumber,
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

                if(entry.hidden){
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
