import { FETCH_GROUPS_SUCCESS, FETCH_GROUPS_ERROR, CREATE_GROUP_SUCCESS, DELETE_GROUP, FETCH_GROUP_SUCCESS, FETCH_GROUP_ERROR } from '../actions/creators';
export const FULL_GROUP = 'full_group';
export const SNIPPET_GROUP = 'snippet_group';

import _ from 'lodash';

const INITIAL_STATE = {
  data: null,
  isLoading: true,
  errorFetching: null
};

export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case FETCH_GROUPS_SUCCESS:
      return {
        data: _.zipObject(_.map(action.payload, group => group.id),
          _.map(action.payload, group => ({
              data: group,
              isLoading: true,
              errorFetching: null,
              mode: SNIPPET_GROUP
          }))
        ),
        isLoading: false,
        errorFetching: null
      };
    case FETCH_GROUPS_ERROR:
      return {
        data: null,
        isLoading: false,
        errorFetching: action.payload.error
      };
    case FETCH_GROUP_SUCCESS:
      return {
        data: {...state.data, [action.payload.id]: {
          data: action.payload,
          isLoading: false,
          errorFetching: null,
          mode: FULL_GROUP
        }},
        isLoading: false,
        errorFetching: null
      };
    case FETCH_GROUP_ERROR:
      const stateWithoutId = _.omit(state.data, action.payload.id);
      return {
        data: {...stateWithoutId},
        isLoading: false,
        errorFetching: null
      };
    case CREATE_GROUP_SUCCESS:
      return {
        data: { ...state.data, [action.payload.id]: {
          data: action.payload,
          isLoading: false,
          errorFetching: null,
          mode: SNIPPET_GROUP
        }},
        isLoading: false,
        errorFetching: null
      };
    case DELETE_GROUP:
      return {
        data: _.omit(state.data, action.response.data),
        isLoading: false,
        errorFetching: null
      };
    default:
      return state;
  }
}
