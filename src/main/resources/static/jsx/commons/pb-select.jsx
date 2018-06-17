import React from "react";
import ReactDom from "react-dom";

export default class PbSelect extends React.Component {

    constructor(props) {
        super(props);

        this.state = {value: props.value};

        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        $(this.select).selectpicker(this.state.value);
    }

    componentDidUpdate(prevProps, prevState) {
        $(this.select).selectpicker(this.state.value);
    }

    render() {
        return (
            <select className="selectpicker" data-width={this.props.dataWidth}
                    multiple={this.props.multiple}
                    ref={(select) => {
                        this.select = select;
                    }}
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