import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import _ from 'lodash';

import ComponentsList from './components_list';

class ComponentsNavBar extends Component {

  render() {
    return (
      <div>
        {_.map(ComponentsList, navItem =>
            <Link to={navItem.path} key={navItem.path}>
              <button
                className="btn pull-xs-right"
              >
                {navItem.title}
              </button>
            </Link>
        )}
      </div>
    );
  }
}

export default ComponentsNavBar;
