import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { componentsNavbarNavigate } from '../../actions';
import { RouteList } from '../../routing/routes_list';
import ComponentsList from './components_list';

class ComponentsNavBar extends Component {

  onNavigate() {
    this.props.componentsNavbarNavigate();
    this.props.history.push(arguments[0]);
  }

  render() {
    let buttons = [];
    Object.entries(ComponentsList).forEach(([key, value]) => {
      buttons.push(
        <button
          className="btn pull-xs-right"
          onClick={this.onNavigate.bind(this, RouteList[key].path)}
          key={buttons.length}
        >
          {value.title}
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
