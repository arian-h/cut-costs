import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, getFormSyncErrors } from 'redux-form';
import _ from 'lodash';

import { createExpense } from '../../actions';
import { validateName, validateDescription, validateAmount, validateImage } from '../../helpers/expense_utils';
import { renderInputField, renderTextAreaField, renderDropzoneField } from '../../helpers/form_utils';

class NewExpense extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: undefined,
      imagePreviewUrl: undefined
    };
  }

  _onSubmit = values => {
    // let reader = new FileReader();
    // reader.onloadend = e => {
    //   var imageValue = reader.result;
    //   this.props.createExpense(values, imageValue, this.props.groupId, () => this.props.onClose(), error => this.setState({error: error}));
    // };
    // reader.readAsDataURL(values.image[0]);
    let formData = new FormData();
    formData.append('title', values.title);
    formData.append('description', values.description);
    formData.append('amount', values.amount);
    formData.append('image', values.image[0], values.image[0].name);
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
    return (
      <div>
        <form onSubmit={ handleSubmit(this._onSubmit.bind(this)) }>
          <Field name="title" validate={ validateName } label="Name" type="text" component={ renderInputField }/>
          <Field name="description" validate={ validateDescription } label="Description" type="text" component={ renderTextAreaField }/>
          <Field name="amount" validate={ validateAmount } label="Amount" type="text" component={ renderInputField }/>
          <Field name="image" validate={validateImage} label="Image" onChange={this._onImagePreviewChange.bind(this)} previewUrl={ imagePreviewUrl } component={ renderDropzoneField } />
          <button type="submit" className="btn btn-primary">Create</button>
          <button type="button" className="btn btn-primary" onClick={this.props.onClose}>Cancel</button>
        </form>
        { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
      </div>
    );
  }
}

export default connect(state => ({
  inputErrors: getFormSyncErrors('NewExpense')(state)
}), { createExpense })(reduxForm({
  form:'NewExpense'
})(NewExpense));
