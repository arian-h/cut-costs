import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { loginUser } from '../../actions';
import _ from 'lodash';
import {validatePassword, validateEmail, AT_LEAST_ONE_SMALL_LETTER_PATTERN, AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN, AT_LEAST_ONE_DIGIT_PATTERN} from '../../helpers/auth_utils';

const FIELDS = {
  username: {
    type: 'input',
    label: 'Email',
    validate: validateEmail
  },
  password: {
    type: 'input',
    label: 'Password',
    validate: validatePassword
  }
};

class LoginForm extends Component {
  renderField(fieldConfig, field) {
    debugger;
    //this is provided by redux-form
    const fieldHelper = this.props.fields[field];
    const {touched, invalid, error} = fieldHelper;
    const className = `auth-form form-group ${touched && invalid ? 'has-danger':''}`;
    return (
      <div className={className} key={field}>
        <label>{fieldConfig.label}</label>
        <fieldConfig.type className="form-control"
          type="text"
          {...fieldHelper}
        />
        <div className="text-help">
          {touched ? error[field]:''}
        </div>
      </div>
    );
  }

  onSubmit(values) {
    const { history } = this.props;
    let original_pathname = '/';
    if (this.props.location.state) {
      original_pathname = this.props.location.state.from.pathname;
    }
    this.props.loginUser(values, ({status, headers}) => {
      if (status === 200) {
        const { authorization } = headers;
        localStorage.setItem('jwt_token', authorization);
        history.push(original_pathname);
      }
    });
  }

  render() {
    const {handleSubmit} = this.props;
    return (
      <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
        {_.map(FIELDS, this.renderField.bind(this))}
        <button type="submit" className="btn btn-primary">Enter</button>
      </form>
    );
  }
}

function validate(values) {
  let errors = {};
  _.each(FIELDS, (val, key) => {
    errors[key] = val.validate(values[key]);
  });
  return errors;
}

export default reduxForm({
  validate,
  //a unique id for this form
  form:'LoginForm',
  fields: _.keys(FIELDS)
})(
  connect(null, { loginUser })(LoginForm)
);
