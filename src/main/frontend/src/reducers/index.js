import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import groupReducer from './reducer_group';

const rootReducer = combineReducers({
  form: formReducer,
  groups: groupReducer
});

export default rootReducer;
