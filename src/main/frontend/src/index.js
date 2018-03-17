import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { Router, Route, Switch, Redirect } from 'react-router-dom';
import thunk from 'redux-thunk';
import rootReducer from './reducers';

import history from './history';
import PrivateRoute from './routing/private_route';
import { isAuthenticated } from './helpers/auth_utils';

import GroupList from './components/group/list_group';
import ExpenseList from './components/expense/list_expense';
import Home from './components/home';
import RegisterForm from './components/auth/form_register';
import LoginForm from './components/auth/form_login';
import NewGroup from './components/group/new_group';
import ShowGroup from './components/group/show_group';
import ShowExpense from './components/expense/show_expense';
import InvitationList from './components/invitation/list_invitation';
import ShowUser from './components/user/show_user';

const createStoreWithMiddleware = applyMiddleware(thunk)(createStore);
export const store = createStoreWithMiddleware(rootReducer); // TODO do we need to export ?

ReactDOM.render(
  <Provider store={store}>
      <Router history={history}>
        <div>
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
            <PrivateRoute exact path="/group/new" component={GroupList} componentProps={{
              modal:{
                'content': NewGroup,
                'className': 'new-group-modal'
              }}}
            />
            <PrivateRoute exact path="/group/:id" component={ShowGroup} />
            <PrivateRoute exact path="/expense" component={ExpenseList}/>
            <PrivateRoute exact path="/expense/:id" component={ShowExpense}/>
            <PrivateRoute exact path="/invitation" component={InvitationList}/>
            <PrivateRoute exact path="/user/:id?" component={ShowUser}/>
            <PrivateRoute component={Home}/>
          </Switch>
        </div>
      </Router>
  </Provider>
  , document.querySelector('.container'));
