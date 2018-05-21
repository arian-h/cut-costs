import React, { Component } from 'react';
import { reduxForm, Field } from 'redux-form';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Form, Button } from 'semantic-ui-react';

import { registerUser } from '../../actions';
import { validatePassword, validateEmail, validateName } from '../../helpers/auth_utils';
import { renderInputField } from '../../helpers/form_utils';

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
      <Form onSubmit={ handleSubmit(this.onSubmit.bind(this)) } error>
        <Field name="name" validate={ validateName } placeholder="Name" type="text" component={renderInputField}/>
        <Field name="username" validate={ validateEmail } placeholder="Email" type="text" component={renderInputField}/>
        <Field name="password" placeholder="Password" validate={ validatePassword } type="password" component={renderInputField}/>
        <Button type="submit" content="Sign up"/>
        <Button as={ Link } to='/login' content="Already signed up"/>
      </Form>
    );
  }
}

export default reduxForm({
  //a unique id for this form
  form:'RegisterForm'
})(
  connect(null, { registerUser })(RegisterForm)
);
