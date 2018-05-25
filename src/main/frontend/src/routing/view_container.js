import React, { Component } from 'react';
import { Modal, Container, Grid, Menu } from 'semantic-ui-react'

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';

class ViewContainer extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    let { component: Component, ...componentProps } = this.props;;

    return (
      <Container fluid={ true }>
        <Grid>
          <Grid.Column stretched width={ 2 }>
            <Menu inverted pointing vertical>
              <ComponentsNavBar />
              <AppNavBar />
            </Menu>
          </Grid.Column>
          <Grid.Column stretched width={ 14 }>
            <Component {...componentProps}/>
          </Grid.Column>
        </Grid>
      </Container>
    );
  }
}

export default ViewContainer;
