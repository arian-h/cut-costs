import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import LoginForm from './login_form';
import Homepage from './home_page';
import Dashboard from './dashboard';
import FindPeople from './find_people';
import NavBar from './nav_bar';

function renderComponent(pathname, props) {
  let Component;
  let _pathname = pathname.substring(1).toLowerCase();
  switch(_pathname) {
    case 'dashboard':
      Component = Dashboard;
      break;
    case 'findpeople':
      Component = FindPeople;
      break;
    default:
      Component = Homepage;
  }
  return (
    <div>
      <NavBar/>
      <Component {...props} />
    </div>
  );
}

export const PrivateRoute = ({ ...rest }) => (
    <Route {...rest} render={(props) => {
      debugger;
        if (localStorage.getItem('jwt_token')) {
          return renderComponent(location.pathname, props);
        } else {
          return <Redirect to={{ pathname: '/login', state: { from: props.location } }} />;
        }
      }
    } />
)

export default PrivateRoute;
