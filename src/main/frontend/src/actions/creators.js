export const FETCH_GROUPS = 'fetch_groups';
export const DELETE_GROUP = 'delete_group';

export function groupDeleted(response) {
  return {
     type: DELETE_GROUP,
     payload: response
  }
}

export function groupsFetched(response) {
  return {
     type: FETCH_GROUPS,
     payload: response
  }
}
