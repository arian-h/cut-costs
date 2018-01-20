export const FETCH_GROUPS = 'fetch_groups';
export const DELETE_GROUP = 'delete_group';
export const FETCH_GROUPS_ERROR = 'fetch_groups_errored';
export const LOGIN_USER = 'login_user';
export const LOGOUT_USER = 'logout_user';

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

export function groupsFetchErrored() {
  return {
     type: FETCH_GROUPS_ERROR
  }
}

export function loggedIn(response) {
  return {
    type: LOGIN_USER,
    response: response
  };
}

export function loggedOut() {
  return {
    type: LOGOUT_USER
  };
}
