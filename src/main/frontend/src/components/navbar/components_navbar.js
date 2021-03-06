import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Menu, Icon } from 'semantic-ui-react';

import ComponentsList from './components_list';

class ComponentsNavBar extends Component {
  render() {
    const { activeItem } = this.props;
    return (
      _.map(ComponentsList, navItem =>
          <Menu.Item as={ Link } to={ navItem.path } active={ activeItem === navItem.name } name={ navItem.name }>
            <Icon name={ navItem.icon } />
            { navItem.title }
          </Menu.Item>
        )
    );
  }
}

export default ComponentsNavBar;
