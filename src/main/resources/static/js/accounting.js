class CurrencySelect extends React.Component {

    componentDidMount() {
        $('#currency-select').selectpicker();
        this.setItems();
    }


    render() {
        return (
            <select className="selectpicker form-control" id="currency-select"
                    data-live-search="true"
                    title="Choose one of the following..." required="required">
            </select>
        );
    }

    setItems() {
        $.ajax({
            type: 'GET',
            url: '/data/accounting/currency/all',
            dataType: 'json',
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    $("#currency-select").append('<option value="' + data[i].id + '">' + data[i].code + ' (' + data[i].name + ')' + '</option>');
                }
                $("#currency-select").selectpicker("refresh");
            },
            error: function () {
            }
        });
    }
}

class TransactionSelect extends React.Component {

    componentDidMount() {
        $('#tran-select').selectpicker();
    }


    render() {
        return (
            <select className="selectpicker form-control" id="tran-select"
                    data-live-search="true"
                    title="Choose one of the following..." required="required">
            </select>
        );
    }
}

class ValueDatePicker extends React.Component {

    componentDidMount() {
        $('#value-datepicker').datetimepicker({
            format: 'll',
            defaultDate: new Date()
        });

        $('.open-value-datepicker').click(function (event) {
            event.preventDefault();
            $('#value-datepicker').click();
        });
    }

    render() {
        return (
            <div className='input-group date'>
                <input type='text' className="form-control" id='value-datepicker'/>
                <label className="input-group-addon btn" htmlFor="value-datepicker">
                    <span className="glyphicon glyphicon-calendar open-value-datepicker"></span>
                </label>
            </div>
        );
    }
}

class TransactionForm extends React.Component {
    componentDidMount() {
        $('#value-date').datetimepicker();
    }

    render() {
        const title = getTransactionTypeText(this.props.actCategoryKey);
        return (
            <div className="col-md-9">
                <div className="content-box-large col-md-6 col-md-offset-3">
                    <div className="panel-heading">
                        <div className="panel-title"><h2>{title}</h2></div>
                    </div>
                    <br/>
                    <hr/>
                    <div className="panel-body">
                        <form action="">
                            <fieldset>
                                <div className="form-group">
                                    <label>Transaction</label>
                                    <br/>
                                    <TransactionSelect/>
                                </div>
                                <div className="form-group">
                                    <label>Value date</label>
                                    <ValueDatePicker/>
                                </div>
                                <div className="form-group">
                                    <label>Currency</label>
                                    <br/>
                                    <CurrencySelect/>
                                </div>
                                <div className="form-group">
                                    <label>Amount</label>
                                    <input className="form-control" type="text" id="tran-amount"
                                           required="required" maxLength="10"/>
                                </div>

                                <div className="form-group">
                                    <label>Comment</label>
                                    <textarea className="form-control" placeholder="Textarea" rows="3"
                                              maxLength="60" style={{resize: 'none'}}></textarea>
                                </div>
                            </fieldset>
                            <div>
                                <div className="btn btn-primary pull-right">
                                    Commit
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

class Category extends React.Component {
    render() {
        const isMin = this.props.isMin;
        if (isMin) {
            return (
                <a href="#" className={this.getClassMin()} role="button" id={getTransactionTypeKey(this.props.txt)}
                   onClick={() => this.props.onCategoryClick(getTransactionTypeKey(this.props.txt))}>
                    <span>
                        <img src={this.props.imgSrc} className="img-responsive" alt={this.props.txt}/>
                    </span>
                </a>
            );
        } else {
            return (
                <a href="#" className={this.getClassMax()} role="button" id={getTransactionTypeKey(this.props.txt)}
                   onClick={() => this.props.onCategoryClick(getTransactionTypeKey(this.props.txt))}>
                <span>
                    <img src={this.props.imgSrc}/>
                </span>
                    <br/>{this.props.txt}
                </a>
            );
        }
    }

    getClassMax() {
        return "btn btn-lg pb-lg-buttons " + this.props.btnColor;
    }

    getClassMin() {
        return "btn btn-lg pb-md-buttons " + this.props.btnColor;
    }
}

function CategoryListHeader(props) {
    const isMin = props.isMin;
    if (isMin) {
        return null;
    } else {
        return (
            <div className="content-box-header panel-heading">
                <h3 className="panel-title">
                    <span className="glyphicon glyphicon-th-large"></span> Transaction categories</h3>
            </div>
        );
    }
}


class CategoryList extends React.Component {
    render() {
        return (
            <div className={this.props.isMin ? "col-md-3" : "col-md-12"}>
                <CategoryListHeader isMin={this.props.isMin}/>
                <div className={this.props.isMin ? "content-box-large" : "content-box-large box-with-header"}>
                    <div className="row">
                        <div className="col-md-12 text-center">
                            <Category txt="Children" imgSrc="/images/children48.png" btnColor="btn-success"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Entertainment" imgSrc="/images/fun48.png" btnColor="btn-info"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Food" imgSrc="/images/food48.png" btnColor="btn-warning"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Gift &amp; Donation" imgSrc="/images/gift48.png" btnColor="btn-danger"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Housing" imgSrc="/images/housing48.png" btnColor="btn-primary"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Income" imgSrc="/images/income48.png" btnColor="btn-info"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Insurance" imgSrc="/images/insurance48.png" btnColor="btn-warning"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Loans" imgSrc="/images/loan48.png" btnColor="btn-danger"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Other" imgSrc="/images/other48.png" btnColor="btn-primary"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Personal care" imgSrc="/images/personal48.png" btnColor="btn-success"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Savings" imgSrc="/images/savings48.png" btnColor="btn-warning"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Taxes" imgSrc="/images/taxes48.png" btnColor="btn-danger"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                            <Category txt="Transport" imgSrc="/images/transport48.png" btnColor="btn-primary"
                                      isMin={this.props.isMin} onCategoryClick={this.props.onCategoryClick}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

}


class AccountingPanel extends React.Component {
    constructor() {
        super();
        this.state = {
            isActive: false
        };

        this.handleCategoryClick = this.handleCategoryClick.bind(this);
    }

    render() {
        if (this.state.isActive) {
            return (
                <div className="row">
                    <TransactionForm actCategoryKey={this.state.actCategoryKey}/>
                    <CategoryList isMin={this.state.isActive} onCategoryClick={this.handleCategoryClick}/>
                </div>
            );
        } else {
            return (
                <div className="row">
                    <CategoryList isMin={this.state.isActive} onCategoryClick={this.handleCategoryClick}/>
                </div>
            );
        }
    }

    handleCategoryClick(key) {
        // const key = event.target.getAttribute('id');

        this.setState({
            isActive: true,
            // actCategoryKey: event.target.getAttribute('id')
            actCategoryKey: key
        });
    }
}

// ====================================================================

$(document).ready(function () {

    ReactDOM.render(<AccountingPanel />, document.getElementById("root"));

});
