import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { leftNavBarNavigate } from '../actions';
import NavBarComponents from './nav_bar_components';

class LeftNavBar extends Component {

  onNavigate() {
    this.props.leftNavBarNavigate();
    this.props.history.push(arguments[0]);
  }

  render() {
    let buttons = [];
    Object.entries(NavBarComponents).forEach(([key, value]) => {
      buttons.push(
        <button
          className="btn pull-xs-right"
          onClick={this.onNavigate.bind(this, value.path)}
          key={value.component.getTitle()}
        >
          {value.component.getTitle()}
        </button>
      )
    });
    return (
      <div>
        {buttons}
      </div>
    );
  }
}

export default withRouter(connect(null, {leftNavBarNavigate})(LeftNavBar));
