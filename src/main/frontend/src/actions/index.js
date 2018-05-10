import axios from 'axios';
import history from '../history';
import { userFetched, invitationAccepted, invitationRejected, userUpdated, groupDeleted, groupsFetched, groupCreated, groupFetched, groupUpdated, membersFetched, memberRemoved, expenseUpdated, expenseCreated, expenseDeletedFromGroup, expensesFetched, expenseDeleted, expenseFetched, sharerAdded, sharerRemoved, invitationsFetched } from './creators';
import { logout } from '../helpers/auth_utils'

const ROOT_URL = "http://localhost:8443/api";
const AUTH_ENDPOINT_URL = `${ROOT_URL}/auth`;
const LOGIN_ENDPOINT = `${AUTH_ENDPOINT_URL}/login`;
const REGISTER_ENDPOINT = `${AUTH_ENDPOINT_URL}/signup`;
const GROUP_ENDPOINT = `${ROOT_URL}/group/`;
const EXPENSE_ENDPOINT = `${ROOT_URL}/expense/`;
const INVITATION_ENDPOINT = `${ROOT_URL}/invitation/`;
const USER_ENDPOINT = `${ROOT_URL}/user/`;

function getAuthorizationHeader() {
  return {
    headers: {
      'Authorization': localStorage.getItem('jwt_token'),
      'Content-Type': undefined
    }
  };
}

