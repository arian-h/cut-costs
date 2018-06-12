import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import groupReducer from './reducer_group';
import expenseReducer from './reducer_expense';
import invitationReducer from './reducer_invitation';
import userReducer from './reducer_user';

const rootReducer = combineReducers({
  form: formReducer,
  groups: groupReducer,
  expenses: expenseReducer,
  invitations: invitationReducer,
  users: userReducer
});

export default rootReducer;
