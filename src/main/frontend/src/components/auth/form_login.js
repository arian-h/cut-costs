import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Form, Button } from 'semantic-ui-react';

import { loginUser } from '../../actions';
import { validatePassword, validateEmail } from '../../helpers/auth_utils';
import { renderInputField, validate } from '../../helpers/form_utils';

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
      <Form onSubmit={ handleSubmit(this.onSubmit.bind(this)) } error>
        <Field name="username" validate={ validateEmail } placeholder="Email" type="text" component={ renderInputField }/>
        <Field name="password" validate={ validatePassword } placeholder="Password" type="password" component={ renderInputField }/>
        <Button type="submit" content="Login" />
        <Button as={ Link } to="/register" content="Sign up" />
      </Form>
    );
  }
}

export default reduxForm({
  //a unique id for this form
  form:'LoginForm'
})(
  connect(null, { loginUser })(LoginForm)
);
