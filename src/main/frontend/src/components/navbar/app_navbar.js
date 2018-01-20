import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { logoutUser } from '../../actions';

class AppNavBar extends Component {

  onLogoutClick() {

    this.props.logoutUser(() => this.props.history.push('/login'));
  }

  render() {
    return (
      <div>
        <button
          className="btn btn-danger pull-xs-right"
          onClick={this.onLogoutClick.bind(this)}
        >
          Logout
        </button>
      </div>
    );
  }
}

export default withRouter(connect(null, { logoutUser })(AppNavBar));