export function loginUser(values, redirected_from, errorCallback) {
  return () => {
    axios.post(LOGIN_ENDPOINT,
      {
        username: values.username,
        password: values.password
      }
    ).then(response => {
      const { headers: { authorization } } = response;
      localStorage.setItem('jwt_token', authorization);
      localStorage.setItem('user_id', response.data);
      history.push(redirected_from);
    }).catch(({response}) => {
      if (!response) {
        //Network error
        //show a sticky message with offline message
      } else {
        if (response.status === 401) { // Unauthorized
          errorCallback();
        }
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
    ).then(({ status, headers: { authorization } }) => {
      localStorage.setItem('jwt_token', authorization);
      history.push('/');
    }).catch(response => {
      debugger;
    });
  }
}

export function updateUser(values, errorCallback) {
  return dispatch => {
    axios.put(USER_ENDPOINT, {
      name: values.name,
      description: values.description
    }, getAuthorizationHeader())
    .then(({data}) => {
      dispatch(userUpdated(data))
    })
    .catch(({response}) => {
      if (!response) {
        //Network error
        //show a sticky message with offline message
      } else {
        if (response.status ===  401) { // Unauthorized
          logout();
        } else {
          errorCallback(response.data.message);
        }
      }
    })
  };
}

export function fetchUser(userId, successCallback, errorCallback) {
  return dispatch => {
    axios.get(`${USER_ENDPOINT}${userId}`, getAuthorizationHeader())
      .then(response => {
        dispatch(userFetched(response.data));
      })
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function inviteUser(inviterId, inviteeId, groupId, successCallback, errorCallback) {
  return () => {
    axios.post(INVITATION_ENDPOINT, {
      inviteeId: inviteeId,
      groupId: groupId,
      inviterId: inviterId
    }, getAuthorizationHeader())
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function rejectInvitation(invitationId, errorCallback) {
  return dispatch => {
    axios.post(`${INVITATION_ENDPOINT}${invitationId}/reject`, {}, getAuthorizationHeader())
      .then(response => {
        dispatch(invitationRejected(invitationId));
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function acceptInvitation(invitationId, errorCallback) {
  return dispatch => {
    axios.post(`${INVITATION_ENDPOINT}${invitationId}/accept`, {}, getAuthorizationHeader())
      .then(({data: group}) => {
        dispatch(groupCreated(group))
      })
      .then(() => {
        dispatch(invitationAccepted(invitationId)); // TODO change it remove invitation both for accept and reject
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function fetchInvitations(successCallback, errorCallback) {
  return dispatch => {
    axios.get(`${INVITATION_ENDPOINT}`, getAuthorizationHeader())
      .then(response => {
        dispatch(invitationsFetched(response.data));
      })
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function fetchMembers(groupId, successCallback, errorCallback) {
  return dispatch => {
    axios.get(`${GROUP_ENDPOINT}${groupId}/user`, getAuthorizationHeader())
      .then(response => {
          dispatch(membersFetched(response.data, groupId));
      })
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function removeMember(groupId, memberId, errorCallback) {
  return dispatch => {
    axios.delete(`${GROUP_ENDPOINT}${groupId}/user/${memberId}`, getAuthorizationHeader())
      .then(response => {
          dispatch(memberRemoved(response.data, groupId));
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function createExpense(formData, groupId, successCallback, errorCallback) {
  return dispatch => {
    axios.post(`${EXPENSE_ENDPOINT}${groupId}`,
      formData
    , getAuthorizationHeader())
      .then(({data}) => {
        successCallback();
        dispatch(expenseCreated(data, groupId))
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
    };
}

export function removeSharer(sharerId, expenseId, errorCallback) {
  return dispatch => {
    axios.delete(`${EXPENSE_ENDPOINT}${expenseId}/sharer/${sharerId}`, getAuthorizationHeader())
      .then(() => {
        dispatch(sharerRemoved(sharerId, expenseId));
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function updateExpense(values, expenseId, errorCallback) {
  return dispatch => {
    axios.put(`${EXPENSE_ENDPOINT}${expenseId}`,
      {
        ...values
      }, getAuthorizationHeader())
      .then(({data}) => {
        dispatch(expenseUpdated(data))
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
  };
}

export function fetchExpense(expenseId, successCallback, errorCallback) {
  return dispatch => {
    axios.get(`${EXPENSE_ENDPOINT}${expenseId}`, getAuthorizationHeader())
      .then(({data}) => {
        dispatch(expenseFetched(data))
      })
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
    };
}

export function fetchExpenses(successCallback, errorCallback) {
  return dispatch => {
    axios.get(`${EXPENSE_ENDPOINT}`, getAuthorizationHeader())
      .then(({data}) => dispatch(expensesFetched(data)))
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
    };
}

export function deleteExpense(expenseId, groupId, errorCallback) {
  return dispatch => {
    axios.delete(`${EXPENSE_ENDPOINT}${expenseId}`, getAuthorizationHeader())
      .then(() => {
        if (groupId) {
          dispatch(expenseDeletedFromGroup(expenseId, groupId)); // in case expense is being deleted from a group
          // changing the state differs from simply deleting an expense
        } else {
          dispatch(expenseDeleted(expenseId));
        }
      }).catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
    };
}

export function addSharer(sharerId, expenseId, successCallback, errorCallback) {
  return dispatch => {
    axios.patch(`${EXPENSE_ENDPOINT}${expenseId}/sharer/${sharerId}`, {},
        getAuthorizationHeader())
      .then(({data}) => {
        dispatch(sharerAdded(expenseId, data));
      })
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function fetchGroups(successCallback, errorCallback) {
  return dispatch => {
    axios.get(GROUP_ENDPOINT, getAuthorizationHeader())
      .then(response => dispatch(groupsFetched(response.data)))
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function fetchGroup(id, successCallback, errorCallback) {
  return (dispatch) => {
    axios.get(`${GROUP_ENDPOINT}${id}`, getAuthorizationHeader())
      .then(response => dispatch(groupFetched(response.data)))
      .then(() => successCallback())
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      })
  };
}

export function updateGroup(values, groupId, errorCallback) {
  return dispatch => {
    axios.put(`${GROUP_ENDPOINT}${groupId}`,
      {
        ...values
      }, getAuthorizationHeader())
      .then(({data}) => {
        dispatch(groupUpdated(data))
      })
      .catch(({response}) => {
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
  };
}

export function createGroup(values, successCallback, errorCallback) {
  return (dispatch) => {
    axios.post(GROUP_ENDPOINT,
      {
        name: values.name,
        description: values.description
      }, getAuthorizationHeader())
      .then(({data}) => dispatch(groupCreated(data)))
      .then(() => successCallback())
      .catch(({response}) => { //TODO manually test it
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          } else {
            errorCallback(response.data.message);
          }
        }
      });
    };
}

export function deleteGroup(groupID) {
  return dispatch => {
    axios.delete(`${GROUP_ENDPOINT}${groupID}`, getAuthorizationHeader())
      .then(response => {
        dispatch(groupDeleted(response));
      }).catch(response => { //TODO manually test it
        if (!response) {
          //Network error
          //show a sticky message with offline message
        } else {
          if (response.status ===  401) { // Unauthorized
            logout();
          }
        }
      });
  };
}
