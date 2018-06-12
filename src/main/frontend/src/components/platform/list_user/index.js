import React, { Component } from 'react';
import { Button, Label, Icon, Modal } from 'semantic-ui-react';

import ItemList from '../list_item';
import SearchBar from '../search_bar';

class UserList extends Component {
  constructor(props) {
    super(props);
  }

  _resultRenderer = user => {
    //TODO use user.photoUrl for the avatar
    return <Label as='a' float='left'>
        <img src='https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/220px-User_icon_2.svg.png' />
        { user.name }
        <Button icon onClick={ ()=> this.props.action(user._id) }>
          <Icon name='plus square outline' />
        </Button>
    </Label>;
  }

  render() {
    const {
      users,
      header,
      searchUsers,
      removeEnabled,
      onRemove,
      itemRenderer
    } = this.props;

    return ([
        <Modal.Header>
          { header }
        </Modal.Header>,
        <Modal.Content>
          <SearchBar
            searchHandler={ searchUsers }
            resultRenderer={ this._resultRenderer }
          />
          <ItemList
            itemRenderer={ itemRenderer }
            onRemove={ onRemove }
            removeEnabled= { removeEnabled }
            items={ users }
          />
        </Modal.Content>
    ]);
  }
}

export default UserList;
