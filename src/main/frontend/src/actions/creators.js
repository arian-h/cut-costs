export const FETCH_GROUPS = 'fetch_groups';
export const DELETE_GROUP = 'delete_group';
export const FETCH_GROUPS_ERROR = 'fetch_groups_errored';
export const CREATE_GROUP = 'create_group';
export const FETCH_GROUP = 'fetch_group';
export const FETCH_GROUP_ERROR = 'fetch_group_errored';

export function groupDeleted(response) {
  return {
     type: DELETE_GROUP,
     response
  }
}

export function groupsFetched(response) {
  return {
     type: FETCH_GROUPS,
     response
  }
}

export function groupsFetchErrored() {
  return {
     type: FETCH_GROUPS_ERROR
  }
}

export function groupCreated(response) {
  return {
    type: CREATE_GROUP,
    response
  }
}

export function groupFetched(response) {
  return {
     type: FETCH_GROUP,
     response
  }
}

export function groupFetchErrored(response) {
  //handle the error here
  return {
     type: FETCH_GROUP_ERROR,
     response
  }
}

export function fetchGroupStarted(response) {
  return {
    type: FETCH_GROUP_STARTED
  }
}
