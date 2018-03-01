import _ from 'lodash';
import { FETCH_EXPENSE, DELETE_EXPENSE } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_EXPENSE:
      return state;
    case DELETE_EXPENSE:
      return state;
    default:
      return state;
  }
}
