import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import groupReducer from './reducer_group';
import memberReducer from './reducer_member';

const rootReducer = combineReducers({
  form: formReducer,
  groups: groupReducer,
  members: memberReducer
});

export default rootReducer;
