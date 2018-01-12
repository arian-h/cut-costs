import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import AppNavBar from '../components/navbar/app_navbar';
import ComponentsNavBar from '../components/navbar/components_navbar';
import RouteList from './routes_list';

function renderComponent(pathname, props) {
  let _pathname = pathname.substring(1).toLowerCase();
  if (!RouteList[_pathname]) {
    _pathname = 'home';
  }
  let Component = RouteList[_pathname].component;
  return (
    <div>
      <AppNavBar/>
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
