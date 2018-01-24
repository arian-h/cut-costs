import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import _ from 'lodash';

import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import {RouteList, getNavPath} from './routes_list';
import {isAuthenticated} from '../helpers/auth_utils';

//renders components with authenticated access
function renderComponent(pathname, props) {
  let _pathName = pathname.substring(1).toLowerCase();
  let route = getNavPath(_pathName);
  let Component = RouteList[route].component;
  let customProps = RouteList[route].props;
  return (
    <div>
      <AppNavBar />
      <ComponentsNavBar />
      <Component {...props} {...customProps}/>
    </div>
  );
}

export const PrivateRoute = ({ ...rest }) => (
    <Route {...rest} render={(props) => {
        if (isAuthenticated()) {
          return renderComponent(location.pathname, props);
        } else if (location.pathname === '/register') {
          let RegisterForm = RouteList['register'].component;
          return <RegisterForm {...props}/>
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />
)

export default PrivateRoute;
