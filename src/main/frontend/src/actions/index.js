import axios from 'axios';
import { groupDeleted, groupsFetched, groupsFetchErrored, groupCreated, groupFetched, groupFetchErrored } from './creators';

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
        if (response.status === 200) {
          dispatch(groupsFetched(response));
        } else {
          dispatch(groupsFetchErrored());
        }
      })
  };
}

export function fetchGroup(id, callback) {
  return (dispatch) => {
    axios.get(`${GROUP_ENDPOINT}${id}`, AUTHORIZATION_HEADER)
      .then(response => {
        dispatch(groupFetched(response))
      })
      .catch(({response}) => {
        dispatch(groupFetchErrored(response));
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

export function createGroup(values, callback) {
  debugger;
  return (dispatch) => {
    axios.post(GROUP_ENDPOINT,
      {
        name: values.name,
        description: values.description
      }, AUTHORIZATION_HEADER)
      .then((response) => {
        if (response.status === 200) {
          callback(response);
          dispatch(groupCreated(response));
        }
      });
    };
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
