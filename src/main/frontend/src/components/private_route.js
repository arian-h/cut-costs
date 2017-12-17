import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import LoginForm from './login_form';
import Homepage from './home_page';

export const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={props =>
        localStorage.getItem('jwt_token')
            ? <Component {...props} />
            : <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
    } />
)

export default PrivateRoute;
