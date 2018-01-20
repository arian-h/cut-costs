import axios from 'axios';
import { groupDeleted, groupsFetched } from './creators';

export const LOGIN_USER = 'login_user';
export const LOGOUT_USER = 'logout_user';
export const COMPONENTS_NAVBAR_NAVIGATE = 'components_navbar_navigate';
export const REGISTER_USER = 'register_user';
export const CREATE_GROUP = 'create_group';

const ROOT_URL = "http://localhost:8443/api";
const AUTH_ENDPOINT_URL = `${ROOT_URL}/auth`;
const LOGIN_ENDPOINT = `${AUTH_ENDPOINT_URL}/login`;
const REGISTER_ENDPOINT = `${AUTH_ENDPOINT_URL}/signup`;
const GROUP_ENDPOINT = `${ROOT_URL}/group/`;

export function loginUser(values, callback) {
  const request = axios.post(LOGIN_ENDPOINT,
    {
      username: values.username,
      password: values.password
    }
  ).then((response) => callback(response));

  return {
    type: LOGIN_USER,
    payload: request
  };
}

export function logoutUser() {
    return {
      type: LOGOUT_USER
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
    axios.get(GROUP_ENDPOINT,
      {
        headers: {
          'Authorization': localStorage.getItem('jwt_token'),
          'Content-Type': 'application/json'
        }
      }
    ).then(response => {
      debugger;
      //TODO evaluate response
      dispatch(groupsFetched(response));
    })
  };
}

export function createGroup(values, callback) {
  const request = axios.post(GROUP_ENDPOINT,
    {
      name: values.name,
      description: values.description
    },
    {
      headers: {
        'Authorization': localStorage.getItem('jwt_token'),
        'Content-Type': 'application/json'
      }
    }
  ).then((response) => callback(response));
  return {
    type: NEW_GROUP,
    payload: request
  }
}

export function deleteGroup(groupID, callback) {
  return (dispatch) => {
    let deleteEndpoint = `${GROUP_ENDPOINT}${groupID}`;
    debugger;
    axios.delete(deleteEndpoint, {
      headers: {
        'Authorization': localStorage.getItem('jwt_token'),
        'Content-Type': 'application/json'
      }
    }).then((response) => {
      dispatch(groupDeleted(response));
    });
  };
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
