import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { registerUser } from '../../actions';
import {validatePassword, validateEmail, validateName} from '../../helpers/auth_utils';
import { renderField, validate } from '../../helpers/form_utils';

const FIELDS = {
  name: {
    label: 'Username',
    validate: validateName,
    fieldType: 'input',
    props: {
      type: 'text'
    }
  },
  username: {
    label: 'Email',
    validate: validateEmail,
    fieldType: 'input',
    props: {
      type: 'text'
    }
  },
  password: {
    label: 'Password',
    validate: validatePassword,
    fieldType: 'input',
    props: {
      type: 'password'
    }
  }
};

class RegisterForm extends Component {

  onSubmit(values) {
    const { history } = this.props;
    this.props.registerUser(values, ({status, headers}) => {
      if (status === 200) {
        const { authorization } = headers;
        localStorage.setItem('jwt_token', authorization);
        history.push('/');
      }
    });
  }

  render() {
    const {handleSubmit} = this.props;
    return (
      <div className="auth-form-container">
        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
          {_.map(FIELDS, renderField.bind(this))}
          <button type="submit" className="btn btn-primary">Sign up</button>
          <Link className="auth-switch-link" to='/login'>Already Registered</Link>
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
  connect(null, { registerUser })(RegisterForm)
);
