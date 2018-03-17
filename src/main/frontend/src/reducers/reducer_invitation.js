import _ from 'lodash';
import { REJECT_INVITATION, FETCH_INVITATIONS, ACCEPT_INVITATION } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case REJECT_INVITATION:
      return _.omit(state, action.invitationId);
    case FETCH_INVITATIONS:
      return _.mapKeys(action.invitations, 'id');
    case ACCEPT_INVITATION:
      return _.omit(state, action.invitationId);
    default:
      return state;
  }
}
