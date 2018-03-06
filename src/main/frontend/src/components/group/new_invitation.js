import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';

import { inviteUser } from '../../actions';
import { getUserId } from '../../helpers/user_utils';

class NewInvitation extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      inputValue: ''
    }
  }

  _onInviteUser() {
    this.props.inviteUser(getUserId(), this.state.inputValue, this.props.groupId, () => this.props.onClose(), error => this.setState({error: error}));
  }

  _updateInputValue(evt) {
    this.setState({inputValue: evt.target.value});
  }

  render() {
    return (
      <div>
          <input type="text" value={this.state.userId} onChange={evt => this._updateInputValue(evt)} />
          <button type="submit" className="btn btn-primary" onClick={() => this._onInviteUser()}>Create</button>
          <button type="button" className="btn btn-primary" onClick={() => this.props.onClose()}>Cancel</button>
        { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
      </div>
    );
  }
}

const mapDispatchToProps = (dispatch) => {
    return {
        inviteUser: (inviterId, inviteeId, groupId, successCallback, errorCallback) => dispatch(inviteUser(inviterId, inviteeId, groupId, successCallback, errorCallback))
    };
};

export default connect(null, mapDispatchToProps)(NewInvitation);
