import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import _ from 'lodash';

import { isAuthenticated } from '../helpers/auth_utils';
import ViewContainer from './view_container';

const PrivateRoute = ({component, ...rest }) => {
  return (<Route {...rest} render={ props => {
      if (isAuthenticated()) {
        return <ViewContainer component={ component } {...props}/>;
      } else {
        return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
      }
    }
  } />);
}

export default PrivateRoute;
