import _ from 'lodash';
import { FETCH_GROUPS, CREATE_GROUP, DELETE_GROUP, FETCH_GROUP } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      return _.mapKeys(action.payload, 'id');
    case FETCH_GROUP:
      return {...state.data, [action.payload.id]: action.payload};
    case CREATE_GROUP:
      return { ...state.data, [action.payload.id]: action.payload};
    case DELETE_GROUP:
      return _.omit(state, action.response.data);
    default:
      return state;
  }
}
