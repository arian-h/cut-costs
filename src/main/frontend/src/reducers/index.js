import { combineReducers } from 'redux';
import {reducer as formReducer } from 'redux-form';
import authReducer from './reducer_auth';
import groupReducer from './reducer_group';

const rootReducer = combineReducers({
  form: formReducer,
  auth: authReducer,
  groups: groupReducer
});

export default rootReducer;
