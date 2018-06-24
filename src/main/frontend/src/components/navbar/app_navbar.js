import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Button, Icon, Menu } from 'semantic-ui-react'

import { logoutUser } from '../../actions';

class AppNavBar extends Component {

  handleLogout() {
    this.props.logoutUser();
  }

  render() {
    return (
      <Menu borderless>
        <Menu.Item as={ Link } to='/profile' name='profile'>
          <Icon name='id card' />
          Profile
        </Menu.Item>
        <Menu.Item as={ Link } to='/invitation' name='invitation'>
          <Icon name='envelope' />
          Invitation
        </Menu.Item>
        <Menu.Item>
          <Button onClick={ this.handleLogout.bind(this) }>
            <Icon name='sign out'/>Log out
          </Button>
        </Menu.Item>
      </Menu>
    );
  }
}

export default AppNavBar;
