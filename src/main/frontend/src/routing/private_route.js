import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import _ from 'lodash';
import { Container, Sidebar, Segment, Menu, Grid } from 'semantic-ui-react'

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import { isAuthenticated } from '../helpers/auth_utils';

export const PrivateRoute = ({component: Component, componentProps, ...rest }) => {
    return (<Route {...rest} render={(props) => {
        if (isAuthenticated()) {
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
                  <Component {...props} {...componentProps}/>
                </Grid.Column>
              </Grid>
            </Container>
          )
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />);
}

export default PrivateRoute;
