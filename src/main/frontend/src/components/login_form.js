import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { loginUser } from '../actions';
import validator from 'validator';

const AT_LEAST_ONE_SMALL_LETTER_PATTERN = /^(?=.*[a-z]).+$/;
const AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN = /^(?=.*[A-Z]).+$/;
const AT_LEAST_ONE_DIGIT_PATTERN = /^(?=.*\d).+$/;

class LoginForm extends Component {
  renderField(field) {
      const {meta: {touched, error}} = field;
      const className = `form-group ${touched && error ? 'has-danger':''}`;
      return (
        <div className={className}>
          <label>{field.label}</label>
          <input className="form-control"
            type="text"
            {...field.input}
          />
          <div className="text-help">
            {touched ? error:''}
          </div>
        </div>
      );
  }

  onSubmit(values) {
    const { history } = this.props;
    let original_pathname = this.props.location.state.from.pathname;
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
        <Field
          name="username"
          label="Username"
          component={this.renderField}
        />
        <Field
          name="password"
          label="Password"
          component={this.renderField}
        />
        <button type="submit" className="btn btn-primary">Enter</button>
      </form>
    );
  }
}

function validate(values) {
  const errors = {};
  var password = values.password || '';
  var username = values.username || '';

  if (!validator.isLength(password, {min:8, max: 30})) {
    errors["password"] = "Password must be between 8 and 30 characters long";
  } else if (!validator.matches(password, AT_LEAST_ONE_SMALL_LETTER_PATTERN)) {
    errors["password"] = "Password must contain at least one small letter";
  } else if (!validator.matches(password, AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN)) {
    errors["password"] = "Password must contain at least one capital letter";
  } else if (!validator.matches(password, AT_LEAST_ONE_DIGIT_PATTERN)) {
    errors["password"] = "Password must contain at least one digit";
  }

  if (!validator.isEmail(username)) {
    errors["username"] = "Username must be an email address";
  }

  return errors;
}

export default reduxForm({
  validate,
  form:'LoginForm'
})(
  connect(null, { loginUser })(LoginForm)
);
