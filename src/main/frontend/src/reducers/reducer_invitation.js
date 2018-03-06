import _ from 'lodash';
import { REJECT_INVITATION } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case REJECT_INVITATION:
      return _.omit(state, action.invitationId);
    default:
      return state;
  }
}
