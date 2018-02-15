import React, { Component } from 'react';
import { reduxForm, Field } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { registerUser } from '../../actions';
import { validatePassword, validateEmail, validateName } from '../../helpers/auth_utils';
import { renderField, validate } from '../../helpers/form_utils';

const validators = [
  {
    field: 'password',
    validator: validatePassword
  },
  {
    field: 'username',
    validator: validateEmail
  },
  {
    field: 'name',
    validator: validateName
  }
];

class RegisterForm extends Component {

  _failedSignupCallback() {
    debugger;
    //show up the message
  }

  onSubmit(values) {
    const { history } = this.props;
    this.props.registerUser(values, this._failedSignupCallback);
  }

  render() {
    const { handleSubmit } = this.props;
    return (
      <div className="auth-form-container">
        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
          <Field name="name" placeholder="Name" type="text" fieldType="input" component={renderField}/>
          <Field name="username" placeholder="Email" type="text" fieldType="input" component={renderField}/>
          <Field name="password" placeholder="Password" type="password" fieldType="input" component={renderField}/>
          <button type="submit" className="btn btn-primary">Sign up</button>
          <Link className="auth-switch-link" to='/login'>Already Registered</Link>
        </form>
      </div>
    );
  }
}

export default reduxForm({
  validate,
  validators,
  //a unique id for this form
  form:'RegisterForm'
})(
  connect(null, { registerUser })(RegisterForm)
);
