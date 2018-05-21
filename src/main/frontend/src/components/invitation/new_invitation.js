import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, getFormSyncErrors } from 'redux-form';

import { inviteUser } from '../../actions';
import { getUserId } from '../../helpers/user_utils';
import { Form, Button, Modal } from 'semantic-ui-react'
import { renderInputField } from '../../helpers/form_utils';

class NewInvitation extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  _onInviteUser() {
    this.props.inviteUser(this.state.inputValue, () => this.props.onClose(), error => this.setState({error: error}));
  }

  render() {
    const { handleSubmit } = this.props;

    return ([
      <Modal.Content>
        <Form onSubmit={ handleSubmit(this._onInviteUser.bind(this)) }>
          <Field name="userId" label="Invitee Id" type="text" component={ renderInputField }/>
          { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
        </Form>
      </Modal.Content>,
      <Modal.Actions>
        <Button type="submit" content="Invite" form="invitation-form"/>
      </Modal.Actions>
    ]);
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
      inviteUser: (inviteeId, successCallback, errorCallback) => dispatch(inviteUser(getUserId(), inviteeId, ownProps.groupId, successCallback, errorCallback))
  };
};

export default connect(state => ({
  inputErrors: getFormSyncErrors('NewExpense')(state)
}), { inviteUser }, mapDispatchToProps)(reduxForm({
  form:'NewInvitation'
})(NewInvitation));
