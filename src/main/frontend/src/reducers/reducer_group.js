import { FETCH_GROUPS, CREATE_GROUP, DELETE_GROUP, FETCH_GROUP, FETCH_GROUPS_ERROR, FETCH_GROUP_ERROR } from '../actions/creators';
import _ from 'lodash';

const INITIAL_STATE = {};

export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      return _.mapKeys(action.response.data, 'id');
    case FETCH_GROUP:
      return {...state, [action.response.data.id]: action.response};
    case CREATE_GROUP:
      return { ...state, [action.response.data.id]: action.response.data}; // maybe we should just return response instead of response.data as it could be an error
    case DELETE_GROUP:
      return _.omit(state, action.response.data);
    case FETCH_GROUPS_ERROR:
        return null; //TODO fix this
    case FETCH_GROUP_ERROR:
      return {...state, [action.response.data.id]: action.response}; //TODO convert it to error signal
    default: // no change in state if the action is unknown
      return state;
  }
}
