import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import promise from 'redux-promise';

import reducers from './reducers';

import LoginForm from './components/login_form';
import NavBar from './components/nav_bar';
import Homepage from './components/home_page';
import PrivateRoute from './components/private_route';

const createStoreWithMiddleware = applyMiddleware(promise)(createStore);

ReactDOM.render(
  <Provider store={createStoreWithMiddleware(reducers)}>
      <BrowserRouter>
        <div>
          <Switch>
            <PrivateRoute exact path="/salam" component={Homepage} />
            <Route path="/login" component={LoginForm} />
          </Switch>
        </div>
      </BrowserRouter>
  </Provider>
  , document.querySelector('.container'));


//  <Route exact path="/login" component={LoginForm} />
