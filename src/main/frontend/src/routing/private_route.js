import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import RouteList from './routes_list';
import {isAuthenticated} from '../helpers/auth_utils';

function renderComponent(pathname, props) {
  let _pathname = pathname.substring(1).toLowerCase();
  if (!RouteList[_pathname]) {
    _pathname = 'home';
  }
  let Component = RouteList[_pathname].component;
  return (
    <div>
      <AppNavBar isLoggedOn={isAuthenticated()}/>
      <ComponentsNavBar isLoggedOn={isAuthenticated()}/>
      <Component {...props} />
    </div>
  );
}

export const PrivateRoute = ({ ...rest }) => (
    <Route {...rest} render={(props) => {
        if (isAuthenticated()) {
          return renderComponent(location.pathname, props);
        } else if (location.pathname === '/register') {
          return renderComponent('/register', props);
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />
)

export default PrivateRoute;
