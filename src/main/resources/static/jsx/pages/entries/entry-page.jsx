import React from "react";
import ReactDom from "react-dom";
import moment from "moment";

import PbSelect from "../../commons/pb-select";
import PbDatePicker from "../../commons/pb-datepicker";


// ========== COMPONENTS ============

class EntryValueDatePicker extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            valueDateCriteria: this.props.valueDateCriteria,
            valueDate: this.props.valueDate
        };

        this.handleValueDateCriteriaChange = this.handleValueDateCriteriaChange.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
    }

    render() {
        return (
            <div>
                <div className="col-md-1">Value date</div>
                <div className="col-md-1">
                    <PbSelect dataWidth="fit" value={this.state.valueDateCriteria}
                              onChange={this.handleValueDateCriteriaChange}>
                        <option value="EQUALS">=</option>
                        <option value="LESS_OR_EQUAL">&lt;=</option>
                        <option value="LESS">&lt;</option>
                    </PbSelect>
                </div>
                <div className="col-md-3">
                    <PbDatePicker id="entry-value-datepicker" defaultDate={this.state.valueDate}
                                  onChange={this.handleDateChange}/>
                </div>
            </div>

        );
    }

    handleValueDateCriteriaChange(value) {
        this.setState({valueDateCriteria: value});

        if (this.props.onChange) {
            this.props.onChange({
                valueDateCriteria: value,
                valueDate: this.state.valueDate
            });
        }
    }

    handleDateChange(value) {
        this.setState({valueDate: value});

        if (this.props.onChange) {
            this.props.onChange({
                valueDateCriteria: this.state.valueDateCriteria,
                valueDate: value
            });
        }
    }
}

class CategorySelect extends React.Component {
    render() {
        return (
            <div>
                <div className="col-md-1">Category</div>
                <div className="col-md-1">
                    <PbSelect dataWidth="fit" multiple>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                    </PbSelect>
                </div>
            </div>
        )
    }
}

class EntryPageContent extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            valueDateCriteria: 'EQUALS',
            valueDate: new Date()
        };

        this.handleValueDatePickerChange = this.handleValueDatePickerChange.bind(this);
        this.handleValueDatePickerChange = this.handleValueDatePickerChange.bind(this);
    }

    componentDidMount() {
        this.initEntryDataTable();
        this.initFilterOptions();
    }

    componentDidUpdate(prevProps, prevState) {
        $(this.table).data('valueDateCriteria', this.state.valueDateCriteria);
        $(this.table).data('valueDate', moment(this.state.valueDate).format('YYYY-MM-DD'));
        $(this.table).DataTable().draw();
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
                        <EntryValueDatePicker valueDateCriteria={this.state.valueDateCriteria}
                                              valueDate={this.state.valueDate}
                                              onChange={this.handleValueDatePickerChange}/>
                    </div>
                    <div className="row">
                        <CategorySelect/>
                    </div>
                    <div className="row">
                        <table className="table table-striped entries" id={this.getEntriesTableId()}
                               data-value-date-criteria={this.state.valueDateCriteria}
                               data-value-date={moment(this.state.valueDate).format('YYYY-MM-DD')}
                               width="100%"
                               ref={(table) => {
                                   this.table = table;
                               }}></table>
                    </div>
                </div>
            </div>
        );
    }

    getEntriesTableId() {
        return "t_entries";
    }

    handleValueDatePickerChange(values) {
        this.setState({
            valueDateCriteria: values.valueDateCriteria,
            valueDate: values.valueDate
        });
    }

    initFilterOptions() {
        $.ajax({
            url: '/data/entry/options/' + sessionStorage.getItem("bookId"),
            type: 'GET',
            dataType: 'json',
            success: function (data) {
            },
            error: function () {
            }
        });
    }

    initEntryDataTable() {
        var table = this.table;
        $(table).DataTable({
            searching: false,
            processing: true,
            serverSide: true,
            ajax: {
                url: '/data/entry',
                data: function (params) {
                    params.bookId = sessionStorage.getItem("bookId");

                    params.filters = [];
                    var dateFilter = {
                        column: "header.valueDate",
                        criteria: $(table).data('valueDateCriteria'),
                        value: $(table).data('valueDate'),
                        valueType: 'date'
                    };
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
            ]
        });
    }

}


ReactDom.render(<EntryPageContent/>, document.getElementById("root"));

