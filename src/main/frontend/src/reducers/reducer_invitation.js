import _ from 'lodash';
import { DELETE_INVITATION, FETCH_INVITATIONS } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case DELETE_INVITATION:
      return _.omit(state, action.invitationId);
    case FETCH_INVITATIONS:
      return _.mapKeys(action.invitations, 'id');
    default:
      return state;
  }
}
