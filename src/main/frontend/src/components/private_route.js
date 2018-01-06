import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import NavBar from './nav_bar';
import LeftNavBar from './left_nav_bar';
import NavBarComponents from './nav_bar_components';

function renderComponent(pathname, props) {
  let _pathname = pathname.substring(1).toLowerCase();
  if (!NavBarComponents[_pathname]) {
    _pathname = 'home';
  }
  let Component = NavBarComponents[_pathname].component;

  return (
    <div>
      <NavBar/>
      <LeftNavBar/>
      <Component {...props} />
    </div>
  );
}

export const PrivateRoute = ({ ...rest }) => (
    <Route {...rest} render={(props) => {
        if (localStorage.getItem('jwt_token')) {
          return renderComponent(location.pathname, props);
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />
)

export default PrivateRoute;
