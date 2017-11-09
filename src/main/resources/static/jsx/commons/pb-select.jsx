import React from "react";
import ReactDom from "react-dom";

export default class PbSelect extends React.Component {

    constructor(props) {
        super(props);

        this.state = {value: props.value};

        this.handleChange = this.handleChange.bind(this);
    }

    render() {
        return (
            <select className="selectpicker" data-width={this.props.dataWidth} value={this.state.value}
                    onChange={this.handleChange}>
                {this.props.children}
            </select>
        )
    }

    handleChange(event) {
        this.setState({value: event.target.value});

        if (this.props.onChange) {
            this.props.onChange(event.target.value);
        }
    }
}