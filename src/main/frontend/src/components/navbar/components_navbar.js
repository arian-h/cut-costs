import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { componentsNavbarNavigate } from '../../actions';
import componentsList from '../routing/navbar_components_list';

class ComponentsNavBar extends Component {

  onNavigate() {
    this.props.leftNavBarNavigate();
    this.props.history.push(arguments[0]);
  }

  render() {
    let buttons = [];
    Object.entries(componentsList).forEach(([key, value]) => {
      buttons.push(
        <button
          className="btn pull-xs-right"
          onClick={this.onNavigate.bind(this, value.path)}
          key={buttons.length}
        >
          {value.navbarTitle}
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

export default withRouter(connect(null, {componentsNavbarNavigate})(ComponentsNavBar));
