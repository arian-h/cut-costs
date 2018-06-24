import React, { Component } from 'react';
import { Container, Grid, Menu } from 'semantic-ui-react'

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';

class ViewContainer extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    let { component: Component, ...componentProps } = this.props;;

    return (
      <Grid>
        <Grid.Row>
          <Grid.Column floated='right' width={5}>
            <AppNavBar />
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          <Grid.Column width={3}>
            <Menu inverted pointing vertical>
              <ComponentsNavBar />
            </Menu>
          </Grid.Column>
          <Grid.Column width={13}>
            <Component {...componentProps}/>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

export default ViewContainer;
