import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { loginUser } from '../../actions';
import {validatePassword, validateEmail} from '../../helpers/auth_utils';
import { renderField, validate } from '../../helpers/form_utils';

const FIELDS = {
  username: {
    type: 'text',
    label: 'Email',
    validate: validateEmail
  },
  password: {
    type: 'password',
    label: 'Password',
    validate: validatePassword
  }
};

class LoginForm extends Component {

  _login(response) {
    const redirected_from = this.props.location.state ? this.props.location.state.from.pathname : '/';
    const { history } = this.props;
    const { status, headers } = response;
    if (status === 200) {
      const { authorization } = headers;
      localStorage.setItem('jwt_token', authorization);
      history.push(redirected_from);
    } // TODO else, show error message
  }

  onSubmit(values) {
    this.props.loginUser(values, this._login.bind(this));
  }

  render() {
    const { handleSubmit } = this.props;
    return (
      <div className="auth-form-container">
        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
          {_.map(FIELDS, renderField.bind(this))}
          <button type="submit" className="btn btn-primary">Login</button>
          <Link className="auth-switch-link" to='/register'>Sign up</Link>
        </form>
      </div>
    );
  }
}

export default reduxForm({
  validate,
  //a unique id for this form
  form:'LoginForm',
  fields: _.keys(FIELDS),
  fields_def: FIELDS
})(
  connect(null, { loginUser })(LoginForm)
);
