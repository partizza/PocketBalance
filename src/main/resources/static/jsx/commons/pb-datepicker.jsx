import React from "react";
import PropTypes from 'prop-types';

export default class PbDatePicker extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};

        // Date
        if (this.props.defaultDate) {
            this.state.value = this.props.defaultDate;
        } else {
            this.state.value = new Date();
        }

        // format
        if (this.props.format) {
            this.state.format = this.props.format;
        } else {
            this.state.format = 'll';
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleLabelClick = this.handleLabelClick.bind(this);
    }

    componentDidMount() {
        $('#' + this.props.id).datetimepicker({
            format: this.state.format,
            defaultDate: this.state.value
        });

        $('#' + this.props.id).on("dp.change", this.handleChange);

        $('.open-' + this.props.id).on("click", this.handleLabelClick);
    }

    render() {
        return (
            <div className="input-group date">
                <input type="text" className="form-control" id={this.props.id} required="required"/>
                <label className="input-group-addon btn" htmlFor={this.props.id}>
                    <span className={'glyphicon glyphicon-calendar open-' + this.props.id}></span>
                </label>
            </div>
        )
    }

    handleChange(event) {
        this.setState({value: event.date.toDate()});

        if (this.props.onChange) {
            this.props.onChange(event.date.toDate());
        }
    }

    handleLabelClick(event){
        event.preventDefault();
        $('#' + this.props.id).click();
    }
}


PbDatePicker.propTypes = {
    id: PropTypes.string.isRequired,
    value: PropTypes.instanceOf(Date)
}