import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import _ from 'lodash';
import { Container, Sidebar, Segment, Menu } from 'semantic-ui-react'

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import { isAuthenticated } from '../helpers/auth_utils';

export const PrivateRoute = ({component: Component, componentProps, ...rest }) => {
    return (<Route {...rest} render={(props) => {
        if (isAuthenticated()) {
          return (
            <Container fluid={ true }>
              <Sidebar.Pushable as={ Segment }>
                <Sidebar as={ Menu } animation='push' width='thin' visible={true} icon='labeled' vertical inverted>
                  <ComponentsNavBar />
                  <AppNavBar />
                </Sidebar>
                <Sidebar.Pusher>
                  <Segment basic>
                    <Component {...props} {...componentProps}/>
                  </Segment>
                </Sidebar.Pusher>
              </Sidebar.Pushable>
            </Container>
          )
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />);
}

export default PrivateRoute;
