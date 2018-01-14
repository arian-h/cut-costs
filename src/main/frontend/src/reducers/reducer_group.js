import { FETCH_GROUPS} from '../actions';
import _ from 'lodash';

const INITIAL_STATE = {};

export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      //return { ...state, [action.payload.data.id]: action.payload.data};
      return _.mapKeys(action.payload.data, 'id');
    default:
      return state;
  }
}
