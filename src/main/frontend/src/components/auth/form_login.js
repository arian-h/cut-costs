import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { loginUser } from '../../actions';
import { validatePassword, validateEmail } from '../../helpers/auth_utils';
import { renderInputField, validate } from '../../helpers/form_utils';

//TODO: fix the signup page


const validators = [
  {
    field: 'password',
    validator: validatePassword
  },
  {
    field: 'username',
    validator: validateEmail
  }
];

class LoginForm extends Component {

  _unauthorizedLoginCallback() {
    debugger;
    //TODO: set the state to show an error
  }

  onSubmit(values) {
    const redirected_from = this.props.location.state ? this.props.location.state.from.pathname : '/';
    this.props.loginUser(values, redirected_from, this._unauthorizedLoginCallback);
  }

  render() {
    const { handleSubmit } = this.props;
    return (
      <div className="auth-form-container">
        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
          <Field name="username" placeholder="Email" type="text" component={renderInputField}/>
          <Field name="password" placeholder="Password" type="password" component={renderInputField}/>
          <button type="submit" className="btn btn-primary">Login</button>
          <Link className="auth-switch-link" to='/register'>Sign up</Link>
        </form>
      </div>
    );
  }
}

export default reduxForm({
  validate,
  validators,
  //a unique id for this form
  form:'LoginForm'
})(
  connect(null, { loginUser })(LoginForm)
);
