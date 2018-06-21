import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Label } from 'semantic-ui-react';

import { addSharer, searchNewContributor } from '../../actions';
import SearchBar from '../platform/search_bar';

class NewSharer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      selectedNewSharer: {}
    }
  }

  _selectItemHandler(user) {
    this.setState({ selectedNewSharer: user});
  }

  _userSearchResultFormatter(users) {
    return users.map(user => ({
      title: user.name,
      image: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/220px-User_icon_2.svg.png',
      id: user.id
    }));
  }

  _onSubmit(values) {
    this.props.addSharer(this.state.selectedNewSharer.id, () => this.props.onClose(), error => this.setState({ error: error }));
  }

  _searchNewContributor = (searchTerm, searchCallback) => {
    this.props.searchNewContributor(searchTerm, result => searchCallback(result));
  }

  render() {
    return ([
      <Modal.Header>
        Add Contributor
      </Modal.Header>,
      <Modal.Content>
        <SearchBar
          searchHandler={ this._searchNewContributor }
          resultFormatter={ this._userSearchResultFormatter }
          selectItemHandler= { this._selectItemHandler.bind(this) }
        />
        <span>{ this.state.error }</span>
      </Modal.Content>,
      <Modal.Actions>
        <Button onClick={ this._onSubmit.bind(this) } content="Add Sharer" />
      </Modal.Actions>
    ]);
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
      addSharer: (sharerId, successCallback, errorCallback) => dispatch(addSharer(sharerId, ownProps.expenseId, successCallback, errorCallback)),
      searchNewContributor: (searchTerm, successCallback) => searchNewContributor(searchTerm, ownProps.expenseId, successCallback)
  };
};

export default connect(null, mapDispatchToProps)(NewSharer);
