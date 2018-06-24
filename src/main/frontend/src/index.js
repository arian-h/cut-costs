import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { Router, Route, Switch, Redirect } from 'react-router-dom';
import thunk from 'redux-thunk';
import rootReducer from './reducers';
import Container from 'semantic-ui-react';

import history from './history';
import PrivateRoute from './routing/private_route';
import { isAuthenticated } from './helpers/auth_utils';

import GroupList from './components/group/list_group';
import ExpenseList from './components/expense/list_expense';
import Home from './components/home';
import RegisterForm from './components/auth/form_register';
import LoginForm from './components/auth/form_login';
import ShowGroup from './components/group/show_group';
import ShowExpense from './components/expense/show_expense';
import InvitationList from './components/invitation/list_invitation';
import Profile from './components/user/profile';

const createStoreWithMiddleware = applyMiddleware(thunk)(createStore);
const store = createStoreWithMiddleware(rootReducer);

ReactDOM.render(
  <Provider store={store}>
      <Router history={history}>
          <Switch>
            <Route exact path="/login" render={props =>
                isAuthenticated() ? <Redirect to='/' /> :
                <LoginForm {...props}/>
            } />
            <Route exact path="/register" render={props =>
                isAuthenticated() ? <Redirect to='/' /> :
                <RegisterForm {...props}/>
            } />
            <PrivateRoute exact path="/group" component={GroupList}/>
            <PrivateRoute exact path="/group/:id" component={ShowGroup} />
            <PrivateRoute exact path="/expense" component={ExpenseList}/>
            <PrivateRoute exact path="/expense/:id" component={ShowExpense}/>
            <PrivateRoute exact path="/invitation" component={InvitationList}/>
            <PrivateRoute exact path="/profile" component={Profile}/>
            <PrivateRoute component={Home}/>
          </Switch>
      </Router>
  </Provider>
  , document.querySelector('.container'));
