import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import promise from 'redux-promise';

import reducers from './reducers';

import LoginForm from './components/login_form';
import PrivateRoute from './routing/private_route';

const createStoreWithMiddleware = applyMiddleware(promise)(createStore);

ReactDOM.render(
  <Provider store={createStoreWithMiddleware(reducers)}>
      <BrowserRouter>
        <div>
          <Switch>
            <Route path="/login" render={(props) => {
                if (localStorage.getItem('jwt_token')) {
                  return <Redirect to='/' />;
                } else {
                  return <LoginForm {...props}/>
                }
              }
            } />
            <PrivateRoute path="/"/>
          </Switch>
        </div>
      </BrowserRouter>
  </Provider>
  , document.querySelector('.container'));


//  <Route exact path="/login" component={LoginForm} />
