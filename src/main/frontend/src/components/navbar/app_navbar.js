import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
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

export default withRouter(connect(null, { logoutUser })(AppNavBar));
