import axios from 'axios';
import { groupDeleted, groupsFetched, groupsFetchErrored, loggedIn, loggedOut } from './creators';

//TODO: TRANSFORM ALL THESE ACTIONS FROM PROMISE TO THUNK

export const COMPONENTS_NAVBAR_NAVIGATE = 'components_navbar_navigate';
export const REGISTER_USER = 'register_user';
export const CREATE_GROUP = 'create_group';

const ROOT_URL = "http://localhost:8443/api";
const AUTH_ENDPOINT_URL = `${ROOT_URL}/auth`;
const LOGIN_ENDPOINT = `${AUTH_ENDPOINT_URL}/login`;
const REGISTER_ENDPOINT = `${AUTH_ENDPOINT_URL}/signup`;
const GROUP_ENDPOINT = `${ROOT_URL}/group/`;

const AUTHORIZATION_HEADER = {
  headers: {
    'Authorization': localStorage.getItem('jwt_token'),
    'Content-Type': 'application/json'
  }
};

export function loginUser(values, callback) {
  return (dispatch) => {
    axios.post(LOGIN_ENDPOINT,
      {
        username: values.username,
        password: values.password
      }
    ).then((response) => {
      callback(response);
      dispatch(loggedIn(response));
    });
  };
}

//TODO: not useful yet
export function componentsNavbarNavigate() {
  return {
    type: COMPONENTS_NAVBAR_NAVIGATE
  }
}

export function fetchGroups() {
  return (dispatch) => {
    axios.get(GROUP_ENDPOINT, AUTHORIZATION_HEADER)
      .then(response => {
        if (response.status === 200) {
          dispatch(groupsFetched(response));
        } else {
          dispatch(groupsFetchErrored());
        }
      })
  };
}

export function createGroup(values, callback) {
  const request = axios.post(GROUP_ENDPOINT,
    {
      name: values.name,
      description: values.description
    }, AUTHORIZATION_HEADER
  ).then((response) => callback(response));
  return {
    type: NEW_GROUP,
    payload: request
  }
}

export function deleteGroup(groupID, callback) {
  return (dispatch) => {
    let deleteEndpoint = `${GROUP_ENDPOINT}${groupID}`;
    axios.delete(deleteEndpoint, AUTHORIZATION_HEADER)
      .then((response) => {
        if (response.status === 200) {
          dispatch(groupDeleted(response));
        } // else do nothing or show an error
      });
  };
}

export function logoutUser(callback) {
    return (dispatch) => {
      callback();
      localStorage.removeItem('jwt_token');
      dispatch(loggedOut());
    }
}

export function registerUser(values, callback) {
  const request = axios.post(REGISTER_ENDPOINT,
    {
      name: values.name,
      username: values.email,
      password: values.password
    }
  ).then((response) => callback(response));

  return {
    type: REGISTER_USER,
    payload: request
  };
}
