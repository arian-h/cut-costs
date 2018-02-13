import axios from 'axios';
import history from '../history';
import { groupDeleted, groupsFetchSucceeded, groupsFetchErrored, groupCreateSucceeded, groupFetchSucceeded, groupFetchErrored } from './creators';
import { logout } from '../helpers/auth_utils'

export const COMPONENTS_NAVBAR_NAVIGATE = 'components_navbar_navigate';
export const REGISTER_USER = 'register_user';

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
  return () => {
    axios.post(LOGIN_ENDPOINT,
      {
        username: values.username,
        password: values.password
      }
    ).then((response) => {
      callback(response);
    });
  };
}

export function fetchGroups() {
  return (dispatch) => {
    axios.get(GROUP_ENDPOINT, AUTHORIZATION_HEADER)
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
    axios.get(`${GROUP_ENDPOINT}${id}`, AUTHORIZATION_HEADER)
      .then(response => {
        dispatch(groupFetchSucceeded(response.data))
      })
      .catch(({response}) => {
        dispatch(groupFetchErrored({
          error: response.data.message,
          id: response.data.id
        }));
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
      }, AUTHORIZATION_HEADER)
      .then(({data}) => {
        successCallback();
        dispatch(groupCreateSucceeded(data))
      })
      .catch(({response: {data}}) => {
        errorCallback(data.message);
      });
    };
}

export function deleteGroup(groupID) {
  return dispatch => {
    let deleteEndpoint = `${GROUP_ENDPOINT}${groupID}`;
    axios.delete(deleteEndpoint, AUTHORIZATION_HEADER)
      .then(response => {
        debugger;
        dispatch(groupDeleted(response));
      });
  };
}

export function logoutUser(callback) {
    return () => {
      callback();
    }
}

export function registerUser(values, callback) {
  return () => {
    axios.post(REGISTER_ENDPOINT,
      {
        name: values.name,
        username: values.username,
        password: values.password
      }
    ).then((response) => callback(response));
  }
}
