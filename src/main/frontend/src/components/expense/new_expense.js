import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';

import { createExpense } from '../../actions';
import { validateName, validateDescription, validateAmount } from '../../helpers/expense_utils';
import { renderField, validate } from '../../helpers/form_utils';
import ImageUpload from '../platform/imageUpload';

const validators = [{
    field: 'title',
    validator: validateName
  },
  {
    field: 'description',
    validator: validateDescription
  },
  {
    field: 'amount',
    validator: validateAmount
  }
];

class NewExpense extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null
    };
  }

  _onSubmit = (values) => {
    this.props.createExpense(values, this.props.groupId, () => this.props.onClose(), error => this.setState({error: error}));
  }

  render() {
    const { handleSubmit } = this.props;
    return (
      <div>
        <form onSubmit={handleSubmit(this._onSubmit.bind(this))}>
          <Field name="title" label="Name" type="text" fieldType="input" component={renderField.bind(this)}/>
          <Field name="description" label="Description" type="text" fieldType="input" component={renderField.bind(this)}/>
          <Field name="amount" label="Amount" type="text" fieldType="input" component={renderField.bind(this)}/>
          <ImageUpload noPreviewClassName="" previewClassName="expensePreviewClassname"/>
          <button type="submit" className="btn btn-primary">Create</button>
          <button type="button" className="btn btn-primary" onClick={this.props.onClose}>Cancel</button>
        </form>
        { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
      </div>
    );
  }
}

export default connect(null, { createExpense })(reduxForm({
  validate,
  form:'NewExpense',
  validators
})(NewExpense));
