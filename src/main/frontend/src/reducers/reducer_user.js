import _ from 'lodash';
import { FETCH_USER } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_USER:
      return {...state, [action.user.id]: action.user};
    default:
      return state;
  }
}
