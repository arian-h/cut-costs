import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import thunk from 'redux-thunk';
import rootReducer from './reducers';
import PrivateRoute from './routing/private_route';
import {RouteList} from './routing/routes_list';

const createStoreWithMiddleware = applyMiddleware(thunk)(createStore);
export const store = createStoreWithMiddleware(rootReducer);

ReactDOM.render(
  <Provider store={store}>
      <BrowserRouter>
        <div>
          <Switch>
            <Route path="/login" render={(props) => {
                if (localStorage.getItem('jwt_token')) {
                  return <Redirect to='/' />;
                } else {
                  let LoginForm = RouteList['login'].component;
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
