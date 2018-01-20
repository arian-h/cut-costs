export const FETCH_GROUPS = 'fetch_groups';
export const DELETE_GROUP = 'delete_group';
export const FETCH_GROUPS_ERROR = 'fetch_groups_errored';
export const CREATE_GROUP = 'create_group';

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
