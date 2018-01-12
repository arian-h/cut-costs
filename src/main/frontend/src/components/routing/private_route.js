import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import NavBar from '../navbar/app_navbar';
import ComponentsNavBar from '../navbar/components_navbar';
import NavBarComponentsList from './navbar_components_list';

function renderComponent(pathname, props) {
  let _pathname = pathname.substring(1).toLowerCase();
  if (!NavBarComponentsList[_pathname]) {
    _pathname = 'home';
  }
  let Component = NavBarComponentsList[_pathname].component;

  return (
    <div>
      <NavBar/>
      <ComponentsNavBar/>
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
