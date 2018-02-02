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
      return { ...state, [action.response.data.id]: action.response.data}; // TODO maybe we should just return response instead of response.data as it could be an error
    case DELETE_GROUP:
      return _.omit(state, action.response.data); // TODO change DELET GROUP endpoint to return id inside a body
    case FETCH_GROUPS_ERROR:
        return {};
    case FETCH_GROUP_ERROR:
      const stateWithoutId = _.omit(state, action.response.data.id);
      return {
        ...stateWithoutId,
        errorFetching: action.response.error,
        isLoading: false
      }
    case FETCH_GROUP_STARTED:
      return { ...state, isLoading: true };
    default:
      return state;
  }
}
