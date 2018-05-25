import React, { Component } from 'react';
import { connect } from 'react-redux';
import { logoutUser } from '../../actions';
import { Menu, Icon } from 'semantic-ui-react'

class AppNavBar extends Component {

  handleLogout() {
    this.props.logoutUser();
  }

  render() {
    return (
      <Menu.Item name='logout' onClick={ this.handleLogout.bind(this) }>
        <Icon name='sign out'/>Log out
      </Menu.Item>
    );
  }
}

export default connect(null, { logoutUser })(AppNavBar);
