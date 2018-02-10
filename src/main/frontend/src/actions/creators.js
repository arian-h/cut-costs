export const FETCH_GROUPS_SUCCESS = 'fetch_groups_success';
export const DELETE_GROUP = 'delete_group';
export const FETCH_GROUPS_ERROR = 'fetch_groups_errored';
export const CREATE_GROUP_SUCCESS = 'create_group_success';
export const CREATE_GROUP_ERROR = 'create_group_error';
export const FETCH_GROUP_SUCCESS = 'fetch_group_success';

export function groupDeleted(response) {
  return {
     type: DELETE_GROUP,
     response
  }
}

export function groupsFetchSucceeded(response) {
  return {
     type: FETCH_GROUPS_SUCCESS,
     payload: response
  }
}

export function groupsFetchErrored(errorWithId) {
  return {
     type: FETCH_GROUPS_ERROR,
     payload: errorWithId
  }
}

export function groupCreateSucceeded(group) {
  return {
    type: CREATE_GROUP_SUCCESS,
    payload: group
  }
}

export function groupCreateErrored(errorWithId) {
  return {
    type: CREATE_GROUP_ERROR,
    payload: response
  }
}

export function groupFetchSucceeded(groupData) {
  return {
     type: FETCH_GROUP_SUCCESS,
     payload: groupData
  }
}
