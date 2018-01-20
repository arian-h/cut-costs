import { FETCH_GROUPS, CREATE_GROUP, DELETE_GROUP, FETCH_GROUPS_ERROR } from '../actions/creators';
import _ from 'lodash';

const INITIAL_STATE = {};

export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      return _.mapKeys(action.payload.data, 'id');
    case FETCH_GROUPS_ERROR:
      return null;
    case CREATE_GROUP:
      return { ...state, [action.payload.data.id]: action.payload.data};
    case DELETE_GROUP:
      return _.omit(state, action.payload.data);
    default:
      return state;
  }
}
