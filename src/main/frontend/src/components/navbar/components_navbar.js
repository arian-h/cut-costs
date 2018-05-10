import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Menu, Icon } from 'semantic-ui-react';

import ComponentsList from './components_list';

class ComponentsNavBar extends Component {

  render() {
    return (
      <div>
        {_.map(ComponentsList, navItem =>
            <Menu.Item as={ Link } to={ navItem.path } name={ navItem.name  }>
              <Icon name={navItem.icon} />
              { navItem.title }
            </Menu.Item>
        )}
      </div>
    );
  }

}

export default ComponentsNavBar;
