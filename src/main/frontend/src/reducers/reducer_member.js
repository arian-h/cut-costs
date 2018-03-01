import _ from 'lodash';
import { FETCH_MEMBERS, REMOVE_MEMBER } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_MEMBERS:
      return { ...state, [action.groupId]: action.payload};
    case REMOVE_MEMBER:
      return _.omit(state, action.payload.data);
    default:
      return state;
  }
}
