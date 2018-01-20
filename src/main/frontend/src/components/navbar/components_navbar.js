import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import _ from 'lodash';

import { RouteList } from '../../routing/routes_list';
import ComponentsList from './components_list';

class ComponentsNavBar extends Component {

  render() {
    let buttons = [];
    _.forOwn(ComponentsList, (value, key) => {
      buttons.push(
        <button
          className="btn pull-xs-right"
          onClick={() => this.props.history.push(RouteList[key].path)}
          key={key}
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

export default withRouter(ComponentsNavBar);
