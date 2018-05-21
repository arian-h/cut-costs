import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, getFormSyncErrors } from 'redux-form';
import _ from 'lodash';

import { createExpense } from '../../actions';
import { validateName, validateDescription, validateAmount, validateImage } from '../../helpers/expense_utils';
import { renderInputField, renderTextAreaField, renderDropzoneField } from '../../helpers/form_utils';
import { Modal, Button, Form } from 'semantic-ui-react';

class NewExpense extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  _onSubmit = values => {
    let formData = new FormData();
    formData.append('title', values.title);
    formData.append('description', values.description);
    formData.append('amount', values.amount);
    if (values.image && values.image.length > 0) {
      formData.append('image', values.image[0], values.image[0].name);
    }
    this.props.createExpense(formData, this.props.groupId, () => this.props.onClose(), error => this.setState({error: error}));
  }

  componentWillReceiveProps(newProps) {
    if (newProps.inputErrors && newProps.inputErrors !== this.props.inputErrors) {
      if (newProps.inputErrors.image) {
        this.setState({imagePreviewUrl: undefined});
      }
    }
  }

  _onImagePreviewChange = files => {
    this.setState({
      imagePreviewUrl: files[0].preview
    });
  }

  render() {
    const { handleSubmit } = this.props;
    const { imagePreviewUrl } = this.state;
    return ([
      <Modal.Header>
        Add Expense
      </Modal.Header>,
        <Modal.Content scrolling>
          <Form onSubmit={ handleSubmit(this._onSubmit.bind(this)) } error id="new-expense-form">
            <Field name="title" validate={ validateName } label="Name" type="text" component={ renderInputField }/>
            <Field name="description" validate={ validateDescription } label="Description" type="text" component={ renderTextAreaField }/>
            <Field name="amount" validate={ validateAmount } label="Amount" type="text" component={ renderInputField }/>
            <Field name="image" validate={ validateImage } label="Photo" onChange={ this._onImagePreviewChange.bind(this) } previewUrl={ imagePreviewUrl } component={ renderDropzoneField } />
            { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
          </Form>
        </Modal.Content>,
        <Modal.Actions>
          <Button type="submit" form="new-expense-form" content="Create"/>
        </Modal.Actions>
    ]);
  }
}

export default connect(state => ({
  inputErrors: getFormSyncErrors('NewExpense')(state)
}), { createExpense })(reduxForm({
  form:'NewExpense'
})(NewExpense));
