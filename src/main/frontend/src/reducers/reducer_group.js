import { FETCH_GROUPS, CREATE_GROUP, DELETE_GROUP } from '../actions/creators';
import _ from 'lodash';

const INITIAL_STATE = {};

export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      //return { ...state, [action.payload.data.id]: action.payload.data};
      return _.mapKeys(action.payload.data, 'id');
    case CREATE_GROUP:
      return { ...state, [action.payload.data.id]: action.payload.data};
    case DELETE_GROUP:
    debugger;
      //fix the delete endpoint to return the data in a json object
      //use redux thunk ! all these reducers are working incorrectly
      return _.omit(state, action.payload.data);
    default:
      return state;
  }
}
