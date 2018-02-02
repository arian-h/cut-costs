import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import _ from 'lodash';

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import {isAuthenticated} from '../helpers/auth_utils';

export const PrivateRoute = ({component: Component, componentProps, ...rest }) => {
    return (<Route {...rest} render={(props) => {
        if (isAuthenticated()) {
          return (
            <div>
              <AppNavBar />
              <ComponentsNavBar />
              <Component {...props} {...componentProps}/>
            </div>
          )
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />);
}

export default PrivateRoute;
