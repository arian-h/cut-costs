import axios from 'axios';
import history from '../history';
import { groupDeleted, groupsFetchSucceeded, groupsFetchErrored, groupCreateSucceeded, groupFetchSucceeded, groupFetchErrored } from './creators';
import { login, logout } from '../helpers/auth_utils'

export const COMPONENTS_NAVBAR_NAVIGATE = 'components_navbar_navigate';
export const REGISTER_USER = 'register_user';

const ROOT_URL = "http://localhost:8443/api";
const AUTH_ENDPOINT_URL = `${ROOT_URL}/auth`;
const LOGIN_ENDPOINT = `${AUTH_ENDPOINT_URL}/login`;
const REGISTER_ENDPOINT = `${AUTH_ENDPOINT_URL}/signup`;
const GROUP_ENDPOINT = `${ROOT_URL}/group/`;

function getAuthorizationHeader() {
  return {
    headers: {
      'Authorization': localStorage.getItem('jwt_token'),
      'Content-Type': 'application/json'
    }
  };
}

export function loginUser(values, redirected_from, unauthorizedLoginCallback) {
  return () => {
    axios.post(LOGIN_ENDPOINT,
      {
        username: values.username,
        password: values.password
      }
    ).then(response => {
      const { headers: { authorization } } = response;
      localStorage.setItem('jwt_token', authorization);
      history.push(redirected_from);
    }).catch(({response}) => {
      if (response.status === 401) {
        unauthorizedLoginCallback();
      }
    });
  };
}

export function fetchGroups() {
  return (dispatch) => {
    axios.get(GROUP_ENDPOINT, getAuthorizationHeader())
      .then(response => {
          dispatch(groupsFetchSucceeded(response.data));
      })
      .catch(({response}) => {
        if (response.status ===  401) { // Unauthorized
          logout();
        } else {
          dispatch(groupsFetchErrored({
            error: response.data.message
          }));
        }
      })
  };
}

export function fetchGroup(id) {
  return (dispatch) => {
    axios.get(`${GROUP_ENDPOINT}${id}`, getAuthorizationHeader())
      .then(response => {
        dispatch(groupFetchSucceeded(response.data))
      })
      .catch(({response}) => {
        if (response.status ===  401) { // Unauthorized
          logout();
        } else {
          dispatch(groupFetchErrored({
            error: response.data.message,
            id: response.data.id
          }));
        }
      })
  };
}

export function updateGroup(values, callback) {
  return (dispatch) => {
    console.log(values);
    // axios.put(`${GROUP_ENDPOINT}values.id`,
    //   {
    //
    //   }, AUTHORIZATION_HEADER)
    //   .then(response => {
    //     if (response.status === 200) {
    //       dispatch(groupsFetched(response));
    //     } else {
    //       dispatch(groupsFetchErrored());
    //     }
    //   })
  };
}

export function createGroup(values, successCallback, errorCallback) {
  return (dispatch) => {
    axios.post(GROUP_ENDPOINT,
      {
        name: values.name,
        description: values.description
      }, getAuthorizationHeader())
      .then(({data}) => {
        successCallback();
        dispatch(groupCreateSucceeded(data))
      })
      .catch(({response: {data}}) => { //TODO manually test it
        if (response.status ===  401) { // Unauthorized
          logout();
        } else {
          errorCallback(data.message);
        }
      });
    };
}

export function deleteGroup(groupID) {
  return dispatch => {
    let deleteEndpoint = `${GROUP_ENDPOINT}${groupID}`;
    axios.delete(deleteEndpoint, getAuthorizationHeader())
      .then(response => {
        dispatch(groupDeleted(response));
      }).catch((response) => { //TODO manually test it
        if (response.status ===  401) { // Unauthorized
          logout();
        }
      });
  };
}

export function logoutUser() {
    return () => {
      logout();
    }
}

export function registerUser(values, signupFailedCallback) {
  return () => {
    axios.post(REGISTER_ENDPOINT,
      {
        name: values.name,
        username: values.username,
        password: values.password
      }
    ).then(({status, headers}) => {
      const { authorization } = headers;
      localStorage.setItem('jwt_token', authorization);
      history.push('/');
    }).catch(response => {
      debugger;
    });
  }
}
