export const FETCH_GROUPS = 'fetch_groups_success';
export const DELETE_GROUP = 'delete_group';
export const CREATE_GROUP = 'create_group_success';
export const FETCH_GROUP = 'fetch_group_success';

export function groupDeleted(response) {
  return {
     type: DELETE_GROUP,
     response
  }
}

export function groupsFetched(response) {
  return {
     type: FETCH_GROUPS,
     payload: response
  }
}

export function groupCreated(group) {
  return {
    type: CREATE_GROUP,
    payload: group
  }
}

export function groupFetched(group) {
  return {
     type: FETCH_GROUP,
     payload: group
  }
}
