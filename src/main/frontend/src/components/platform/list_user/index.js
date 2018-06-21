import React, { Component } from 'react';
import { Button, Label, Icon, Modal } from 'semantic-ui-react';

import ItemList from '../list_item';
import SearchBar from '../search_bar';

class UserList extends Component {
  constructor(props) {
    super(props);
  }

  _userSearchResultFormatter(users) {
    return users.map(user => ({
      title: user.name,
      image: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/220px-User_icon_2.svg.png'
    }));
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
            resultFormatter={ this._userSearchResultFormatter }
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
