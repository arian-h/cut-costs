import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Button, Label, Icon } from 'semantic-ui-react';

import { searchGroupMember, removeMember, inviteUser } from '../../actions';
import { getUserId } from '../../helpers/user_utils';
import { Modal } from 'semantic-ui-react';
import UserList from '../platform/list_user';
import ItemList from '../platform/list_item';

class MemberList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      removeError: null,
      showUserList: false
    };
  }

  _searchGroupMember = (searchTerm, searchCallback) => {
    this.props.searchGroupMember(searchTerm, results => searchCallback(results));
  }

  _removeEnabled = memberId => {
    return (getUserId() != memberId.toString()) && this.props.isAdmin;
  }

  _onRemove = memberId => {
    this.props.removeMember(this.props.groupId, memberId, this._removeErrorCallback);
  }

  _removeErrorCallback = error => {
    this.setState({ removeError: error });
  }

  _onInvite = inviteeId => {
    //TODO pass in successCallback
    this.props.inviteUser(inviteeId);
  }

  _showMore = () => {
    this.setState({ showUserList: true });
  }

  _closeUserList = () => {
    this.setState({ showUserList: false });
  }


  _itemRenderer = user => {
    let removeButton;
    if (this._removeEnabled(user.id)) {
      removeButton = <Button icon onClick={ ()=> this._onRemove(user.id) }>
        <Icon name='delete' />
      </Button>;
    }
    return (
      <Label image>
        <img src={ user.avatar } />
        { user.name }
        { removeButton }
      </Label>
    );
  }

  render() {
    const { error, showUserList } = this.state;
    const { groups, groupId } = this.props;
    let group = groups[groupId];

    let members = [];
    members.push(group.admin);
    members.push(...group.members);
    if (error) {
      return <div>{ props.error }</div>;
    }
    return (
      <div>
        <Modal closeIcon
          open={ showUserList }
          onClose={ this._closeUserList }
        >
          <UserList
              users={ members }
              header='Group list'
              searchUsers={ this._searchGroupMember }
              removeEnabled={ this._removeEnabled }
              onRemove={ this._onRemove }
              action={ this._onInvite }
              itemRenderer={ this._itemRenderer }
          />
        </Modal>
        <ItemList
          items={ members }
          limit={ 3 }
          onRemove={ this._onRemove }
          removeEnabled={ this._removeEnabled }
          showMore={ this._showMore }
          itemRenderer={ this._itemRenderer }
        />
      </div>
    );
  }
}

/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { groups: state.groups };
}

const mapDispatchToProps = (dispatch, ownProps) => {
  let { groupId } = ownProps;
  return {
    inviteUser: (inviteeId, successCallback, errorCallback) => dispatch(inviteUser(inviteeId, groupId, successCallback, errorCallback)),
    searchGroupMember: (searchTerm, successCallback) => searchGroupMember(searchTerm, groupId, successCallback),
    removeMember: (memberId, successCallback, errorCallback) => dispatch(removeMember(groupId, memberId, successCallback, errorCallback))
  }
}
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */

export default connect(mapStateToProps, mapDispatchToProps)(MemberList);
